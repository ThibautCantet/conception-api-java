package fr.soat.training.api.superhero.web.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public record HistoryEventResponse(@JsonIgnore String uuid, String description,
                                   @JsonProperty(value = "date") LocalDateTime creationDate) {
}
