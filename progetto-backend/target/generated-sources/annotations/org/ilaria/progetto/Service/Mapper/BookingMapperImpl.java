package org.ilaria.progetto.Service.Mapper;

import javax.annotation.processing.Generated;
import org.ilaria.progetto.Model.DTO.BookingDTO;
import org.ilaria.progetto.Model.Entity.Booking;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T09:31:01+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingDTO toDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingDTO bookingDTO = new BookingDTO();

        if ( booking.getId() != null ) {
            bookingDTO.setId( booking.getId() );
        }
        bookingDTO.setBookingDate( booking.getBookingDate() );
        bookingDTO.setDuration( booking.getDuration() );
        bookingDTO.setCode( booking.getCode() );

        return bookingDTO;
    }

    @Override
    public Booking toEntity(BookingDTO bookingDTO) {
        if ( bookingDTO == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setBookingDate( bookingDTO.getBookingDate() );
        booking.setDuration( bookingDTO.getDuration() );
        booking.setCode( bookingDTO.getCode() );

        return booking;
    }
}
