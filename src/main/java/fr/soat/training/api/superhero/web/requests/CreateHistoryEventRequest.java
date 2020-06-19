package fr.soat.training.api.superhero.web.requests;

import javax.validation.constraints.NotBlank;

public record CreateHistoryEventRequest(@NotBlank String description) {
}
