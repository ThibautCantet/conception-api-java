package fr.soat.training.api.superhero.web.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class HistoryEventResponse {

    @JsonIgnore
    private final String uuid;

    private final String description;

    @JsonProperty(value = "date")
    private final LocalDateTime creationDate;

    public HistoryEventResponse(final String uuid, final String description, final LocalDateTime creationDate) {
        this.uuid = uuid;
        this.description = description;
        this.creationDate = creationDate;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final HistoryEventResponse that = (HistoryEventResponse) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(description, that.description) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, description, creationDate);
    }
}
