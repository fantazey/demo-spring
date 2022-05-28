package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    AuthorRepository authorRepository;
    @InjectMocks
    AuthorService authorService;

    @Test
    @DisplayName("Ищет авторов по фамилии")
    public void shouldSearchAuthorsByLastName() {
        Author a1 = new Author("test", "Sam");
        Author a2 = new Author("test-3", "Sam");
        Mockito.when(authorRepository.findByLastName(Mockito.eq("Sam"))).thenReturn(List.of(a1,a2));

        List<Author> result = authorService.findByLastName("Sam");
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(a1, result.get(0));
        Assertions.assertEquals(a2, result.get(1));
    }

    @Test
    @DisplayName("Должен генерировать исключение если автор не найден")
    public void shouldThrowExceptionIfAuthorNotFound() {
        Mockito.when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try {
           authorService.getAuthorById(4L);
           Assertions.fail();
        } catch (EntityNotFoundException ex) {
            Assertions.assertEquals("Author with id=4 not exists", ex.getMessage());
        }
    }
}
