package com.example.demo;

import org.springframework.stereotype.Service;

/*
 * Real Subject
 * */
@Service
public class SimpleEventService implements EventService {
    @PerLogging
    @Override
    public void createEvent() {
        //        long l = System.currentTimeMillis();
        //
        //        try {
        //            Thread.sleep(1000);
        //        }
        //        catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        System.out.println("createEvent");
        //System.out.println(System.currentTimeMillis() - l);
    }

    @Override
    public void publicEvent() {
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("publicEvent");
    }
}
