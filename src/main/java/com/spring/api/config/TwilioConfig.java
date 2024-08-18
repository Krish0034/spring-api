package com.spring.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String trialNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    // Getters and setters
}
