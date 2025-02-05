package ec.edu.uce.pokedex;

import ec.edu.uce.pokedex.api.PokeService;
import ec.edu.uce.pokedex.service.PokemonService;
import ec.edu.uce.pokedex.swing.PokedexGUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class PokedexApplication implements CommandLineRunner {

    @Autowired
    private PokeService pokeService;

    @Autowired
    private PokemonService pokemonService;
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(PokedexApplication.class, args); }

    @Override
    public void run(String... args) throws Exception {
        double timeData = pokeService.fetchAndSaveAllPokemon();
        if (!GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> {
                PokedexGUI pokedexGUI = new PokedexGUI(pokemonService, timeData);
                pokedexGUI.setVisible(true);
                pokedexGUI.loadPokemons();
            });
        } else {
            System.out.println("Entorno sin GUI detectado. Ejecución en modo headless.");
        }
    }

}
