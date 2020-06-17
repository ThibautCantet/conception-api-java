package fr.soat.training.api.superhero.web;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import fr.soat.training.api.superhero.services.SuperHeroService;
import fr.soat.training.api.superhero.services.domain.MatchingHero;
import fr.soat.training.api.superhero.web.requests.CreateSuperHeroRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/super-heroes/")
@Validated
public class SuperHeroApi {

    private final SuperHeroService superHeroService;

    public SuperHeroApi(SuperHeroService superHeroService) {
        this.superHeroService = superHeroService;
    }

    @GetMapping()
    public ResponseEntity<List<MatchingHero>> getAllTheSuperHeroes() {
        List<MatchingHero> allTheMissions = this.superHeroService.findAllTheMissions();
        if (allTheMissions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allTheMissions);
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<MatchingHero> getSuperHero(@Valid GetSuperHeroById request) {
        MatchingHero superHero = this.superHeroService.getSuperHero(UUID.fromString(request.getUuid()));

        return ResponseEntity.ok(superHero);
    }

    @PostMapping()
    public ResponseEntity<URI> createSuperHero(@RequestBody @Valid CreateSuperHeroRequest request) {
        MatchingHero newHero = this.superHeroService.createSuperHero(request.name());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newHero.getUuid()).toUri();

        return ResponseEntity.created(location).build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("invalid due to " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
