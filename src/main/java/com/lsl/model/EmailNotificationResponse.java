package com.lsl.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class EmailNotificationResponse {

    private UUID id;

}
