package fr.soat.training.api.superhero.web.requests;

import java.util.Objects;

public class CreateHistoryEventRequest {

    private String description;

    public CreateHistoryEventRequest() {
    }

    public CreateHistoryEventRequest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CreateHistoryEventRequest that = (CreateHistoryEventRequest) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
