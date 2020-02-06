package com.example.demo;

import java.lang.annotation.*;

/*
 * 에노테이션에서 RetentionPolicy 란 에노테이션 정보를 얼마나 유지할 것인가
 * RetentionPolicy.CLASS 인 경우 class 까지만 유지하겠다는 설정
 * 즉 컴파일 바이트코드 안에도 에노테이션이 존재한다는 뜻입니다.
 * 기본값은 CLASS 입니다. 고로 @Retention 생략 가능합니다.
 * 만약 RetentionPolicy.SOURCE 인경우 컴파일 이후 사라집니다.
 *
 * @Target 메소드라는 것을 선언해줍니다.
 * */
@Retention(RetentionPolicy.CLASS) // 생략 가능
@Target(ElementType.METHOD)
@Documented
public @interface PerLogging {
}
