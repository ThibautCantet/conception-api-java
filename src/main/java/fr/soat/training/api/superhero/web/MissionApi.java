package fr.soat.training.api.superhero.web;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import fr.soat.training.api.superhero.services.HistoricEventService;
import fr.soat.training.api.superhero.services.MissionService;
import fr.soat.training.api.superhero.services.SuperHeroService;
import fr.soat.training.api.superhero.services.domain.MatchingHistoricEvent;
import fr.soat.training.api.superhero.services.domain.MatchingMission;
import fr.soat.training.api.superhero.web.requests.CreateHistoryEventRequest;
import fr.soat.training.api.superhero.web.requests.CreateMissionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/missions/")
public class MissionApi {

    private final MissionService missionService;
    private final HistoricEventService historicEventService;
    private final SuperHeroService superHeroService;

    public MissionApi(MissionService missionService, HistoricEventService historicEventService, SuperHeroService superHeroService) {
        this.missionService = missionService;
        this.historicEventService = historicEventService;
        this.superHeroService = superHeroService;
    }

    @GetMapping()
    public ResponseEntity<List<MatchingMission>> getMissions() {
        List<MatchingMission> allTheMissions = this.missionService.getAllTheMissions();
        return ResponseEntity.ok().body(allTheMissions);
    }

    @PostMapping()
    public ResponseEntity<String> createNewMission(@RequestBody @Valid CreateMissionRequest request){
        if (!this.superHeroService.exists(request.assignedHero())){
            return new ResponseEntity<>(request.assignedHero(), HttpStatus.NOT_FOUND);
        }
        MatchingMission newMission = this.missionService.createAMissionFor(request.assignedHero(), request.title());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newMission.getMissionId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<MatchingMission> getMission(@PathVariable String uuid){
        MatchingMission mission = this.missionService.getMatchingMission(UUID.fromString(uuid));
        return ResponseEntity.ok(mission);
    }

    @GetMapping(value = "{uuid}/history-events")
    public ResponseEntity<List<MatchingHistoricEvent>> getMissionEvents(@PathVariable String uuid){
        if( !this.missionService.missionExists(uuid)){
            return ResponseEntity.notFound().build();
        }
        List<MatchingHistoricEvent> matchingHistoricEvents = historicEventService.retrieveAllEventsOfAMission(UUID.fromString(uuid));
        if (matchingHistoricEvents.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(matchingHistoricEvents);
    }

    @PostMapping(value = "{uuid}/history-events")
    public ResponseEntity<URI> createANewEvent(@PathVariable String uuid, @RequestBody final CreateHistoryEventRequest request){

        this.historicEventService.createNewEventOnMission(UUID.fromString(uuid), request.description());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

}
