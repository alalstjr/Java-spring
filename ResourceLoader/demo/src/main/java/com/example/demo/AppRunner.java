package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext loader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = loader.getResource("classpath:test.txt");
        System.out.println(resource.exists());
        System.out.println(resource.getDescription());
        System.out.println(Files.readString(Path.of(resource.getURI())));

        // Class Type 검증
        System.out.println(loader.getClass());   // locader는 ApplicationContext 타입이므로 ApplicationContext.class 출력되야 합니다.
        System.out.println(resource.getClass()); // resource는 classpath를 사용하였으므로 ClassPathResource.class 출력되야 합니다.
    }
}

