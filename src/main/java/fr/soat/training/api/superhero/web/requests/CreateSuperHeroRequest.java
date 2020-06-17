package fr.soat.training.api.superhero.web.requests;

import javax.validation.constraints.NotEmpty;

public record CreateSuperHeroRequest(@NotEmpty String name) {
}
