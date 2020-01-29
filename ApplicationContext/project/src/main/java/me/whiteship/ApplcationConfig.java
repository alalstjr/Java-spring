package me.whiteship;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Bean 설정파일이라는 것을 알려주는 어노테이션
@Configuration
@ComponentScan(basePackageClasses = Application.class)
public class ApplcationConfig {

    /*
     * Bean:메소드 name  : bookRepository
     * Bean:메소드의 type : BookRepository
     * Bean:메소드의 class: new BookRepository()
     * */

    /*
    @Bean
    BookRepository bookRepository() {
        return new BookRepository();
    }
    @Bean
    BookService bookService() {
        BookService bookService = new BookService();
        bookService.setBookRepository(bookRepository()); // 메소드를 호출해서 의존성 주입을 직접 해줄 수 있습니다.
        return bookService;
    }
    @Bean
    BookService bookService(BookRepository bookRepository) {
        BookService bookService = new BookService();
        bookService.setBookRepository(bookRepository);  // 메소드 파라미터로 주입을 받아서 의존성을 주입받을 수 있습니다.
        return bookService;
    }
    */
}