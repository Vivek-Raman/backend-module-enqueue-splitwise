package dev.vivekraman.enqueue.splitwise.entity;

import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="enqueue_splitwise_job")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnqueueSplitwiseJob {
  @Id
  private String id;
  private String apiKey;
  private String clientID;
  private String clientSecret;
  private String groupID;
  private LocalTime timeOfDay;
  private Integer maxCount;
}
