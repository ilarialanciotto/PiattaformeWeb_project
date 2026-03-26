import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TeacherDTO} from '../../models/teacher.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private adminApiPath = `http://localhost:8080/FreeClassroom/home/teacher_and_admin/Admin`;

  constructor(private http: HttpClient) { }

  assignPersonInCharge(classroomID: number, teacherID: number, headers: HttpHeaders): Observable<string> {
    const url = `${this.adminApiPath}/personInCharge`;
    return this.http.post(url, {classroomID,teacherID}, { headers: headers, responseType: 'text' });
  }

  findTeachers(headers: HttpHeaders): Observable<TeacherDTO[]> {
    const url = `${this.adminApiPath}/getTeacher`;
    return this.http.get<TeacherDTO[]>(url, { headers: headers});
  }
}
