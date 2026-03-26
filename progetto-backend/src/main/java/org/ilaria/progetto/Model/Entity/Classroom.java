package org.ilaria.progetto.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cube;
    private int numberOfSeats, totalSeats, floor;
    private boolean laboratory;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User personInCharge;

}
