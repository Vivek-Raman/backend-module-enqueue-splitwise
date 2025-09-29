package dev.vivekraman.enqueue.splitwise.service;

import org.springframework.stereotype.Service;

import dev.vivekraman.enqueue.splitwise.entity.EnqueueSplitwiseJob;
import dev.vivekraman.enqueue.splitwise.model.request.UpsertJobRequest;
import dev.vivekraman.enqueue.splitwise.repository.EnqueueSplitwiseJobRepository;
import dev.vivekraman.monolith.security.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class JobService {
  private final EnqueueSplitwiseJobRepository repository;

  public Mono<EnqueueSplitwiseJob> fetchJobInfo() {
    return AuthUtils.fetchApiKey()
        .flatMap(repository::findByApiKey);
  }

  public Mono<EnqueueSplitwiseJob> upsertJob(UpsertJobRequest request) {
    return AuthUtils.fetchApiKey()
        .flatMap(apiKey -> repository.findByApiKey(apiKey).defaultIfEmpty(
            EnqueueSplitwiseJob.builder()
                .apiKey(apiKey)
                .build()))
        .flatMap((toUpsert) -> {
          toUpsert.setClientID(request.getClientID());
          toUpsert.setClientSecret(request.getClientSecret());
          toUpsert.setGroupID(request.getGroupID());
          toUpsert.setTimeOfDay(request.getTimeOfDay());
          toUpsert.setMaxCount(request.getMaxCount());
          return repository.save(toUpsert);
        });
  }
}
