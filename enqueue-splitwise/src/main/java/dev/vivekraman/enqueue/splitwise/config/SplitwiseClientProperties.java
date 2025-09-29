package dev.vivekraman.enqueue.splitwise.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "splitwise.client")
public class SplitwiseClientProperties {

  private String consumerKey;
  private String consumerSecret;
}
