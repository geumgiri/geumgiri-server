package com.tta.geumgiri.test;

// test 코드 실행
/*
## beforeAll Annotation 호출 ##

## beforeEach Annotation ##

## Test 1 시작 ##

## afterEach Annotation ##


void com.tta.geumgiri.test.TestLifeCycle.test2() is @Disabled
## beforeEach Annotation ##

## Test 3 시작 ##

## afterEach Annotation ##

## afterAll Annotation ##

 */

import org.junit.jupiter.api.*;

public class TestLifeCycle {

    @BeforeAll
    static void beforeAll(){
        System.out.println("## beforeAll Annotation 호출 ##");
        System.out.println();
    }

    @AfterAll
    static void afterAll(){
        System.out.println("## afterAll Annotation ##");
        System.out.println();
    }

    @BeforeEach
    void beforeEach(){
        System.out.println("## beforeEach Annotation ##");
        System.out.println();
    }

    @AfterEach
    void afterEach(){
        System.out.println("## afterEach Annotation ##");
        System.out.println();
    }

    @Test
    @DisplayName("Test 1 case !")
    void test1(){
        System.out.println("## Test 1 시작 ##");
        System.out.println();
    }

    @Test
    @DisplayName("Test 2 case !")
    @Disabled                                                                       // test2 는 실행 안하게 설정
    void test2(){
        System.out.println("## Test 2 시작 ##");
        System.out.println();
    }

    @Test
    @DisplayName("Test 3 case !")
    void test3(){
        System.out.println("## Test 3 시작 ##");
        System.out.println();

    }
}
