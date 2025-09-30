package com.example.spring_test.AnnotationConfig;

import org.springframework.stereotype.Component;

@Component
public class KeyBean {

    String key = "gcc";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
