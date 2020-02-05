package com.example.demo;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/*
* thread-safe 하므로 Bean 등록이 가능합니다.
*
* 등록해서 사용하는 방법은 ConverterRegistry에 등록하여 사용해야 합니다.
* */
public class EventFormatter implements Formatter<Event> {
    @Override
    public Event parse(String text, Locale locale) throws ParseException {
        return new Event(Long.parseLong(text));
    }

    @Override
    public String print(Event object, Locale locale) {
        return object.getId().toString();
    }
}
