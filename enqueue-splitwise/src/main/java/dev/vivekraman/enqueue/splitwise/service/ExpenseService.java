package dev.vivekraman.enqueue.splitwise.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dev.vivekraman.enqueue.splitwise.client.SplitwiseClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseService {
  
  public Mono<Boolean> createBlankExpenseInGroup(String clientID, String clientSecret, String groupID) {
    WebClient webClient = WebClient.create();
    try {

      SplitwiseClient splitwiseClient = new SplitwiseClient(clientID, clientSecret);
      String authURL = splitwiseClient.getAuthorizationUrl();
      // webClient.get()
      //   .uri(authURL)
      //   .retrieve()
      //   .bodyToMono(String.class)
      //   .block();

      splitwiseClient.createExpense(Map.of(
          "group_id", groupID,
          "description", "Blank Expense",
          "cost", "0.01",
          "split_equally", "true"));
      return Mono.just(true);
    } catch (Exception e) {
      return Mono.error(e);
    }
  }
}
