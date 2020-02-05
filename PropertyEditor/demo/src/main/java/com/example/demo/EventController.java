package com.example.demo;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    /*
    * @InitBinder 를 사용하여 EventEditor를 Controller 위치에 등록
    * Controller가 어떤 요청을 처리하기 전에 WebDataBinder 내부에 들어있는 PropertyEditor 정보를 사용하게 됩니다.
    * 문자열로 들어온 {event} 값을 원하는 값의 형으로 변환 후 Event 객체로 바꾸는 작업을 합니다.
    * */
    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Event.class, new EventEditor());
    }

    /*
    * {event} 입력을 1, 2, 3, 4, ... int 형으로 event의 id를 입력합니다.
    * 입력한 숫자를 Event 타입으로 변환을 해서 Spring에서 받습니다.
    * */
    @GetMapping("/event/{event}")
    public String getEvent(@PathVariable Event event) {
        System.out.println(event.getId());
        return event.getId().toString();
    }
}
