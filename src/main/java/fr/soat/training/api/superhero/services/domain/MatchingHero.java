package fr.soat.training.api.superhero.services.domain;

import fr.soat.training.api.superhero.domain.SuperHero;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class MatchingHero {

    @NotEmpty
    @NotNull
    private String name;
    private UUID uuid;
    private LocalDateTime createdSince;

    public MatchingHero(SuperHero superHero) {
        this(superHero.getName(), superHero.getCreatedAt());
        this.uuid = superHero.getUUID();
    }

    public MatchingHero(String name, LocalDateTime createdSince) {
        this.name = name;
        this.createdSince = createdSince;
    }

    public MatchingHero(String name, UUID randomUUID) {
        this(name, LocalDateTime.now());
        this.uuid = randomUUID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchingHero that = (MatchingHero) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(createdSince, that.createdSince);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid, createdSince);
    }

    @Override
    public String toString() {
        return "MatchingHero{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                ", createdSince=" + createdSince +
                '}';
    }
}
