package fr.soat.training.api.superhero.domain.builders;

import fr.soat.training.api.superhero.domain.Mission;
import fr.soat.training.api.superhero.domain.SuperHero;
import fr.soat.training.api.superhero.services.domain.MatchingHero;

import java.util.UUID;

public class MissionBuilder {
    private Mission mission;

    public MissionBuilder createMission(String missionName) {
        this.mission = new Mission(missionName);
        return this;
    }

    public MissionBuilder assignedTo(SuperHero superman) {
        if (this.mission == null) {
            throw new IllegalArgumentException("the mission is mandatory");
        }
        this.mission.setAssignedHero(superman);

        return this;
    }

    public MissionBuilder assignedTo(String heroName) {
        if (this.mission == null) {
            throw new IllegalArgumentException("the mission is mandatory");
        }
        SuperHero hero = new SuperHero(heroName);
        this.mission.setAssignedHero(hero);

        return this;
    }

    public Mission build() {
        if (this.mission == null) {
            throw new IllegalArgumentException("the mission is mandatory");
        }

        if (this.mission.getAssignedHero() == null) {
            throw new IllegalArgumentException(" the hero is mandatory");
        }
        return this.mission;
    }

    public MissionBuilder withUUID(UUID missionId) {
        if (this.mission == null) {
            throw new IllegalArgumentException("the mission is mandatory");
        }
        this.mission.setMissionId(missionId);
        return this;
    }
}
