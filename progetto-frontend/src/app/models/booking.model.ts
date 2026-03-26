export interface BookingDTO {

  id : number;
  bookingDate: Date | null ;
  duration: Date | null ;
  code: number ;
  userID: number | null ;
  laboratoryID: number | null ;
  laboratorySeats : number;
  seats : number;
}
