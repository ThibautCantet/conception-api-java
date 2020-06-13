package fr.soat.training.api.superhero.repository;

import fr.soat.training.api.superhero.BaseRepositoryTest;
import fr.soat.training.api.superhero.domain.HistoricEvent;
import fr.soat.training.api.superhero.domain.Mission;
import fr.soat.training.api.superhero.domain.SuperHero;
import fr.soat.training.api.superhero.domain.SuperHeroBuilder;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HistoricEventRepositoryShould extends BaseRepositoryTest {

    private final SuperHero malicia = new SuperHeroBuilder().createSuperHero("Malicia");

    @Test
    void create_a_new_historic_event_linked_to_a_mission() {
        SuperHero savedHero = superHeroRepository.save(malicia);

        Mission aMission = new MissionBuilder().createMission("Save the X-Men !").assignedTo(savedHero).build();
        Mission savedMission = this.missionRepository.save(aMission);

        HistoricEvent theEvent = new HistoricEventBuilder().createAction("Takes off her gloves !")
                .madeDuringTheMission(savedMission)
                .build();

        HistoricEvent historicEvent = this.historicEventRepository.save(theEvent);
        Assertions.assertThat(historicEvent)
                .as("Malicia should have taken off ther gloves")
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("description", "Takes off her gloves !")
                .hasFieldOrPropertyWithValue("mission.uuid", savedMission.getUUID())
                .hasFieldOrPropertyWithValue("mission.title", "Save the X-Men !");
}

    @Test
    void retrieve_an_historical_event_given_an_uuid() {
        SuperHero savedHero = superHeroRepository.save(malicia);

        Mission aMission = new MissionBuilder().createMission("Save the X-Men !").assignedTo(savedHero).build();
        Mission savedMission = this.missionRepository.save(aMission);

        HistoricEvent theEvent = new HistoricEventBuilder().createAction("Takes off her gloves !")
                .madeDuringTheMission(savedMission).build();

        HistoricEvent historicEvent = this.historicEventRepository.save(theEvent);


        Optional<HistoricEvent> found = this.historicEventRepository.findById(historicEvent.getUUID());
        Assertions.assertThat(found)
                .as("The historical event should exist")
                .isPresent();
    }

    @Test
    void returns_all_the_events_linked_to_a_mission() {
        SuperHero savedHero = superHeroRepository.save(malicia);

        Mission aMission = new MissionBuilder().createMission("Save the X-Men !").assignedTo(savedHero).build();
        Mission savedMission = this.missionRepository.save(aMission);
        HistoricEvent dressEvent = new HistoricEventBuilder().createAction("Get dressed!").madeDuringTheMission(savedMission).build();
        HistoricEvent glovesEvent = new HistoricEventBuilder().createAction("Take her gloves !").madeDuringTheMission(savedMission).build();

        this.historicEventRepository.saveAll(Arrays.asList(dressEvent, glovesEvent));

        List<HistoricEvent> allEvents = this.historicEventRepository.findAllByMission(aMission.getUUID());
        Assertions.assertThat(allEvents)
                .as("should have 2 entries as historic")
                .hasSize(2)
                .extracting("mission.uuid","description")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(savedMission.getUUID(), dressEvent.getDescription()),
                        Tuple.tuple(savedMission.getUUID(), glovesEvent.getDescription())
                );

    }
}
