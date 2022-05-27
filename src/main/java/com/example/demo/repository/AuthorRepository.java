package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findByLastName(String lastName);
    Optional<Author> findById(Long id);
}
