import {Time} from '@angular/common';

export interface ClassroomBookingDTO {
  id : number;
  bookingDate : Date;
  bookingTime : Time;
  duration : Time;
}
