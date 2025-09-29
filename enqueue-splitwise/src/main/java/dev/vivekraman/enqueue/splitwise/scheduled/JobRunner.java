package dev.vivekraman.enqueue.splitwise.scheduled;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.vivekraman.enqueue.splitwise.entity.EnqueueSplitwiseJob;
import dev.vivekraman.enqueue.splitwise.repository.EnqueueSplitwiseJobRepository;
import dev.vivekraman.enqueue.splitwise.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@Service
@RequiredArgsConstructor
public class JobRunner {
  private final EnqueueSplitwiseJobRepository repository;
  private final ExpenseService expenseService;
  private final Scheduler scheduler;

  @Scheduled(fixedDelay = 1L, timeUnit = TimeUnit.HOURS)
  public void runJob() {
    LocalTime now = LocalTime.now();
    List<EnqueueSplitwiseJob> jobs = repository.findByTimeOfDayBetween(
        now.minusHours(1), now).collectList().block();
    if (jobs == null || jobs.isEmpty()) {
      return;
    }

    Flux.fromIterable(jobs)
        .flatMap(job -> expenseService.createBlankExpenseInGroup(
            job.getClientID(), job.getClientSecret(), job.getGroupID()))
        .subscribeOn(scheduler)
        .subscribe();
  }
}
