package com.example.spring_test.Aspect.test2;

import org.springframework.stereotype.Component;

@Component
public class CalImpl implements Cal {

    @Override
    @PerformanceMonitor("[累加函数]")
    public int addRange(int a, int b) {
        int sum = 0;
        for (int i = a; i <= b; i++) {
            sum += i;
        }
        return sum;
    }
}
