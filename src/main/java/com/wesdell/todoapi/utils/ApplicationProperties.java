package com.wesdell.todoapi.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
public class ApplicationProperties {
    private String secretKey;
    private long expirationTime;
}
