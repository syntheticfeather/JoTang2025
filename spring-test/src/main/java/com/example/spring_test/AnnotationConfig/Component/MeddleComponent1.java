package com.example.spring_test.AnnotationConfig.Component;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MeddleComponent1 implements MeddleComponent {

    @Override
    public void doSomething() {
        System.out.println("Component1 is doing something");
    }
}
