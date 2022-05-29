package com.example.demo.service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    public Book getById(Long id) {
        Optional<Book> bookRecord = bookRepository.findById(id);
        return bookRecord.orElseThrow(()-> new BookNotFoundException(id));
    }

    public List<Book> getAuthorById(Long id) {
        return bookRepository.findAllByAuthorId(id);
    }

    public List<Book> getAll() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), true)
            .collect(Collectors.toList());
    }
}
