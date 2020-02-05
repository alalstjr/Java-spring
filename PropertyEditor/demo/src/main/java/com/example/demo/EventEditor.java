package com.example.demo;

import java.beans.PropertyEditorSupport;

/*
 * Text를 Event 로 변환해야합니다.
 * */
public class EventEditor extends PropertyEditorSupport {

    /*
    * getValue, setValue 는 PropertyEditor 가지고있는 값입니다.
    * 이 값이 서로다른 Thread 에서 공유가 됍니다.
    * stateful 상태입니다. 이는 thread safe 하지 않기 때문에 여러 thread 에서 공유해서 사용하면 안됍니다.
    * 즉 EventEditor.class 를 Bean으로 등록해서 사용하면 안됍니다.
    * 그럼 어떻게 사용하느냐?
    * @InitBinder 를 사용하여 사용하는 Controller 위치에 등록하는 방법이 있습니다.
    * */
    @Override
    public String getAsText() {
        Event event = (Event) getValue();
        return event.getId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        /*
        * 값이 전달되는 것은 문자열이지만 개발자는 숫자로 인식할겁니다.
        * */
        Event event = new Event(Long.parseLong(text));
        setValue(event);
    }
}
