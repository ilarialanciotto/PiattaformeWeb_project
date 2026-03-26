package org.ilaria.progetto.Model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherDTO {
    private long ID;
    private String name;
    private String email;
}
