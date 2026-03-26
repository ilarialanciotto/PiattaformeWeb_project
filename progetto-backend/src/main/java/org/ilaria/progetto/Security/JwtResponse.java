package org.ilaria.progetto.Security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {

    private String jwt;
    private String username;
    private String role;
    private long enterClassroom;

}
