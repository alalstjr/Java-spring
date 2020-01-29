package me.whiteship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class BookService {

    //    @Autowired
    //    @Qualifier("myBookRepository")
    //    private BookRepository bookRepository;

    //    @Autowired
    //    private List<BookRepository> bookRepositorys;

    @Autowired
    private BookRepository myBookRepository;

    /*
    // 생성자를 활용하여 주입받기
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    */

    //    @Autowired(required = false)
    //    public void setBookRepository(BookRepository bookRepository) {
    //        this.bookRepository = bookRepository;
    //    }

    public void printBookRepository() {
        //        this.bookRepositorys.forEach(System.out::println);
        System.out.println(myBookRepository.getClass());
    }

    @PostConstruct
    public void setup() {
        System.out.println(myBookRepository.getClass());
    }
}
