package me.whiteship.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BookService {
    public BookService() {
    }

    @Autowired
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("==========");
        System.out.println("Hello~!");
        System.out.println("==========");
    }
}
