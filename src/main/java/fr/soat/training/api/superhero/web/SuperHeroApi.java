package fr.soat.training.api.superhero.web;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import fr.soat.training.api.superhero.services.SuperHeroService;
import fr.soat.training.api.superhero.services.domain.MatchingHero;
import fr.soat.training.api.superhero.web.requests.CreateSuperHeroRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/super-heroes/")
public class SuperHeroApi {

    private final SuperHeroService superHeroService;

    public SuperHeroApi(SuperHeroService superHeroService) {
        this.superHeroService = superHeroService;
    }

    @GetMapping()
    public ResponseEntity<List<MatchingHero>> getAllTheSuperHeroes(){
        List<MatchingHero> allTheMissions = this.superHeroService.findAllTheMissions();
        if(allTheMissions.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allTheMissions);
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<MatchingHero> getSuperHero(@PathVariable String uuid) {
        MatchingHero superHero = this.superHeroService.getSuperHero(UUID.fromString(uuid));

        return ResponseEntity.ok(superHero);
    }

    @PostMapping()
    public ResponseEntity<URI> createSuperHero(@RequestBody CreateSuperHeroRequest request){
        MatchingHero newHero = this.superHeroService.createSuperHero(request.getName());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newHero.getUuid()).toUri();

        return ResponseEntity.created(location).build();
    }

}
