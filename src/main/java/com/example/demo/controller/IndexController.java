package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class IndexController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;

    @GetMapping(value = "/populate")
    public ResponseEntity<?> populate() throws IOException {
        Genre genre = new Genre("test");
        genreRepository.save(genre);

        File f = ResourceUtils.getFile("classpath:fake_authors.json");
        JsonNode data = objectMapper.readTree(f);
        ArrayNode results = (ArrayNode) data.get("results");
        List<Author> authorList = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();
        for (JsonNode result: results) {
            String fName = result.get("name").get("first").asText();
            String lName = result.get("name").get("last").asText();
            Author author = new Author(fName, lName);
            authorList.add(author);
            JsonNode location = result.get("location");
            String city = location.get("city").asText();
            JsonNode street = location.get("street");
            int number = street.get("number").asInt();
            String streetName = street.get("name").asText();
            String postCode = location.get("postcode").asText();
            String title = String.format("[%s - %s - %s.%s (%s)]", fName, city, streetName, number, postCode);
            Book book = new Book(title, author, genre);
            bookList.add(book);
        }
        try {
            authorRepository.saveAll(authorList);
        } catch(Exception ignored) {}
        try {
            bookRepository.saveAll(bookList);
        } catch(Exception ignored) {}

        Map<String, String> response = new HashMap<>();
        long books = bookRepository.count();
        long authors = authorRepository.count();
        long genres = genreRepository.count();
        response.put("authors", String.valueOf(authors));
        response.put("books", String.valueOf(books));
        response.put("genres", String.valueOf(genres));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
