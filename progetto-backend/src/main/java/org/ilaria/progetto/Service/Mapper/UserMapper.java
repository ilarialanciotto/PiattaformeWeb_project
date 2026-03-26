package org.ilaria.progetto.Service.Mapper;

import org.ilaria.progetto.Model.DTO.UserDTO;
import org.ilaria.progetto.Model.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);
}

