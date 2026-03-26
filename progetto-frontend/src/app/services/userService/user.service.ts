import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable, of, tap} from 'rxjs';
import {ClassroomDTO} from '../../models/classroom.model';
import {FeedbackDTO} from '../../models/feedback.model';
import {UserDTO} from '../../models/user.model';
import {JwtResponse} from '../../models/jwt-response.model';
import {LoggedInUser} from '../../models/loggedUser.model';
import {BookingDTO} from '../../models/booking.model';
import {Page} from '../../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private currentUserSubject: BehaviorSubject<LoggedInUser | null>;
  currentUser$: Observable<LoggedInUser | null>;
  private apiBasePath = 'http://localhost:8080/FreeClassroom/home';

  constructor(private http: HttpClient) {
    const storedUserString = localStorage.getItem('loggedInUser');
    const initialUser: LoggedInUser | null = storedUserString ? JSON.parse(storedUserString) : null;
    this.currentUserSubject = new BehaviorSubject<LoggedInUser | null>(initialUser);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  register(user: UserDTO): Observable<string> {
    const url = `${this.apiBasePath}/auth/register`;
    return this.http.post(url, user, { responseType: 'text' });
  }

  login(user: UserDTO): Observable<JwtResponse> {
    const url = `${this.apiBasePath}/auth/login`;
    return this.http.post<JwtResponse>(url, user).pipe(
      tap(res => {
        if (res && res.jwt && res.username && res.role) {
          localStorage.setItem('token', res.jwt);
          const userData: LoggedInUser = { username: res.username, role: res.role , enterClassroom : res.enterClassroom};
          localStorage.setItem('loggedInUser', JSON.stringify(userData));
          this.currentUserSubject.next(userData);
        } else {
          this.logout();
        }
      })
    );
  }


  updateCredentials(dates: { email: string, password: string }, headers: HttpHeaders): Observable<string> {
    const url = `${this.apiBasePath}/updateCredentials`;
    return this.http.post(url, dates, { headers: headers, responseType: 'text' });
  }

  getBooking(headers: HttpHeaders): Observable<BookingDTO[]> {
    const url = `${this.apiBasePath}/getBooking`;
    return this.http.get<BookingDTO[]>(url, { headers: headers });
  }

  delete(bookingID: number, headers: HttpHeaders): Observable<string> {
    const url = `${this.apiBasePath}/delete`;
    return this.http.post(url, bookingID, { headers: headers, responseType: 'text' });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('loggedInUser');
    localStorage.removeItem("booking")
    localStorage.removeItem("enter");
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getFreeClassroom(page: number, size: number,headers: HttpHeaders): Observable<Page<ClassroomDTO>> {
    const url = `${this.apiBasePath}/getList`;
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<ClassroomDTO>>(url, { headers: headers , params });
  }

  bookingLaboratory(bookingDate: any, headers: HttpHeaders): Observable<string> {
    const url = `${this.apiBasePath}/booking`;
    return this.http.post(url, bookingDate, { headers: headers, responseType: 'text' });
  }

  sendFeedback(feedback: FeedbackDTO, headers: HttpHeaders): Observable<string> {
    const url = `${this.apiBasePath}/feedback`;
    return this.http.post(url, feedback, { headers: headers, responseType: 'text' });
  }
}
