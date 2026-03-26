import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {BookingDTO} from '../../models/booking.model';
import {ClassroomDTO} from '../../models/classroom.model';
import {TeacherDTO} from '../../models/teacher.model';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

  private teacherApiPath = `http://localhost:8080/FreeClassroom/home/teacher_and_admin`;

  constructor(private http: HttpClient) { }

  getBookingToAccept(headers: HttpHeaders): Observable<BookingDTO[]> {
    const url = `${this.teacherApiPath}/getList`;
    return this.http.get<BookingDTO[]>(url, { headers: headers });
  }

  approveBooking(bookingID: number, headers: HttpHeaders): Observable<string> {
    const url = `${this.teacherApiPath}/approveBooking`;
    return this.http.post(url, bookingID, { headers: headers, responseType: 'text' });
  }

  getBookingsClassroom(classroomId : number,headers: HttpHeaders): Observable<BookingDTO[]> {
    const url = `${this.teacherApiPath}/getBookingClassroom`;
    return this.http.post<BookingDTO[]>(url, classroomId, { headers: headers });
  }
}
