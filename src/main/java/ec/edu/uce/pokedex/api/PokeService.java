package ec.edu.uce.pokedex.api;

import ec.edu.uce.pokedex.entities.Abilities;
import ec.edu.uce.pokedex.entities.Pokemon;
import ec.edu.uce.pokedex.entities.Type;
import ec.edu.uce.pokedex.repository.PokemonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class PokeService {
    @Autowired
    private PokemonRepository pokemonRepository;

    public double fetchAndSaveAllPokemon() throws Exception {
        PokeAPI apiClient = new PokeAPI();
        List<String> pokemonUrls = apiClient.fetchAllPokemonUrls();

        // Medir el tiempo de inicio
        long startTime = System.currentTimeMillis();

        // Crear un pool de hilos
        ExecutorService executorService = Executors.newFixedThreadPool(10); // Usa 10 hilos concurrentes

        // Procesar cada URL de forma concurrente
        List<CompletableFuture<Void>> futures = pokemonUrls.stream()
                .map(url -> CompletableFuture.runAsync(() -> {
                    try {
                        processAndSavePokemon(url);
                    } catch (Exception e) {
                        System.err.println("Error al procesar Pokémon desde: " + url + " - " + e.getMessage());
                    }
                }, executorService))
                .toList();

        // Esperar a que todos los hilos terminen
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Medir el tiempo de finalización
        long endTime = System.currentTimeMillis();

        // Cerrar el pool de hilos
        executorService.shutdown();

        //Sin hilos
//        for (String url : pokemonUrls) {
//            processAndSavePokemon(url);
//        }
        // Calcular y mostrar el tiempo total en segundos
        double totalTimeSeconds = (endTime - startTime) / 1000.0;
        System.out.printf("¡Todos los Pokémon han sido procesados y guardados! Tiempo total: %.2f segundos%n", totalTimeSeconds);

        return totalTimeSeconds;
    }

    private void processAndSavePokemon(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject pokemonJson = new JSONObject(response.toString());

        Pokemon pokemon = new Pokemon();
        pokemon.setName(pokemonJson.getString("name"));
        pokemon.setHeight(pokemonJson.getDouble("height"));
        pokemon.setWeight(pokemonJson.getDouble("weight"));

        // Manejar potencial null en 'front_default'
        JSONObject sprites = pokemonJson.getJSONObject("sprites");
        String frontDefault = sprites.isNull("front_default") ? null : sprites.getString("front_default");
        pokemon.setImage(frontDefault);

        // Procesar habilidades
        JSONArray abilitiesArray = pokemonJson.getJSONArray("abilities");
        List<Abilities> abilities = new ArrayList<>();
        for (int i = 0; i < abilitiesArray.length(); i++) {
            String abilityName = abilitiesArray.getJSONObject(i).getJSONObject("ability").getString("name");
            Abilities ability = new Abilities();
            ability.setName(abilityName);
            abilities.add(ability);
        }
        pokemon.setAbilities(abilities);

        // Procesar tipos
        JSONArray typesArray = pokemonJson.getJSONArray("types");
        List<Type> types = new ArrayList<>();
        for (int i = 0; i < typesArray.length(); i++) {
            String typeName = typesArray.getJSONObject(i).getJSONObject("type").getString("name");
            Type type = new Type();
            type.setName(typeName);
            types.add(type);
        }
        pokemon.setTypes(types);

        // Guardar en la base de datos
        pokemonRepository.save(pokemon);
    }
}
