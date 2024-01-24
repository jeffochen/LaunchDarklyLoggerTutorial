package com.lsl.model;

import lombok.Data;

@Data
public class EmailNotificationRequest {

    private String email;
    private String subject;
    private String body;

}
