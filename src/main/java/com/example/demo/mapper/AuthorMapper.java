package com.example.demo.mapper;

import com.example.demo.dto.AuthorDto;
import com.example.demo.model.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto authorToDto(Author author);

    List<AuthorDto> authorsToDtos(List<Author> authors);
}
