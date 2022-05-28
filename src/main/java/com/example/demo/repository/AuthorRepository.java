package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findByLastName(String lastName);
    Optional<Author> findById(Long id);
}
