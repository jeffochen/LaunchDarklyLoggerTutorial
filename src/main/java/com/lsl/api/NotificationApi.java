package com.lsl.api;

import com.lsl.model.EmailNotificationRequest;
import com.lsl.model.EmailNotificationResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/notify")
public class NotificationApi {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/email",
            produces = { "application/json" }
    )
    ResponseEntity<EmailNotificationResponse> notifyByEmail(@RequestBody EmailNotificationRequest request) {
        log.debug("Sending email to {}", request.getEmail());
        var res = EmailNotificationResponse.builder().id(UUID.randomUUID()).build();
        return ResponseEntity.accepted().body(res);
    }

}
