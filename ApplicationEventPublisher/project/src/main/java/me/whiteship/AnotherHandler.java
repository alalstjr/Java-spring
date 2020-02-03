package me.whiteship;

import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AnotherHandler {

    @EventListener
    @Async
    public void hendler(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("AnotherHandler : " + myEvent.getData());
    }
}
