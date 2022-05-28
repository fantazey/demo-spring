package com.example.demo;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public CommandLineRunner demo(AuthorRepository repository) {
		return (args) -> {
			repository.save(new Author("Alex", "Foo"));
			repository.save(new Author("Sam", "Bar"));
			repository.save(new Author("Rich", "Baz"));
			repository.save(new Author("John", "Bazzar"));
			log.info("Create 4 authors");
			for (Author author: repository.findAll()) {
				log.info(author.toString());
			}
			log.info("Let's find by last name Baz");
			List<Author> authors = repository.findByLastName("Baz");
			log.info("Found next authors:");
			log.info(authors.stream().map(Author::toString).collect(Collectors.joining(",")));
			Optional<Author> a = repository.findById(1L);
			a.ifPresent((author -> {
				log.info(
					String.format("[%s|%s|%s]", author.getId(), author.getFirstName(), author.getLastName())
				);
			}));
		};
	}

}
