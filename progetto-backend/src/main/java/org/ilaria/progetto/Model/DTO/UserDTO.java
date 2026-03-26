package org.ilaria.progetto.Model.DTO;

import lombok.Data;
import org.ilaria.progetto.Role;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String teacherCode;
    private long classroomInID;
    private Role role;

}
