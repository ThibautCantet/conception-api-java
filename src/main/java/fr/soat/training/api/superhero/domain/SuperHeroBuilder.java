package fr.soat.training.api.superhero.domain;

public class SuperHeroBuilder {

    public SuperHero createSuperHero(String name) {
        SuperHero hero = new SuperHero(name);
        return hero;
    }
}
