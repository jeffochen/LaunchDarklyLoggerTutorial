package com.launchdarkly.config;

import com.launchdarkly.sdk.server.Components;
import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class LaunchDarklyConfig {

    private LDClient launchdarklyClient;

    @Bean
    public LDClient ldClient(
            @Value("${launchdarkly.api-key}") String apiKey,
            @Value("#{systemProperties['http.proxyUser']}") String proxyUser,
            @Value("#{systemProperties['http.proxyPassword']}") String proxyPassword,
            @Value("#{systemProperties['http.proxyHost']}") String proxyHost,
            @Value("#{systemProperties['http.proxyPort'] ?: 0}") int proxyPort
    ) {
        if (proxyHost != null) {
            LDConfig config = new LDConfig.Builder()
                    .http(Components.httpConfiguration()
                            .proxyHostAndPort(proxyHost, proxyPort)
                            .proxyAuth(Components.httpBasicAuthentication(proxyUser, proxyPassword)))
                    .build();
            this.launchdarklyClient = new LDClient(apiKey, config);
        } else {
            this.launchdarklyClient = new LDClient(apiKey);
        }
        return launchdarklyClient;
    }

    @PreDestroy
    public void destroy() throws IOException {
        this.launchdarklyClient.close();
    }
}
