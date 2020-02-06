package com.example.demo;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Service
public class EventService {

    @NonNull
    public String createEvent(@NonNull String name) {
        return name;
    }
}
