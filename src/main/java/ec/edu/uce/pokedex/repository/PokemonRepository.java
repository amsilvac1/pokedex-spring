package ec.edu.uce.pokedex.repository;

import ec.edu.uce.pokedex.entities.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

    // Buscar Pokémon por nombre
    Optional<Pokemon> findByName(String name);

    // Buscar Pokémon por habilidad
    List<Pokemon> findByAbilities_Name(String abilityName);

    // Buscar Pokémon por tipo
    List<Pokemon> findByTypes_Name(String typeName);
}


