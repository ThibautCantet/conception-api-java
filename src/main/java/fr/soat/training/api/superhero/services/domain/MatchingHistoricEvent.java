package fr.soat.training.api.superhero.services.domain;

import fr.soat.training.api.superhero.domain.HistoricEvent;
import fr.soat.training.api.superhero.domain.Mission;

import java.time.LocalDateTime;
import java.util.Objects;

public class MatchingHistoricEvent {
    private Mission mission;
    private String description;
    private LocalDateTime createdAt;

    public MatchingHistoricEvent() { }

    public MatchingHistoricEvent(HistoricEvent event) {
        this(event.getMission(), event.getDescription(), event.getCreatedAt());
    }

    public MatchingHistoricEvent(Mission mission, String description, LocalDateTime createdAt) {
        this.mission = mission;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Mission getMission() {
        return mission;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchingHistoricEvent that = (MatchingHistoricEvent) o;
        return Objects.equals(mission, that.mission) &&
                Objects.equals(description, that.description) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mission, description, createdAt);
    }
}
