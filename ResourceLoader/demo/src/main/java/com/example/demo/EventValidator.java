package com.example.demo;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EventValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        // 검증하는 타입의 class가 동일한지 확인합니다.
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // errorCode는 ApplicationContext에서 상속받는 MessageSource 기능을 사용해서 key값에 해당하는 에러 코드를 가져옵니다.
        // defalutmessage 매개변수는 errorCode 메세지에서 key값을 못찾았을때 출력하는 문자열
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "not empty", "Empty Masseage!!");
    }
}
