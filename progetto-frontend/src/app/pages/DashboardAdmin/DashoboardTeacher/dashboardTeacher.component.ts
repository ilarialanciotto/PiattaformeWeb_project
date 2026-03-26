import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders } from "@angular/common/http";
import { ToastrService } from "ngx-toastr";
import {TeacherService} from '../../../services/teacher-adminService/teacher.service';
import {ClassroomDTO} from '../../../models/classroom.model';
import {LoggedInUser} from '../../../models/loggedUser.model';
import {Subscription} from 'rxjs';
import {BookingDTO} from '../../../models/booking.model';


@Component({
  selector: 'app-dashboardTeacher',
  templateUrl: './dashboardTeacher.component.html',
  standalone: false,
  styleUrls: ['./dashboardTeacher.component.css']
})
export class DashboardTeacher implements OnInit, OnDestroy {

  constructor(
    private teacherService: TeacherService,
    private toastr: ToastrService,
  ) { }

  @Input() selectedAula: ClassroomDTO | null = null;
  @Input() headers: HttpHeaders | undefined;
  @Input() currentUser: LoggedInUser | null = null;
  @Input() userSubscription: Subscription | undefined;
  @Input() guideStep: number = 0;
  @Input() toAccept: BookingDTO[] = [];

  ngOnInit(): void {}

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  classroomBookings(booking : BookingDTO) : void{
    if (!this.headers || !booking || !booking.laboratoryID) return;
    booking.seats = booking.laboratorySeats;
    this.teacherService.getBookingsClassroom(booking.laboratoryID, this.headers)
      .subscribe({
        next: (response: BookingDTO[]) => {
          for (const b of response) {
            if (
              b.id !== booking.id &&
              b.bookingDate && booking.bookingDate &&
              b.duration && booking.duration
            ) {
              const start1 = new Date(booking.bookingDate).getHours()
              const end1 =  start1 + Number(String(booking.duration).substring(0,2));
              const start2 = new Date(b.bookingDate).getHours()
              const end2 =start2 + Number(String(b.duration).substring(0,2));
              if (start1 < end2 && start2 < end1) {
                booking.seats--;
                if(booking.seats<0) booking.seats=0
              }
            }
          }
        },
        error: (err: { err: string | undefined; }) => {
          this.toastr.error(err.err, 'booking');
        }
      });
  }

  bookingCheck() {
    if (!this.headers) {
      return;
    }
    this.teacherService.getBookingToAccept(this.headers)
      .subscribe({
        next: (response: BookingDTO[]) => {
          this.toAccept = response;
          for(const b of response){
            this.classroomBookings(b);
          }
        },
        error: (error: { error: string }) => {
          this.toastr.error(error.error, 'Booking');
        }
      });
  }

  accept(id: number) {
    if (!this.headers) {
      return;
    }
    this.teacherService.approveBooking(id, this.headers)
      .subscribe({
        next: (response: string | undefined) => {
          this.bookingCheck();
          this.toastr.info(response, "Booking");
        },
        error: (error: { error: string | undefined; }) => {
          this.toastr.error(error.error, 'Booking');
        }
      });
  }
}
