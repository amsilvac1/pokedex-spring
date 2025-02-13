package ec.edu.uce.pokedex;

import ec.edu.uce.pokedex.api.PokeService;
import ec.edu.uce.pokedex.service.PokemonService;
//import ec.edu.uce.pokedex.swing.PokedexGUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class PokedexApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private PokeService pokeService;

    @Autowired
    private PokemonService pokemonService;
    public static void main(String[] args) {
        SpringApplication.run(PokedexApplication.class, args); }

    @Override
    public void run(String... args) throws Exception {
        pokeService.fetchAndSaveAllPokemon();
    }

}
