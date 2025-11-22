// UserService.java
package com.example.spring_junit;

import org.springframework.web.client.RestTemplate;

public class UserService {

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUserDetails(String userId) {
        String userDetailsUrl = "https://api.example.com/users/" + userId;
        return restTemplate.getForObject(userDetailsUrl, String.class);
    }
}
