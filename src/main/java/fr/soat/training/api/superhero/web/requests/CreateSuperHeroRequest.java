package fr.soat.training.api.superhero.web.requests;

public class CreateSuperHeroRequest {
    private String name;

    public CreateSuperHeroRequest() {
    }

    public CreateSuperHeroRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
