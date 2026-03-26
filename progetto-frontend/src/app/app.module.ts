import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {AppComponent} from './app.component';
import {NgModule} from '@angular/core';
import {routes} from './app.routes';
import { HttpClientModule } from '@angular/common/http';
import {AuthGuard} from './services/auth.guard';
import { ToastrModule } from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HomeComponent} from './pages/DashboardShared/home/home.components';
import {LoginComponent} from './pages/DashboardShared/login/login.component';
import {RegisterComponent} from './pages/DashboardShared/register/register.component';
import {DashboardComponent} from './pages/DashboardShared/dashboard.component';
import {HeaderComponent} from './pages/DashboardShared/header-logo/header.component';
import {LogoComponent} from './pages/DashboardShared/header-logo/logo.component';
import {DashboardStudent} from './pages/DashboardStudent/DashboardStudent.component';
import {DashboardAdmin} from './pages/DashboardAdmin/dashboardAdmin.component';
import {GuideComponent} from './pages/DashboardShared/header-logo/guide.component';
import {DashboardTeacher} from './pages/DashboardAdmin/DashoboardTeacher/dashboardTeacher.component';


@NgModule({
  declarations: [
    AppComponent,
    GuideComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    DashboardTeacher,
    DashboardComponent,
    HeaderComponent,
    LogoComponent,
    DashboardStudent,
    DashboardAdmin
  ],
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 10000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
  ],
  providers: [AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
