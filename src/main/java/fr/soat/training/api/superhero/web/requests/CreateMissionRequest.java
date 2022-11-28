package fr.soat.training.api.superhero.web.requests;


public record CreateMissionRequest(
    String title,
    String assignedHero) {
}
