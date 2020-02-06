package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    // 표현식 사용방법
    @Value("#{1 + 1}")
    int number;

    @Value("#{'hello' + 'hi~!'}")
    String string;

    @Value("#{1 eq 1}")
    boolean trueOrFalse;

    // property 잠조방법
    @Value("${my.value}")
    String myValue;

    // property 잠조와 동시에 표현식을 사용 가능합니다.
    @Value("#{${my.value} + 10}")
    String myValue2;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("======");
        System.out.println(number);
        System.out.println(string);
        System.out.println(trueOrFalse);
        System.out.println(myValue);
        System.out.println(myValue2);
        System.out.println("======");
    }
}
