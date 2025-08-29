package com.projecct.vitee.service;

import com.projecct.vitee.dto.WebhookRequestDto;
import com.projecct.vitee.dto.WebhookResponseDto;
import com.projecct.vitee.dto.SolutionRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public WebhookResponseDto generateWebhook(WebhookRequestDto requestDto) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        logger.info("Sending POST request to generate webhook...");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<WebhookRequestDto> entity = new HttpEntity<>(requestDto, headers);
        ResponseEntity<WebhookResponseDto> response = restTemplate.postForEntity(url, entity, WebhookResponseDto.class);
        logger.info("Received response: {}", response.getBody());
        return response.getBody();
    }

    public void submitSolution(String webhookUrl, String accessToken, String finalQuery) {
        logger.info("Submitting solution to webhook: {}", webhookUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        SolutionRequestDto solutionRequest = new SolutionRequestDto();
        solutionRequest.setFinalQuery(finalQuery);
        HttpEntity<SolutionRequestDto> entity = new HttpEntity<>(solutionRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, entity, String.class);
        logger.info("Webhook response: {}", response.getBody());
    }
}
