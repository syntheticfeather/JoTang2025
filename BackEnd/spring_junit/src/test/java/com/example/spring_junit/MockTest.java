// MockTest.java
package com.example.spring_junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MockTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserDetails() {
        String userId = "1";
        String mockResponse = "User Details for ID: 1";

        // 设置Mockito的行为
        when(restTemplate.getForObject("https://api.example.com/users/" + userId, String.class))
                .thenReturn(mockResponse);

        // 执行测试
        String result = userService.getUserDetails(userId);

        // 验证结果
        assertEquals(mockResponse, result);
    }
}
