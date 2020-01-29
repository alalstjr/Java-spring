package me.whiteship;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        // XML 주입받아 사용하는 Context
        // ApplicationContext appC                = new ClassPathXmlApplicationContext("application.xml");

        // Configuration 주입받아 사용하는 Context
        ApplicationContext appC = new AnnotationConfigApplicationContext(ApplcationConfig.class);

        // 등록된 bean의 정보 목록
        String[]           beanDefinitionNames = appC.getBeanDefinitionNames();
        System.out.println("beanDefinitionNames : " + Arrays.toString(beanDefinitionNames));

        // 등록된 Bean을 선택하여 가져올 수 있습니다.
        // bookService 클래스에 bookRepository 생성자가 주입이 되었는지 확인합니다.
        // 값이 존재하므로 me.whiteship.BookRepository@273e7444 출력합니다.
        // 만약 property 주석처리되어 실행하면 Null 을 출력하게 됩니다.
        BookService bookService = (BookService) appC.getBean("bookService");
        System.out.println("getBean : " + bookService.bookRepository);
    }
}
