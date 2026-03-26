package org.ilaria.progetto.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ClassroomBookingDTO {

    private long id;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private LocalTime duration;
}
