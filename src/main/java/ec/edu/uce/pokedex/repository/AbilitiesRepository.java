package ec.edu.uce.pokedex.repository;


import ec.edu.uce.pokedex.entities.Abilities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbilitiesRepository extends JpaRepository<Abilities, Long> {
    Optional<Abilities> findByName(String name);
}
