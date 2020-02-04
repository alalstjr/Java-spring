package com.example.demo;

import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import java.util.Arrays;

// 스프링 부트 2.0.5 이상 버전 검증방법
@Component
public class EventRunner implements ApplicationRunner {

    @Autowired
    Validator validator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(validator);

        Event event = new Event();
        event.setLimit(-1);
        event.setEmail("asdasd");
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(event, "event");

        validator.validate(event, beanPropertyBindingResult);

        System.out.println(beanPropertyBindingResult);

        beanPropertyBindingResult.getAllErrors().forEach(e -> {
            System.out.println("=======");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });
    }
}

/*
// 스프링 부트 2.0.5 이하 버전 검증방법
@Component
public class EventRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Event event = new Event();
        EventValidator eventValidator = new EventValidator();

        // 어떤 객체를 검사할것인지 어떤 이름인지 작성
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(event, "event");

        // 검증 실시
        eventValidator.validate(event, beanPropertyBindingResult);

        // error 존재여부 확인
        System.out.println(beanPropertyBindingResult);

        beanPropertyBindingResult.getAllErrors().forEach(e -> {
            System.out.println("=======");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });
    }
}
*/
