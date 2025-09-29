package dev.vivekraman.enqueue.splitwise.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.vivekraman.*")
public class BackendModuleEnqueueSplitwiseApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendModuleEnqueueSplitwiseApplication.class, args);
  }
}
