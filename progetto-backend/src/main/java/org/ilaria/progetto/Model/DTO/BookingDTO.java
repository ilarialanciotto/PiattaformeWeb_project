package org.ilaria.progetto.Model.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class BookingDTO {

    private long id;
    private LocalDateTime bookingDate;
    private LocalTime duration;
    private Integer code =0;
    private long userID;
    private long laboratoryID;
    private int laboratorySeats;

}
