package fr.soat.training.api.superhero.web.requests;


import javax.validation.constraints.NotBlank;

public record CreateMissionRequest(
    @NotBlank
    String title,
    @NotBlank
    String assignedHero) {
}
