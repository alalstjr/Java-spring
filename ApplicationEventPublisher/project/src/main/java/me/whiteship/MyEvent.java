package me.whiteship;

import org.springframework.context.ApplicationEvent;

// Bean으로 등록되지 않는 이벤트입니다.

// spring 4.2 이전 버전에는 ApplicationEvent 상속이 필수였습니다.
// public class MyEvent extends ApplicationEvent {

public class MyEvent {
    private Object source;
    private int    data;

    /*
    spring 4.2 이전 버전
    public MyEvent(Object source) {
        super(source);
    }

    public MyEvent(Object source, int data) {
        super(source);
        this.data = data;
    }
    */

    public MyEvent(Object source, int data) {
        this.source = source;
        this.data = data;
    }

    public Object getSource() {
        return source;
    }

    public int getData() {
        return data;
    }
}
