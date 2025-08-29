package com.projecct.vitee.dto;

import lombok.Data;

@Data
public class WebhookResponseDto {
    private String webhook;
    private String accessToken;
}
