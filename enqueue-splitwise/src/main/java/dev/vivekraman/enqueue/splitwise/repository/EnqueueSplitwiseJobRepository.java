package dev.vivekraman.enqueue.splitwise.repository;

import java.time.LocalTime;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import dev.vivekraman.enqueue.splitwise.entity.EnqueueSplitwiseJob;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EnqueueSplitwiseJobRepository extends ReactiveCrudRepository<EnqueueSplitwiseJob, String> {
  Mono<EnqueueSplitwiseJob> findByApiKey(String apiKey);

  Mono<Boolean> existsByApiKey(String apiKey);

  Flux<EnqueueSplitwiseJob> findByTimeOfDayBetween(LocalTime leftBound, LocalTime rightBound);
}
