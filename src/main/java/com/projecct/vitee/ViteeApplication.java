package com.projecct.vitee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projecct.vitee.dto.WebhookRequestDto;
import com.projecct.vitee.dto.WebhookResponseDto;
import com.projecct.vitee.service.WebhookService;

@SpringBootApplication
public class ViteeApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ViteeApplication.class);

    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(ViteeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Application started. Beginning workflow...");
        WebhookRequestDto requestDto = new WebhookRequestDto();
        requestDto.setName("Kakumanu Uday Kiran");
        requestDto.setRegNo("22BCE7635");
        requestDto.setEmail("uday.22bce7635@vitapstudent.ac.in");

        WebhookResponseDto responseDto = webhookService.generateWebhook(requestDto);
        String webhookUrl = responseDto.getWebhook();
        String accessToken = responseDto.getAccessToken();

        String regNo = requestDto.getRegNo();
        int lastTwoDigits = Integer.parseInt(regNo.substring(regNo.length() - 2));
        String finalQuery;
        if (lastTwoDigits % 2 == 0) {
          
            finalQuery = "SELECT \n" + //
                                "    e1.EMP_ID,\n" + //
                                "    e1.FIRST_NAME,\n" + //
                                "    e1.LAST_NAME,\n" + //
                                "    d.DEPARTMENT_NAME,\n" + //
                                "    COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT\n" + //
                                "FROM EMPLOYEE e1\n" + //
                                "JOIN DEPARTMENT d \n" + //
                                "    ON e1.DEPARTMENT = d.DEPARTMENT_ID\n" + //
                                "LEFT JOIN EMPLOYEE e2\n" + //
                                "    ON e1.DEPARTMENT = e2.DEPARTMENT\n" + //
                                "   AND e2.DOB > e1.DOB   -- younger employees\n" + //
                                "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME\n" + //
                                "ORDER BY e1.EMP_ID DESC;";
        } else {
            // Odd: SQL Question 1
            finalQuery = "SELECT \n" + //
                                "    p.AMOUNT AS SALARY,\n" + //
                                "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,\n" + //
                                "    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,\n" + //
                                "    d.DEPARTMENT_NAME\n" + //
                                "FROM PAYMENTS p\n" + //
                                "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID\n" + //
                                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID\n" + //
                                "WHERE DAY(p.PAYMENT_TIME) <> 1\n" + //
                                "ORDER BY p.AMOUNT DESC\n" + //
                                "LIMIT 1;";
        }
        logger.info("Final SQL Query: {}", finalQuery);
        webhookService.submitSolution(webhookUrl, accessToken, finalQuery);
        logger.info("Workflow completed.");
    }
}

// ...existing code...
