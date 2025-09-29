package dev.vivekraman.enqueue.splitwise.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.vivekraman.enqueue.splitwise.config.Constants;
import dev.vivekraman.enqueue.splitwise.entity.EnqueueSplitwiseJob;
import dev.vivekraman.enqueue.splitwise.model.request.UpsertJobRequest;
import dev.vivekraman.enqueue.splitwise.service.ExpenseService;
import dev.vivekraman.enqueue.splitwise.service.JobService;
import dev.vivekraman.monolith.annotation.MonolithController;
import dev.vivekraman.monolith.model.Response;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@MonolithController(moduleName = Constants.MODULE_NAME)
@RequiredArgsConstructor
public class EnqueueController {
  private final ExpenseService expenseService;
  private final JobService jobService;
  private final Scheduler scheduler;

  @GetMapping("/job")
  @PreAuthorize(Constants.PRE_AUTHORIZATION_SPEC)
  public Mono<Response<EnqueueSplitwiseJob>> fetchJobInfo() {
    return jobService.fetchJobInfo()
    .map(Response::of)
    .defaultIfEmpty(Response.of(null))
    .subscribeOn(scheduler);
  }

  @PostMapping("/job")
  @PreAuthorize(Constants.PRE_AUTHORIZATION_SPEC)
  public Mono<Response<EnqueueSplitwiseJob>> upsertJob(@RequestBody UpsertJobRequest request) {
    return jobService.upsertJob(request)
        .map(Response::of)
        .subscribeOn(scheduler);
  }

  @PostMapping("/force-run")
  @PreAuthorize(Constants.PRE_AUTHORIZATION_SPEC)
  public Mono<Response<Boolean>> forceRun() {
    return jobService.fetchJobInfo()
        .flatMap(job -> expenseService.createBlankExpenseInGroup(
          job.getClientID(), job.getClientSecret(), job.getGroupID()))
        .map(Response::of)
        .subscribeOn(scheduler);
  }
}
