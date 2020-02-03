package me.whiteship;

import org.springframework.context.ApplicationListener;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
// spring 4.2 이전 버전에는 ApplicationListener 상속이 필수였습니다.
// public class MyEventHandler implements ApplicationListener<MyEvent> {
public class MyEventHandler {
    @EventListener
    // @Order(Ordered.HIGHEST_PRECEDENCE + 2) 실행 순서를 선정해줍니다.
    @Async
    public void onApplicationEvent(MyEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("MyEventHandler : " + event.getData());
    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextRefreshedEvent refreshedEvent) {
        // 해당 이벤트는 ApplicationContext 상속하고있어서 ApplicationContext 꺼내서 사용할 수 있습니다.
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextRefreshedEvent : " + refreshedEvent.getSource());
    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextClosedEvent closedEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextClosedEvent : " + closedEvent.getSource());
    }
}

