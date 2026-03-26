import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription} from 'rxjs';
import { ClassroomDTO } from '../../models/classroom.model';
import { LoggedInUser } from '../../models/loggedUser.model';
import { FeedbackDTO } from '../../models/feedback.model';
import {UserService} from '../../services/userService/user.service';
import {StudentService} from '../../services/studentService/student.service';
import {ClassroomBookingDTO} from '../../models/classroomBookingDTO.model';
import {BookingDTO} from '../../models/booking.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: false,
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {

  constructor(
    private router: Router,
    private toastr: ToastrService,
    private userService: UserService,
    private studentService : StudentService
  ) { }

  feedback: FeedbackDTO = { feedback: "" };
  isSidebarOpen: boolean = false;
  freeClassroom: ClassroomDTO[] = [];
  bookings: ClassroomBookingDTO[] = [];
  selectedAula: ClassroomDTO | null = null;
  laboratory: boolean = false;
  showForm: boolean = false;
  bookingDuration: any = null;
  bookingDate: any = null;
  headers!: HttpHeaders;
  currentUser: LoggedInUser | null = null;
  userSubscription: Subscription | undefined;
  showSettings: boolean = false;
  newEmail: string = '';
  newPassword: any;
  confirmPassword: any;
  role: string = "";
  readonly ROLE_STUDENT: string = "ROLE_STUDENT";
  readonly ROLE_TEACHER: string = "ROLE_TEACHER";
  readonly ROLE_ADMIN: string = "ROLE_ADMIN";
  guideStep: number = 0;
  myBooking: BookingDTO[] = [];
  bookingToConfirm: number | null = null;
  private confirmTimeout: any;
  activeView: 'feedback' | 'booking' = 'feedback';
  userToken: any;
  totalItems = 0;
  pageSize = 5;
  currentPage = 0;

  ngOnInit(): void {
    this.userToken = this.userService.getToken();
    if (this.userToken) {
      this.headers = new HttpHeaders().set('Authorization', `Bearer ${(this.userToken)}`);
    } else {
      this.router.navigate(['/login']);
      return;
    }
    this.userSubscription = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (this.currentUser) {
        this.role = this.currentUser.role;
      } else {
        this.router.navigate(['/login']);
      }
    });
  }

  openSidebar(): void { this.isSidebarOpen = true; }
  closeSidebar(): void { this.isSidebarOpen = false; }
  onSettings(): void { this.showSettings = true; }
  offSettings(): void { this.showSettings = false; }
  init(){ this.loadFreeClassroom(0);}

  loadFreeClassroom(page: number) {
    this.userService.getFreeClassroom(page, this.pageSize,this.headers).subscribe(data => {
      this.freeClassroom = data.content;
      this.totalItems = data.totalElements;
      this.currentPage = data.number;
    });
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.pageSize);
  }

  changeView(view: 'feedback' | 'booking'): void {
    this.activeView = view;
    if (this.activeView === 'booking') {
      this.loadMyBooking();
    }
  }

  loadMyBooking(): void {
    if (!this.headers) return;
    const localStorageBooking = localStorage.getItem("booking");
    if(localStorageBooking) {
      this.myBooking = JSON.parse(localStorageBooking);
      return;
    }
    this.userService.getBooking(this.headers).subscribe({
      next: (date) => {
        this.myBooking = date;
        localStorage.setItem("booking", JSON.stringify(this.myBooking));
      },
      error: (err) => {
        this.toastr.error(err.error,'Booking');
      }
    });
  }

  private executeDeletion(toDeleteID: number): void {
    if (!this.headers) return;
    this.userService.delete(toDeleteID, this.headers).subscribe({
      next: (responseMessage) => {
        this.toastr.info(responseMessage, 'Booking');
        this.myBooking = this.myBooking.filter(p => p.id !== toDeleteID);
        localStorage.removeItem("booking")
      },
      error: (err) => {
        this.toastr.error(err.err,'Booking');
      }
    });
  }

  onDeleteBooking(BookingID: number): void {
    if (this.bookingToConfirm === BookingID) {
      this.executeDeletion(BookingID);
      this.bookingToConfirm = null;
      clearTimeout(this.confirmTimeout);
    } else {
      this.bookingToConfirm = BookingID;
      this.confirmTimeout = setTimeout(() => {
        if (this.bookingToConfirm === BookingID) {
          this.bookingToConfirm = null;
        }
      }, 4000);
    }
  }

  cancelConfirm(): void {
    this.bookingToConfirm = null;
    clearTimeout(this.confirmTimeout);
  }

  loadBooking(): void {
    if (!this.selectedAula) return;
    this.studentService.getBookingForClassroom(this.selectedAula.id, this.headers).subscribe({
      next: (date: ClassroomBookingDTO[]) => this.bookings = date,
      error: (err: { error: string | undefined; }) => this.toastr.error(err.error, 'Booking')
    });
  }

  bookWithDuration(): void {
    if (!this.selectedAula || !this.bookingDuration || !this.bookingDate) return;
    const bookingDTO = {
      laboratoryID: this.selectedAula.id,
      duration: this.bookingDuration,
      bookingDate: this.bookingDate
    };
    this.userService.bookingLaboratory(bookingDTO, this.headers).subscribe({
      next: (response: string | undefined) => {
        this.toastr.info(response, 'Booking');
        this.showForm = false;
        localStorage.removeItem("booking")
      },
      error: (err: { error: string | undefined; }) => this.toastr.error(err.error, 'Booking')
    });
  }

  insertFeedback(): void {
    this.userService.sendFeedback(this.feedback, this.headers).subscribe({
      next: (response: string | undefined) => {
        this.toastr.info(response, 'Feedback');
        this.feedback = { feedback: "" };
      },
      error: (err: { error: string | undefined; }) => this.toastr.error(err.error, 'Feedback')
    });
  }

  submit(): void {
    if (this.newPassword !== this.confirmPassword) {
      this.toastr.error("The passwords do not match", 'Edit Data');
      return;
    }
    const datesToEdit = { email: this.newEmail, password: this.newPassword };
    this.userService.updateCredentials(datesToEdit, this.headers).subscribe({
      next: response => {
        this.toastr.info(response, 'Edit Data');
        this.onLogout();
      },
      error: err => this.toastr.error(err.error, 'Edit Data')
    });
  }

  selectAula(classroom: ClassroomDTO): void {
    if (this.selectedAula?.id === classroom.id) {
      this.selectedAula = null;
      this.laboratory = false;
    } else {
      this.selectedAula = classroom;
      this.laboratory = this.selectedAula.laboratory;
      if (this.role === this.ROLE_STUDENT) {
        this.loadBooking();
      }
    }
  }

  onLogout(): void {
    this.userService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.userSubscription?.unsubscribe();
  }

}
