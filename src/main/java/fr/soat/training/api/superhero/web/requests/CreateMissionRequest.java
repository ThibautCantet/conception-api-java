package fr.soat.training.api.superhero.web.requests;

import java.util.Objects;


public class CreateMissionRequest {

    private String title;

    private String assignedHero;

    public CreateMissionRequest() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAssignedHero() {
        return assignedHero;
    }

    public void setAssignedHero(final String assignedHero) {
        this.assignedHero = assignedHero;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CreateMissionRequest that = (CreateMissionRequest) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(assignedHero, that.assignedHero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, assignedHero);
    }
}
