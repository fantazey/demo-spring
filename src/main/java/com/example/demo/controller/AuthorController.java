package com.example.demo.controller;

import com.example.demo.dto.AuthorDto;
import com.example.demo.mapper.AuthorMapper;
import com.example.demo.model.Author;
import com.example.demo.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/author")
public class AuthorController {
    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorMapper authorMapper;

    @GetMapping(value = "/{id}")
    public ResponseEntity<AuthorDto> getById(@PathVariable("id") Long id) {
        Author author = authorService.getAuthorById(id);
        AuthorDto dto = authorMapper.authorToDto(author);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getList() {
        List<Author> authors = authorService.getAll();
        List<AuthorDto> result = authorMapper.authorsToDtos(authors);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        Optional.ofNullable(ex.getCause()).ifPresent(cause -> response.put("error", cause.toString()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
