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
import lombok.extern.slf4j.Slf4j;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@Slf4j
public class IndexController {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    GenreRepository genreRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping(value = "/populate")
    public ResponseEntity<?> populate() throws IOException {
        Optional<Genre> optionalGenre = genreRepository.findById(1L);
        Genre genre;
        if (optionalGenre.isEmpty()) {
            genre = new Genre("test");
            genreRepository.save(genre);
        } else {
            genre = optionalGenre.get();
        }

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
            log.info("start creating records");
            authorRepository.saveAll(authorList.subList(0, 501));
            bookRepository.saveAll(bookList.subList(0, 501));
            log.info("500 done");
            authorRepository.saveAll(authorList.subList(501, 1001));
            bookRepository.saveAll(bookList.subList(501, 1001));
            log.info("1000 done");
            authorRepository.saveAll(authorList.subList(1001, 1501));
            bookRepository.saveAll(bookList.subList(1001, 1501));
            log.info("1500 done");
            authorRepository.saveAll(authorList.subList(1501, 2001));
            bookRepository.saveAll(bookList.subList(1501, 2001));
            log.info("2000 done");
            authorRepository.saveAll(authorList.subList(2001, 2501));
            bookRepository.saveAll(bookList.subList(2001, 2501));
            log.info("2500 done");
            authorRepository.saveAll(authorList.subList(2501, 3001));
            bookRepository.saveAll(bookList.subList(2501, 3001));
            log.info("3000 done");
        } catch(Exception ex) {
            log.error("Error on saving authors data: {}", ex.getMessage());
        }
        Map<String, String> response = new HashMap<>();
        long books = bookRepository.count();
        long authors = authorRepository.count();
        long genres = genreRepository.count();
        response.put("authors", String.valueOf(authors));
        response.put("books", String.valueOf(books));
        response.put("genres", String.valueOf(genres));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/export/authors")
    public ResponseEntity<byte[]> exportAuthors() {
        List<Author> authors = StreamSupport.stream(authorRepository.findAll().spliterator(), true)
            .collect(Collectors.toList());
        Resource r = resourceLoader.getResource("classpath:templates/authors.xlsx");
        try (InputStream is = r.getInputStream();
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Context ctx = new Context();
            ctx.putVar("authors", authors);
            JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, ctx);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File f = new File("tmp.xlsx");
        byte[] report = new byte[(int)f.length()];
        int readByteCount;
        try (InputStream is = new FileInputStream(f)) {
            readByteCount = is.read(report);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (readByteCount > 0) {
            return ResponseEntity.ok()
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(report);
        }
        throw new RuntimeException("Ошибка построения отчета");
    }

    @GetMapping(value = "/export/authorsV2")
    public ResponseEntity<byte[]> exportAuthorsV2() {
        List<Author> authors = StreamSupport.stream(authorRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
        Resource r = resourceLoader.getResource("classpath:templates/authors.xlsx");
        try (InputStream is = r.getInputStream();
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Context ctx = new Context();
            ctx.putVar("authors", authors);
            JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, ctx);
            if (os.size() > 0) {
                return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(os.toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Ошибка построения отчета");
    }
}
