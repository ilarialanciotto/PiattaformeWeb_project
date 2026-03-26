import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders } from "@angular/common/http";
import { ToastrService } from "ngx-toastr";
import { ClassroomDTO } from "../../models/classroom.model";
import { LoggedInUser } from "../../models/loggedUser.model";
import { Subscription } from "rxjs";
import { BookingDTO } from '../../models/booking.model';
import {TeacherDTO} from '../../models/teacher.model';
import {AdminService} from '../../services/teacher-adminService/admin.service';

@Component({
  selector: 'app-dashboardAdmin',
  templateUrl: './dashboardAdmin.component.html',
  standalone: false,
  styleUrls: ['./dashboardAdmin.component.css']
})
export class DashboardAdmin implements OnInit, OnDestroy {

  constructor(
    private adminService: AdminService,
    private toastr: ToastrService,
  ) { }

  @Input() selectedAula: ClassroomDTO | null = null;
  @Input() headers: HttpHeaders | undefined;
  @Input() currentUser: LoggedInUser | null = null;
  @Input() userSubscription: Subscription | undefined;
  @Input() guideStep: number = 0;
  @Input() toAccept: BookingDTO[] = [];
  availableTeachers: TeacherDTO[] = [];
  selectedTeacherId: number = -1;

  ngOnInit(): void {
    this.loadTeachers();
  }

  loadTeachers(): void {
    if (!this.headers) return;

    this.adminService.findTeachers(this.headers).subscribe({
      next: (teachers) => this.availableTeachers = teachers,
      error: () => this.toastr.error('Error load teacher','Teacher')
    });
  }

  assignTeacherToClassroom(teacherId: number): void {
    if (!this.selectedAula || !this.headers || !teacherId) return;

    this.adminService.assignPersonInCharge(this.selectedAula.id, teacherId, this.headers).subscribe({
      next: (res: string) => {
        this.toastr.info(res, 'Teacher');
        this.selectedTeacherId = -1;
      },
      error: (error) => this.toastr.error(error.err, 'Teacher')
    });
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }


}
