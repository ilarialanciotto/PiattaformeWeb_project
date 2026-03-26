import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, of, tap, throwError} from 'rxjs';
import {ClassroomBookingDTO} from '../../models/classroomBookingDTO.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private studentApiPath = `http://localhost:8080/FreeClassroom/home/Student`;

  constructor(private http: HttpClient) {}

  exitToClassroom(classroomID: number | null, headers: HttpHeaders): Observable<string> {
    const url = `${this.studentApiPath}/Exit`;
    return this.http.post(url, classroomID, { headers: headers, responseType: 'text' });
  }

  checkCode(headers: HttpHeaders): Observable<string> {
    const url = `${this.studentApiPath}/checkCode`;
    return this.http.get(url, { headers: headers, responseType: 'text' });
  }

  getBookingForClassroom(classroomID: number, headers: HttpHeaders): Observable<ClassroomBookingDTO[]> {
    const url = `${this.studentApiPath}/classroomCheck`;
    return this.http.post<ClassroomBookingDTO[]>(url, classroomID, { headers: headers });
  }

  reportEnter(classroomID: number, code: number, headers: HttpHeaders): Observable<string> {
    const url = `${this.studentApiPath}/Enter`;
    const body = { classroomID: classroomID, code: code };
    return this.http.post(url, body, { headers: headers, responseType: 'text' });
  }
}
