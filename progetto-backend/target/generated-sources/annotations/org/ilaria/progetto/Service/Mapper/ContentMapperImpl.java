package org.ilaria.progetto.Service.Mapper;

import javax.annotation.processing.Generated;
import org.ilaria.progetto.Model.DTO.ContentDTO;
import org.ilaria.progetto.Model.Entity.Content;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T09:31:01+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ContentMapperImpl implements ContentMapper {

    @Override
    public ContentDTO toDto(Content content) {
        if ( content == null ) {
            return null;
        }

        ContentDTO contentDTO = new ContentDTO();

        contentDTO.setName( content.getName() );

        return contentDTO;
    }

    @Override
    public Content toEntity(ContentDTO contentDTO) {
        if ( contentDTO == null ) {
            return null;
        }

        Content content = new Content();

        content.setName( contentDTO.getName() );

        return content;
    }
}
