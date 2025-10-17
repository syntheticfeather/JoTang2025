package com.example.spring_test.AnnotationConfig.Component;

import org.springframework.stereotype.Component;

@Component
public class MeddleComponent2 implements MeddleComponent {

    @Override
    public void doSomething() {
        System.out.println("Component2 is doing something");
    }
}
