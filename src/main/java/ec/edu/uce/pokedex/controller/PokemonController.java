package ec.edu.uce.pokedex.controller;

import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:8080") // Ajusta según la URL de tu frontend
public class PokemonController {
    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    // Obtener todos los Pokémon
    @GetMapping
    public ResponseEntity<List<Pokemon>> getAllPokemon() {
        List<Pokemon> pokemonList = pokemonService.findAllPokemons();
        return ResponseEntity.ok(pokemonList);
    }

    // Obtener un Pokémon por ID
    @GetMapping("/{id}")
    public Pokemon  getPokemonById(@PathVariable Long id) {
        Pokemon pokemon = pokemonService.findPokemonById(id);
        return pokemon;
    }

    // Obtener un Pokémon por nombre
    @GetMapping("/name/{name}")
    public Pokemon getPokemonByName(@PathVariable String name) {
        Pokemon pokemon = pokemonService.getPokemonByName(name);
        return pokemon;
    }

    // Obtener un Pokémon por habilidad
    @GetMapping("/ability/{ability}")
    public List<Pokemon> getPokemonByAbility(@PathVariable String ability) {
        List<Pokemon> pokemons = pokemonService.getPokemonsByAbility(ability);
        return pokemons;
    }

    // Obtener un Pokémon por habilidad
    @GetMapping("/type/{type}")
    public List<Pokemon> getPokemonByType(@PathVariable String type) {
        List<Pokemon> pokemons = pokemonService.getPokemonsByType(type);
        return pokemons;
    }

}


