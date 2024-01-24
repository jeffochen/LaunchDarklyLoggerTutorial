package com.launchdarkly.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.interfaces.FlagValueChangeEvent;
import com.launchdarkly.sdk.server.interfaces.FlagValueChangeListener;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
@AllArgsConstructor
public class FeatureToggleService implements FlagValueChangeListener {

    private LDClient ldClient;

    private static final String FLAG_LOGGING_LEVEL = "qbr.portal.logging.level";

    @PostConstruct
    private void bindListener()  {
        ldClient.getFlagTracker().addFlagValueChangeListener(
                FLAG_LOGGING_LEVEL,
                LDContext.builder("ctx-portal-log.level").anonymous(true).build(),
                this);
    }

    @Override
    public void onFlagValueChange(FlagValueChangeEvent event) {
        var flagKey = event.getKey();
        var flagValue = event.getNewValue();
        log.info("{} changed from {} to {}", flagKey, event.getOldValue(), flagValue);

        try {
            var mapper = new ObjectMapper();
            Map<String, String> newSettings = mapper.readValue(event.getNewValue().toJsonString(), Map.class);

            for (Map.Entry<String, String> setting : newSettings.entrySet()) {
                var logName = setting.getKey();
                var logLevel = Level.toLevel(setting.getValue());
                var isRootLogger = logName.equals("root");

                if (isRootLogger) {
                    Configurator.setRootLevel(logLevel);
                } else {
                    Configurator.setLevel(logName, logLevel);
                }
                log.info("Setting {} to {}", logName, logLevel);
            }
        } catch (Exception e) {
            log.error("Fail to process new setting [{}: {}]", flagKey, flagValue, e);
        }
    }


}
