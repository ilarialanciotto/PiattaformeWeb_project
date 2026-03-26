import { Component } from '@angular/core';
import { UserDTO } from '../../../models/user.model';
import { JwtResponse } from '../../../models/jwt-response.model';
import { Router } from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {UserService} from '../../../services/userService/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: false,
  styleUrl: './login.component.css'
})

export class LoginComponent {

  user: UserDTO = { email: '', password: ''};
  jwtResponse: JwtResponse | null = null;

  constructor(private userService: UserService, private router: Router, private toastr: ToastrService) {}

  onLogin() {
    this.userService.login(this.user).subscribe({
      next: res => {
        this.jwtResponse = res;
        this.router.navigate(['/dashboard']);
      },
      error: (err: { error: string | undefined; }) => {
        this.toastr.error(err.error, 'login');
        this.jwtResponse = null;
      }
    });
  }
}

