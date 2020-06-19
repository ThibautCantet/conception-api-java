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
import fr.soat.training.api.superhero.services.SuperHeroService;
import fr.soat.training.api.superhero.services.domain.MatchingHistoricEvent;
import fr.soat.training.api.superhero.services.domain.MatchingMission;
import fr.soat.training.api.superhero.web.requests.CreateHistoryEventRequest;
import fr.soat.training.api.superhero.web.requests.CreateMissionRequest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

class MissionApiShould extends APIsBaseComponentTest{
    private static final String ALL_MISSIONS = "missions/";

    @MockBean
    private MissionService missionService;

    @MockBean
    private HistoricEventService historicEventService;

    @MockBean
    private SuperHeroService superHeroService;

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
    void respond_with_201_and_the_missionId_in_location_when_the_created_a_mission_with_a_known_super_hero() {
        CreateMissionRequest request = missionRequest("Save the world !", "Batman");
        UUID fakeMissionId = UUID.randomUUID();
        MatchingMission savedMission = new MatchingMission("Save the world Batman !!", "Batman", fakeMissionId);

        when(this.superHeroService.exists("Batman")).thenReturn(true);
        when(missionService.createAMissionFor(anyString(), anyString())).thenReturn(savedMission);

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
        when(missionService.missionExists(fakeMissionId.toString())).thenReturn(true);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ALL_MISSIONS + fakeMissionId.toString() + "/history-events")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(historicEventService).retrieveAllEventsOfAMission(fakeMissionId);
    }

    @Test
    void respond_with_404_status_when_the_mission_for_the_event_to_create_does_not_exists() {
        String unknownMissionId = "111123";
        Mockito.when(missionService.missionExists(unknownMissionId)).thenReturn(false);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ALL_MISSIONS + unknownMissionId + "/history-events")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

        verify(missionService).missionExists(unknownMissionId);
        verify(historicEventService, never()).retrieveAllEventsOfAMission(any(UUID.class));
    }

    @Test
    void respond_with_200_OK_and_the_matching_events_as_body_when_there_are_events_for_a_mission() {
        UUID fakeMissionId = UUID.randomUUID();

        Mission aMission = new MissionBuilder().createMission("mission !").assignedTo("ze hero").build();
        HistoricEvent firstEvent = new HistoricEventBuilder().createAction("first action").madeDuringTheMission(aMission).build();
        HistoricEvent secondEvent = new HistoricEventBuilder().createAction("another action").madeDuringTheMission(aMission).build();

        List<MatchingHistoricEvent> historicEvents = Arrays.asList(firstEvent, secondEvent).stream().map(he -> new MatchingHistoricEvent(he))
                .toList();

        Mockito.when(missionService.missionExists(fakeMissionId.toString())).thenReturn(true);
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

        verify(historicEventService).createNewEventOnMission(fakeMissionId, event.description());
    }

    @Test
    void should_return_an_error_when_creating_a_new_mission_without_a_title() {
        CreateMissionRequest request = missionRequest("", "Batman");

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(ALL_MISSIONS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void should_return_a_bad_request_status_when_creating_a_new_mission_without_the_name_of_the_assigned_hero() {
        CreateMissionRequest request = missionRequest("Saving the world!", "");

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(ALL_MISSIONS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void return_a_not_found_status_when_creating_a_mission_for_hero_that_does_not_exist() {
        CreateMissionRequest request = missionRequest("Saving the world!", "unknown");
        when(superHeroService.exists(anyString())).thenReturn(false);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(ALL_MISSIONS)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

        verify(superHeroService).exists("unknown");
        verify(missionService, never()).createAMissionFor(request.assignedHero(), request.title());
    }

    private static CreateMissionRequest missionRequest(final String title, final String hero) {
        return new CreateMissionRequest(title, hero);
    }

    private static CreateHistoryEventRequest eventRequest(final String description) {
        return new CreateHistoryEventRequest(description);
    }


}
