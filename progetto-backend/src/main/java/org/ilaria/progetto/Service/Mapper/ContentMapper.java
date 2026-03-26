package org.ilaria.progetto.Service.Mapper;

import org.ilaria.progetto.Model.DTO.ContentDTO;
import org.ilaria.progetto.Model.Entity.Content;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ContentMapper {

    ContentDTO toDto(Content content);

    @Mapping(target = "id", ignore = true)
    Content toEntity(ContentDTO contentDTO);
}
