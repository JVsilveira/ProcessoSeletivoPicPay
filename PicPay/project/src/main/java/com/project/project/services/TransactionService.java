package com.project.project.services;

import com.project.project.DTOs.TransactionDTO;
import com.project.project.domain.transaction.Transaction;
import com.project.project.domain.user.User;
import com.project.project.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {
  @Autowired
  private UserService userService;

  @Autowired
  private TransactionRepository repository;

  @Autowired
  private RestTemplate restTemplate;

  public void createTrasnsaction(TransactionDTO transaction) throws Exception {
    User sender = this.userService.findUserById(transaction.senderId());
    User reciever = this.userService.findUserById(transaction.recieverId());

    userService.validateTransaction(sender, transaction.value());

    boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());

    if (!isAuthorized) {
      throw new Exception("Transação não autorizada.");
    }

    Transaction newTransaction = new Transaction();
    newTransaction.setAmount(transaction.value());
    newTransaction.setSender(sender);
    newTransaction.setReceiver(reciever);
    newTransaction.setTimestamp(LocalDateTime.now());

    sender.setBalance(sender.getBalance().subtract(transaction.value()));
    reciever.setBalance(reciever.getBalance().add(transaction.value()));

    this.repository.save(newTransaction);
    this.userService.saveUser(sender);
    this.userService.saveUser(reciever);

  }

  public boolean authorizeTransaction(User sender, BigDecimal value) {
    ResponseEntity<Map<String, String>> authorizationResponse = restTemplate
        .exchange("https://run.mocky.io/v3/19371f09-968b-4afc-9542-7be8e19cd824", HttpMethod.GET, null,
            new ParameterizedTypeReference<Map<String, String>>() {
            });

    if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
      Map<String, String> responseBodyMap = authorizationResponse.getBody();

      if (responseBodyMap != null) {
        String message = responseBodyMap.get("message");
        return "Autorizado".equalsIgnoreCase(message);
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}