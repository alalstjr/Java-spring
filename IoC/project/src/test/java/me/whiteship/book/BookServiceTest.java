package me.whiteship.book;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookServiceTest {

    @Test
    public void test() {
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository);
    }
}