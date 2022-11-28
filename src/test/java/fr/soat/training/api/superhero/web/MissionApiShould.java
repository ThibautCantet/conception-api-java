package fr.soat.training.api.superhero.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import fr.soat.training.api.superhero.domain.HistoricEvent;
import fr.soat.training.api.superhero.domain.Mission;
import fr.soat.training.api.superhero.domain.builders.HistoricEventBuilder;
import fr.soat.training.api.superhero.domain.builders.MissionBuilder;
import fr.soat.training.api.superhero.services.HistoricEventService;
import fr.soat.training.api.superhero.services.MissionService;
import fr.soat.training.api.superhero.services.domain.MatchingHistoricEvent;
import fr.soat.training.api.superhero.services.domain.MatchingMission;
import fr.soat.training.api.superhero.web.requests.CreateHistoryEventRequest;
import fr.soat.training.api.superhero.web.requests.CreateMissionRequest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class MissionApiShould extends APIsBaseComponentTest{
    private static final String ALL_MISSIONS = "missions/";

    @MockBean
    private MissionService missionService;

    @MockBean
    private HistoricEventService historicEventService;

    @Test
    void respond_with_200_OK_when_getting_all_the_missions_is_a_success() {
        MatchingMission first = new MatchingMission("Save the world Batman !!", "Batman");
        MatchingMission second = new MatchingMission("Save the X-Men Malicia", "Malicia");

        when(missionService.getAllTheMissions()).thenReturn(Arrays.asList(first, second));

        MatchingMission[] missions = this.given()
                .when()
                .get(ALL_MISSIONS).then()
                .assertThat().statusCode(OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(MatchingMission[].class);

        assertThat(missions)
                .as("should return 2 missions")
                .hasSize(2);
    }

    @Test
    void respond_with_201_and_the_missionId_in_headers_as_location_when_the_creation_is_a_success() {
        CreateMissionRequest request = missionRequest("Save the world !", "Batman");
        UUID fakeMissionId = UUID.randomUUID();
        MatchingMission savedMission = new MatchingMission("Save the world Batman !!", "Batman", fakeMissionId);

        when(missionService.createAMissionFor(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(savedMission);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(ALL_MISSIONS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .header(HttpHeaders.LOCATION, Matchers.allOf(
                        Matchers.containsString("/api/v1/missions/"),
                        Matchers.endsWith(fakeMissionId.toString())
                ));
    }

    @Test
    void respond_with_200_OK_and_the_mission_as_body_when_a_mission_requested_is_found() {
        UUID fakeMissionId = UUID.randomUUID();
        MatchingMission theMission = new MatchingMission("Save the world Batman !!", "Batman", fakeMissionId);

        when(missionService.getMatchingMission(fakeMissionId)).thenReturn(theMission);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(ALL_MISSIONS  + fakeMissionId.toString())
                .then()
                .statusCode(OK.value())
                .and()
                .body("title", equalTo(theMission.getTitle()));

        verify(missionService).getMatchingMission(fakeMissionId);
    }

    @Test
    void respond_with_204_when_there_no_events_given_a_mission() {
        UUID fakeMissionId = UUID.randomUUID();
        when(historicEventService.retrieveAllEventsOfAMission(fakeMissionId)).thenReturn(Collections.emptyList());

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ALL_MISSIONS + fakeMissionId.toString() + "/history-events")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(historicEventService).retrieveAllEventsOfAMission(fakeMissionId);
    }


    @Test
    void respond_with_200_OK_and_the_matching_events_as_body_when_there_are_events_for_a_mission() {
        UUID fakeMissionId = UUID.randomUUID();

        Mission aMission = new MissionBuilder().createMission("mission !").assignedTo("ze hero").build();
        HistoricEvent firstEvent = new HistoricEventBuilder().createAction("first action").madeDuringTheMission(aMission).build();
        HistoricEvent secondEvent = new HistoricEventBuilder().createAction("another action").madeDuringTheMission(aMission).build();

        List<MatchingHistoricEvent> historicEvents = Arrays.asList(firstEvent, secondEvent).stream().map(he -> new MatchingHistoricEvent(he))
                .toList();

        when(historicEventService.retrieveAllEventsOfAMission(fakeMissionId)).thenReturn(historicEvents);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ALL_MISSIONS + fakeMissionId.toString() + "/history-events")
                .then()
                .statusCode(OK.value())
                .body("", Matchers.hasSize(2));

        verify(historicEventService).retrieveAllEventsOfAMission(fakeMissionId);
    }

    @Test
    void respond_with_201_Status_and_all_the_events_URL_as_location_when_the_creation_of_an_historic_event_succeeds() {
        UUID fakeMissionId = UUID.randomUUID();
        CreateHistoryEventRequest event = eventRequest("jump in the batMobile");

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(event)
                .when()
                .post(ALL_MISSIONS + fakeMissionId.toString() + "/history-events")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .header(HttpHeaders.LOCATION, endsWith("/api/v1/missions/" + fakeMissionId.toString() + "/history-events") );

        verify(historicEventService).createNewEventOnMission(fakeMissionId, event.getDescription());

    }

    private static CreateMissionRequest missionRequest(final String title, final String hero) {
        final CreateMissionRequest request = new CreateMissionRequest();
        request.setTitle(title);
        request.setAssignedHero(hero);
        return request;
    }

    private static CreateHistoryEventRequest eventRequest(final String description) {
        final CreateHistoryEventRequest request = new CreateHistoryEventRequest(description);
        return request;
    }
}
