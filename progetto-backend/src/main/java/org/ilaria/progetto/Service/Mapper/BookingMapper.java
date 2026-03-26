package org.ilaria.progetto.Service.Mapper;

import org.ilaria.progetto.Model.DTO.BookingDTO;
import org.ilaria.progetto.Model.Entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "laboratoryID", ignore = true)
    @Mapping(target = "userID", ignore = true)
    @Mapping(target = "laboratorySeats" , ignore = true)
    BookingDTO toDto(Booking booking);

    @Mapping(target = "classroom", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Booking toEntity(BookingDTO bookingDTO);

}
