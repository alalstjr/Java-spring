package com.example.demo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class EventConverter {
    /*
    * 상태정보가 없기 때문에 thread-safe 하므로 Bean 등록이 가능합니다.
    *
    * 등록해서 사용하는 방법은 ConverterRegistry에 등록하여 사용해야 합니다.
    * */
    @Component
    public static class StringToEventConverter implements Converter<String, Event> {
        @Override
        public Event convert(String source) {
            return new Event(Long.parseLong(source));
        }
    }

    //@Component
    public static class EventToStringConverter implements Converter<Event, String> {
        @Override
        public String convert(Event source) {
            return source.getId().toString();
        }
    }
}
