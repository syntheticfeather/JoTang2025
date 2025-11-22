package com.example.spring_junit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class SimpleTest {

    @Test
    public void testAdd() {
        int result = 2 + 3;
        System.out.println("2 + 3 = " + result);
        assertEquals(5, result, "2 + 3 should equal 5");
        log.info("testAdd() executed successfully");
    }
}
