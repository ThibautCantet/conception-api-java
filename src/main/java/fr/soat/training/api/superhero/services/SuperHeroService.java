package fr.soat.training.api.superhero.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.soat.training.api.superhero.domain.SuperHero;
import fr.soat.training.api.superhero.domain.builders.SuperHeroBuilder;
import fr.soat.training.api.superhero.repository.SuperHeroRepository;
import fr.soat.training.api.superhero.services.domain.MatchingHero;
import org.springframework.stereotype.Service;

@Service
public class SuperHeroService {

    private final SuperHeroRepository superHeroRepository;

    public SuperHeroService(SuperHeroRepository superHeroRepository) {
        this.superHeroRepository = superHeroRepository;
    }

    public MatchingHero createSuperHero(String name) {
        SuperHero toBeSaved = new SuperHeroBuilder().createSuperHero(name);
        SuperHero hero = this.superHeroRepository.saveAndFlush(toBeSaved);

        return new MatchingHero(hero);
    }


    public List<MatchingHero> findAllTheMissions() {
        List<SuperHero> missions = this.superHeroRepository.findAll();
        return missions.stream().map(MatchingHero::new).toList();
    }

    public MatchingHero getTheSuperHeroMatching(String name) {
        Optional<SuperHero> matchingSuperHero = this.superHeroRepository.findByName(name);
        return matchingSuperHero.map(MatchingHero::new)
                .orElse(null);
    }

    public SuperHero getTheSuperHero(String name) {
        return this.superHeroRepository.findByName(name).orElse(null);
    }

    public MatchingHero getSuperHero(UUID heroId) {
        Optional<SuperHero> found = this.superHeroRepository.findById(heroId);
        return found.map(MatchingHero::new).orElse(null);
    }

    public boolean exists(String name) {
        return this.superHeroRepository.findByName(name).isPresent();
    }
}
