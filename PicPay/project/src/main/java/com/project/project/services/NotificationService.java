package com.project.project.services;

import com.project.project.DTOs.NotificationDTO;
import com.project.project.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
  @Autowired
  private RestTemplate restTemplate;

  public void sendNotification(User user, String message) throws Exception {
    String email = user.getEmail();

    NotificationDTO notificationRequest = new NotificationDTO(email, message);

    ResponseEntity<String> notificationResponse = restTemplate.postForEntity(
        "https://run.mocky.io/v3/76136e85-2b6a-4cab-916a-6c5b26db8580", notificationRequest, String.class);

    if (!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
      System.out.println("Erro ao enviar notificação.");
      throw new Exception("Serviço de notificação está fora do ar");
    }
  }
}
