package fr.soat.training.api.superhero.web;

import javax.validation.constraints.NotEmpty;

public class GetSuperHeroById {

    @NotEmpty
    private String uuid;

    public GetSuperHeroById(String uuid) {
        this.uuid = uuid;
    }

    public GetSuperHeroById() {
    }

    public String getUuid() {
        return uuid;
    }
}
