package org.example.spring_data_jdbc;

import org.example.spring_data_jdbc.controller.BookController;
import org.example.spring_data_jdbc.model.Book;
import org.example.spring_data_jdbc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book sampleBook;

    @BeforeEach
    public void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("Sample Book");
        sampleBook.setAuthor("Sample Author");
        sampleBook.setPublicationYear(2023);
    }

    @Test
    public void testCreateBook() {
        when(bookService.createBook(any(Book.class))).thenReturn(sampleBook);

        ResponseEntity<Book> response = bookController.createBook(sampleBook);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    public void testGetBookById() {
        when(bookService.getBookById(eq(1L))).thenReturn(Optional.of(sampleBook));

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookService.getBookById(eq(1L))).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = Arrays.asList(sampleBook,
                new Book(2L, "Another Book", "Another Author", 2022));
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    public void testUpdateBook() {
        Book updatedBook = new Book(1L, "Updated Book", "Updated Author", 2024);
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        ResponseEntity<Book> response = bookController.updateBook(1L, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook, response.getBody());
    }

    @Test
    public void testDeleteBook() {
        doNothing().when(bookService).deleteBook(eq(1L));

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService).deleteBook(eq(1L));
    }
}