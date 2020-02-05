package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /*
    * StringToEventConverter 등록합니다.
    * */
/*    @Override
    public void addFormatters(FormatterRegistry registry) {
        // EventConverter
        // registry.addConverter(new EventConverter.StringToEventConverter());

        // EventFormatter
        registry.addFormatter(new EventFormatter());
    }*/
}
