package fr.soat.training.api.superhero.services;

import java.util.List;
import java.util.UUID;

import fr.soat.training.api.superhero.domain.HistoricEvent;
import fr.soat.training.api.superhero.domain.Mission;
import fr.soat.training.api.superhero.domain.builders.HistoricEventBuilder;
import fr.soat.training.api.superhero.repository.HistoricEventRepository;
import fr.soat.training.api.superhero.services.domain.MatchingHistoricEvent;
import org.springframework.stereotype.Service;

@Service
public class HistoricEventService {

    private final MissionService missionService;

    private final HistoricEventRepository historicEventRepository;

    public HistoricEventService(MissionService missionService, HistoricEventRepository historicEventRepository) {
        this.missionService = missionService;
        this.historicEventRepository = historicEventRepository;
    }

    public void createNewEventOnMission(UUID missionUUID, String actionDescription) {
        Mission relatedMission = this.missionService.getMission(missionUUID);
        HistoricEvent theEvent = new HistoricEventBuilder()
                .createAction(actionDescription)
                .madeDuringTheMission(relatedMission)
                .build();

        this.historicEventRepository.saveAndFlush(theEvent);
    }

    public List<MatchingHistoricEvent> retrieveAllEventsOfAMission(UUID missionId) {
        List<HistoricEvent> historicEvents = this.historicEventRepository.findAllByMission(missionId);
        return historicEvents.stream().map(MatchingHistoricEvent::new)
                .toList();

    }
}
