package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/*
 * Sbject와 타입이 동일해야 합니다.
 * */
//@Primary
//@Service
public class ProxySimpleEventService implements EventService {

    /*
     * Proxy 같은경우 Subject Bean을 주입 받아서 사용해야 합니다.
     * */
    @Autowired
    EventService eventService;

    @Override
    public void createEvent() {
        /*
        * 기능을 위임해 줍니다.
        * 그와 동시에 시간을 측정하는 기능을 추가합니다.
        * */
        long l = System.currentTimeMillis();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        eventService.createEvent();

        System.out.println(System.currentTimeMillis() - l);
    }

    @Override
    public void publicEvent() {
        eventService.publicEvent();
    }
}
