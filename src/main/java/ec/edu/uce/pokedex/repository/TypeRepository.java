package ec.edu.uce.pokedex.repository;

import ec.edu.uce.pokedex.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {
    //buscar pokemon por habilidad
    Optional<Type> findByName(String name);
}
