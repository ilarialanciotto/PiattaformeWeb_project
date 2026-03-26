import {UserDTO} from '../../../models/user.model';
import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/userService/user.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: false,
  styleUrl: './register.component.css'
})

export class RegisterComponent implements OnInit{

  user: UserDTO = { name: '', email: '', password: '', teacherCode: '' };
  success = '';
  error = '';

  constructor(private utenteService: UserService,private toastr: ToastrService,private router: Router) {}

  onSubmit() {

    if (this.user.teacherCode?.trim() === '') {
      this.user.teacherCode = null;
    }

    this.utenteService.register(this.user).subscribe({
      next: res => {
        this.toastr.info(res,"Register");
        this.error = '';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);
      },
      error: err => {
        this.toastr.error(err.error,"Register");
        this.success = '';
      }
    });
  }

  ngOnInit(): void {
  }
}
