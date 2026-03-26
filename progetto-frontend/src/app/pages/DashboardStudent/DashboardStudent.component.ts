import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ClassroomDTO } from '../../models/classroom.model';
import { ToastrService } from 'ngx-toastr';
import { LoggedInUser } from '../../models/loggedUser.model';
import { Subscription } from 'rxjs';
import {StudentService} from '../../services/studentService/student.service';
import {ClassroomBookingDTO} from '../../models/classroomBookingDTO.model';

@Component({
  selector: 'app-dashboardStudent',
  templateUrl: './DashboardStudent.component.html',
  standalone: false,
  styleUrls: ['./DashboardStudent.component.css']
})
export class DashboardStudent implements OnInit, OnDestroy {

  constructor(
    private studentService: StudentService,
    private toastr: ToastrService
  ) { }

  @Input() guideStep: number = 0;
  @Input() bookings: ClassroomBookingDTO[] = [];
  @Input() selectedAula: ClassroomDTO | null = null;
  @Output() selectedAulaChange = new EventEmitter<null>();

  codes: String = '';
  insertCode: number = 0;
  enterClass : number = -1;

  @Input() headers: HttpHeaders | undefined;
  @Input() currentUser: LoggedInUser | null = null;
  @Input() userSubscription: Subscription | undefined;

  enterClassroom(): void {
    if (!this.selectedAula) {
      return;
    }
    this.reportEnter(this.selectedAula.id, this.insertCode);
  }

  exitClassroom(): void {
    if (!this.headers) {
      this.toastr.error("Error in exiting the classroom", 'Exit');
      return;
    }
    this.studentService.exitToClassroom(this.enterClass,this.headers)
      .subscribe({
        next: (response: string | undefined) => {
          this.toastr.info(response, 'Exit');
          this.selectedAula = null;
          this.selectedAulaChange.emit(null);
          this.enterClass=-1;
          localStorage.setItem("enter",JSON.stringify(-1));
        },
        error: (error: { error: string | undefined; }) => {
          this.toastr.error(error.error, 'Exit');
        }
      });
  }

  checkCode() {
    if (!this.headers) return;
    this.codes= "";
    this.studentService.checkCode(this.headers).subscribe({
      next: (response: String) => {
        this.codes = response;
        this.selectedAula = null;
      },
      error: (error) => {
        this.toastr.error(error.error, 'Codes');
      }
    });
  }

  reportEnter(classroomID: number, code: number): void {
    if (!this.headers) return;
    this.studentService.reportEnter(classroomID, code, this.headers)
      .subscribe({
        next: (response: string | undefined) => {
          this.toastr.info(response, 'Enter');
          this.enterClass = classroomID;
          localStorage.setItem("enter",JSON.stringify(classroomID));
        },
        error: (error: { error: string | undefined; }) => {
          this.toastr.error(error.error, 'Enter');
        }
      });
  }

  ngOnInit(): void {
    if(this.currentUser) {
      const enter = Number(localStorage.getItem("enter"));
      if(enter) this.enterClass = enter ;
      else {
        this.enterClass = this.currentUser.enterClassroom;
        localStorage.setItem("enter",JSON.stringify(this.currentUser.enterClassroom));
      }
    }
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
}
