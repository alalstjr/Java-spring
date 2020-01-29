package me.whiteship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BookServiceRunner implements ApplicationRunner {

    @Autowired
    BookService bookService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        bookService.printBookRepository();
        AutowiredAnnotationBeanPostProcessor beanPostProcessor = applicationContext.getBean(AutowiredAnnotationBeanPostProcessor.class);
        System.out.println(beanPostProcessor);
    }
}
