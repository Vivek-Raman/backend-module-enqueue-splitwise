package dev.vivekraman.enqueue.splitwise.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import dev.vivekraman.enqueue.splitwise.client.SplitwiseClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseService {
  
  public Mono<Boolean> createBlankExpenseInGroup(String clientID, String clientSecret, String groupID) {
    try {

      SplitwiseClient splitwiseClient = new SplitwiseClient(clientID, clientSecret);
      // Obtain OAuth2 access token using client credentials
      splitwiseClient.requestAccessToken();

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
