package fr.soat.training.api.superhero.web;

import fr.soat.training.api.superhero.services.SuperHeroService;
import fr.soat.training.api.superhero.services.domain.MatchingHero;
import fr.soat.training.api.superhero.web.requests.CreateSuperHeroRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/super-heroes/")
public class SuperHeroApi {

    @Autowired
    private SuperHeroService superHeroService;

    @GetMapping()
    public ResponseEntity<?> getAllTheSuperHeroes(){
        List<MatchingHero> allTheMissions = this.superHeroService.findAllTheMissions();
        if(allTheMissions.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allTheMissions);
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<?> getSuperHero(@PathVariable String uuid) {
        MatchingHero superHero = this.superHeroService.getSuperHero(UUID.fromString(uuid));

        return ResponseEntity.ok(superHero);
    }

    @PostMapping()
    public ResponseEntity<?> createSuperHero(@RequestBody CreateSuperHeroRequest request){
        MatchingHero newHero = this.superHeroService.createSuperHero(request.getName());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newHero.getUuid()).toUri();

        return ResponseEntity.created(location).build();
    }

}
