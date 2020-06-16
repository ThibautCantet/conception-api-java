package fr.soat.training.api.superhero.web;

import fr.soat.training.api.superhero.services.HistoricEventService;
import fr.soat.training.api.superhero.services.MissionService;
import fr.soat.training.api.superhero.services.domain.MatchingHistoricEvent;
import fr.soat.training.api.superhero.services.domain.MatchingMission;
import fr.soat.training.api.superhero.web.requests.CreateHistoryEventRequest;
import fr.soat.training.api.superhero.web.requests.CreateMissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/missions/")
public class MissionApi {

    @Autowired
    private MissionService missionService;

    @Autowired
    private HistoricEventService historicEventService;

    @GetMapping()
    public ResponseEntity<?> getMissions() {
        List<MatchingMission> allTheMissions = this.missionService.getAllTheMissions();
        return ResponseEntity.ok().body(allTheMissions);
    }

    @PostMapping()
    public ResponseEntity<?> createNewMission(@RequestBody CreateMissionRequest request){
        MatchingMission newMission = this.missionService.createAMissionFor(request.getAssignedHero(), request.getTitle());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newMission.getMissionId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<?> getMission(@PathVariable String uuid){
        MatchingMission mission = this.missionService.getMission(UUID.fromString(uuid));
        return ResponseEntity.ok(mission);
    }

    @GetMapping(value = "{uuid}/history-events")
    public ResponseEntity<?> getMissionEvents(@PathVariable String uuid){
        List<MatchingHistoricEvent> matchingHistoricEvents = historicEventService.retrieveAllEventsOfAMission(UUID.fromString(uuid));
        if (matchingHistoricEvents.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(matchingHistoricEvents);
    }

    @PostMapping(value = "{uuid}/history-events")
    public ResponseEntity<?> createANewEvent(@PathVariable String uuid, @RequestBody final CreateHistoryEventRequest request){

        this.historicEventService.createNewEventOnMission(UUID.fromString(uuid), request.getDescription());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

}
