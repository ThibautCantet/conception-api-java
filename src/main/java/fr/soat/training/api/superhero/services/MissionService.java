package fr.soat.training.api.superhero.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.soat.training.api.superhero.domain.Mission;
import fr.soat.training.api.superhero.domain.SuperHero;
import fr.soat.training.api.superhero.domain.builders.MissionBuilder;
import fr.soat.training.api.superhero.repository.MissionRepository;
import fr.soat.training.api.superhero.services.domain.MatchingMission;
import org.springframework.stereotype.Service;

@Service
public class MissionService {

    private final MissionRepository missionRepository;

    private final SuperHeroService superHeroService;

    public MissionService(MissionRepository missionRepository, SuperHeroService superHeroService) {
        this.missionRepository = missionRepository;
        this.superHeroService = superHeroService;
    }

    public List<MatchingMission> getAllTheMissions() {

        List<Mission> missions = this.missionRepository.findAll();

        return missions.stream()
                .map(MatchingMission::new)
                .toList();
    }

    public MatchingMission createAMissionFor(String superHeroName, String missionTitle) {
        SuperHero matchingHero = this.superHeroService.getTheSuperHero(superHeroName);
        Mission theMission = new MissionBuilder().createMission(missionTitle).assignedTo(matchingHero)
                .build();

        Mission mission = this.missionRepository.saveAndFlush(theMission);

        return new MatchingMission(mission);
    }

    public MatchingMission getMatchingMission(UUID missionUUID) {
        Optional<Mission> mission = this.missionRepository.findById(missionUUID);
        return mission.map(MatchingMission::new).orElse(null);
    }

    public Mission getMission(UUID missionUUID) {
        return this.missionRepository.findById(missionUUID).orElse(null);
    }
}
