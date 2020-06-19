package fr.soat.training.api.superhero.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import fr.soat.training.api.superhero.domain.SuperHero;
import fr.soat.training.api.superhero.domain.builders.SuperHeroBuilder;
import fr.soat.training.api.superhero.services.SuperHeroService;
import fr.soat.training.api.superhero.services.domain.MatchingHero;
import fr.soat.training.api.superhero.web.requests.CreateSuperHeroRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class SuperHeroApiShould extends APIsBaseComponentTest {

    private static final String ALL_SUPER_HEROES = "/super-heroes/";

    @MockBean
    private SuperHeroService superHeroService;

    @Test
    void respond_with_a_200_status_when_getting_all_the_super_heroes_endpoint_exists() {
        SuperHero batman = new SuperHeroBuilder().createSuperHero("Batman");
        SuperHero malicia = new SuperHeroBuilder().createSuperHero("Malicia");
        List<MatchingHero> heroesFound = Arrays.asList(batman, malicia).stream().map(sh -> new MatchingHero(sh))
                .toList();

        when(superHeroService.findAllTheMissions()).thenReturn(heroesFound);

        this.given()
                .when()
                .get("/super-heroes/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("", Matchers.hasSize(2));

        verify(superHeroService).findAllTheMissions();
    }

    @Test
    void respond_with_a_204_status_when_getting_all_the_super_heroes_returns_nothing() {
        when(superHeroService.findAllTheMissions()).thenReturn(Collections.emptyList());

        this.given()
                .when()
                .get(ALL_SUPER_HEROES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    void respond_with_a_201_status_when_a_new_super_hero_is_created() {
        CreateSuperHeroRequest newHero = new CreateSuperHeroRequest("Api hero");
        UUID fakeHeroId = UUID.randomUUID();
        MatchingHero hero = new MatchingHero("Api hero", fakeHeroId);
        when(superHeroService.createSuperHero(anyString())).thenReturn(hero);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(newHero)
                .when()
                .post(ALL_SUPER_HEROES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .header(HttpHeaders.LOCATION, Matchers.endsWith("/api/v1/super-heroes/" + fakeHeroId));

        verify(superHeroService).createSuperHero(newHero.name());
    }

    @Test
    void respond_with_a_200_status_when_a_hero_matches_the_given_uuid() {
        UUID fakeHeroId = UUID.randomUUID();
        MatchingHero hero = new MatchingHero("API hero", fakeHeroId);
        GetSuperHeroById getHeroWithoutIDRequest = new GetSuperHeroById(fakeHeroId.toString());
        when(superHeroService.getSuperHero(fakeHeroId)).thenReturn(hero);

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ALL_SUPER_HEROES + getHeroWithoutIDRequest.getUuid())
                .then()
                .log().ifValidationFails()
                .statusCode(OK.value())
                .and()
                .body("name", equalTo(hero.getName()));

        verify(superHeroService).getSuperHero(fakeHeroId);
    }

    @Test
    void respond_with_a_400_status_when_the_hero_to_name_has_empty_name() {
        CreateSuperHeroRequest heroWithoutNameRequest = new CreateSuperHeroRequest("");
        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(heroWithoutNameRequest)
                .when()
                .post(ALL_SUPER_HEROES)
                .then()
                .assertThat()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void respons_with_a_400_status_when_the_hero_to_create_already_exist() {
        when(this.superHeroService.isSuperHeroAlreadyExist(anyString())).thenReturn(true);
        CreateSuperHeroRequest newHero = new CreateSuperHeroRequest("Api hero");

        this.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(newHero)
                .when()
                .post(ALL_SUPER_HEROES)
                .then()
                .assertThat()
                .statusCode(BAD_REQUEST.value());

        verify(superHeroService, never()).createSuperHero(newHero.getName());
    }
}
