package me.whiteship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    MessageSource messageSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String hello = messageSource.getMessage(
                "hello",
                new String[]{ "jjunpro" },
                Locale.ENGLISH
        );
        String helloDefault = messageSource.getMessage(
                "hello",
                new String[]{ "jjunpro" },
                Locale.getDefault()
        );

        // 1초마다 출력을 합니다.
        while (true) {
            System.out.println(hello);
            System.out.println(helloDefault);
            Thread.sleep(2000l);
        }
    }
}
