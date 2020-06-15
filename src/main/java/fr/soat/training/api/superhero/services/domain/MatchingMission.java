package fr.soat.training.api.superhero.services.domain;

import fr.soat.training.api.superhero.domain.Mission;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class MatchingMission {
    private UUID missionId;
    private String title;
    private String assignedHeroName;
    private LocalDate createdAt;

    public MatchingMission() {
    }

    public MatchingMission(Mission mission) {
        this(mission.getTitle(), mission.getHeroName(), mission.getCreateAt());
        this.missionId = mission.getUUID();


    }

    public MatchingMission(String title, String assignedHeroName, LocalDate createdAt) {
        this(title, assignedHeroName);
        this.createdAt = createdAt;
    }

    public MatchingMission(String title, String assignedHeroName) {
        this.title = title;
        this.assignedHeroName = assignedHeroName;
        this.createdAt = LocalDate.now();
    }

    public MatchingMission(String title, String assignedHeroName, UUID missiondId) {
        this(title, assignedHeroName);
        this.missionId = missiondId;
    }

    public String getTitle() {
        return title;
    }

    public String getAssignedHeroName() {
        return assignedHeroName;
    }

    public UUID getMissionId() {
        return missionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchingMission that = (MatchingMission) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(assignedHeroName, that.assignedHeroName) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, assignedHeroName, createdAt);
    }
}
