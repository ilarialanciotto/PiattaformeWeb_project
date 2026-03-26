package org.ilaria.progetto.Service.Mapper;

import javax.annotation.processing.Generated;
import org.ilaria.progetto.Model.DTO.UserDTO;
import org.ilaria.progetto.Model.Entity.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T09:31:01+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setName( user.getName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setTeacherCode( user.getTeacherCode() );
        userDTO.setRole( user.getRole() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setName( userDTO.getName() );
        user.setEmail( userDTO.getEmail() );
        user.setPassword( userDTO.getPassword() );
        user.setTeacherCode( userDTO.getTeacherCode() );
        user.setRole( userDTO.getRole() );

        return user;
    }
}
