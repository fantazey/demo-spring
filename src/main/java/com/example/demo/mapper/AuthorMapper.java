package com.example.demo.mapper;

import com.example.demo.dto.AuthorDto;
import com.example.demo.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto AuthorToDto(Author author);
}
