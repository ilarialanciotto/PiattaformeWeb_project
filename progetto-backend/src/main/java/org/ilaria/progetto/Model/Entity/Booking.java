package org.ilaria.progetto.Model.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime bookingDate;
    private boolean approved = false;
    private LocalTime duration;
    private Integer code = 0;

    @Version
    private int version;

    @ManyToOne
    private Classroom classroom;

    @ManyToOne
    private User user;

    public String toString(){
        return "Booking [id=" + id + ", booking date=" + bookingDate +
                ", duration=" + duration + ", approved=" + approved +
                ", classroom id=" + (classroom != null ? classroom.getId() : "") +
                ", user id=" + (user != null ? user.getId() : "") + "]";
    }

}

