package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/*
 * Client
 * */
@Component
public class AppRunner implements ApplicationRunner {

    /*
     * Interface 형식으로 주입받는것을 권장합니다.
     * 일단 인터페이스 타입을 쓰면 클래스 타입을 쓸 때 보다 프록시를 만들 때 제약이 별로 없거든요.
     * 클래스 같은 경우엔 final class거나 생성자가 private라거나 그런 경우에 프록시를 만들지 못하는 경우도 있어요.
     * 또한 인터페이스가 있는데 굳이 클래스 타입을 쓰면 프록시 주입이 제대로 안되는 경우가 발생할 수도 있습니다.
     */
    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eventService.createEvent();
        eventService.publicEvent();
    }
}
