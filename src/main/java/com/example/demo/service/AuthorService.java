package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    public Author getAuthorById(Long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        return optionalAuthor.orElseThrow(
            ()-> new EntityNotFoundException(String.format("Author with id=%s not exists", id))
        );
    }

    public List<Author> findByLastName(String lastName) {
        return authorRepository.findByLastName(lastName);
    }

    public List<Author> getAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), true)
            .collect(Collectors.toList());
    }
}
