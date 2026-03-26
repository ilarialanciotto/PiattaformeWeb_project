import {Routes} from '@angular/router';
import {HomeComponent} from './pages/DashboardShared/home/home.components';
import {LoginComponent} from './pages/DashboardShared/login/login.component';
import {HeaderComponent} from './pages/DashboardShared/header-logo/header.component';
import {LogoComponent} from './pages/DashboardShared/header-logo/logo.component';
import {GuideComponent} from './pages/DashboardShared/header-logo/guide.component';
import {DashboardComponent} from './pages/DashboardShared/dashboard.component';
import {AuthGuard} from './services/auth.guard';
import {DashboardAdmin} from './pages/DashboardAdmin/dashboardAdmin.component';
import {DashboardStudent} from './pages/DashboardStudent/DashboardStudent.component';
import {RegisterComponent} from './pages/DashboardShared/register/register.component';
import {DashboardTeacher} from './pages/DashboardAdmin/DashoboardTeacher/dashboardTeacher.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'header', component: HeaderComponent},
  { path: 'logo', component: LogoComponent},
  { path: 'guide', component: GuideComponent},
  { path: 'dashboard', component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  { path: 'dashboardTeacher', component: DashboardTeacher,
    canActivate: [AuthGuard]
  },
  { path: 'dashboardAdmin', component: DashboardAdmin,
    canActivate: [AuthGuard]
  },
  { path: 'dashboardStudent', component: DashboardStudent,
    canActivate: [AuthGuard]
  },
  { path: 'register', component: RegisterComponent }
];

