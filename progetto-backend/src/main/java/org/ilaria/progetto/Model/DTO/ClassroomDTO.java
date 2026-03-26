package org.ilaria.progetto.Model.DTO;

import lombok.Data;

import java.util.LinkedList;

@Data
public class ClassroomDTO {

    private long id;
    private String cube;
    private int numberOfSeats, totalSeats, floor;
    private boolean laboratory;
    private LinkedList<ContentDTO> contents;
    private String personInCharge;


}
