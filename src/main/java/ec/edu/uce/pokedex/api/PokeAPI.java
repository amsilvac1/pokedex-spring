package ec.edu.uce.pokedex.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PokeAPI {
    public List<String> fetchAllPokemonUrls() throws Exception {
        String apiUrl = "https://pokeapi.co/api/v2/pokemon?limit=1304";
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray results = jsonResponse.getJSONArray("results");

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            urls.add(results.getJSONObject(i).getString("url"));
        }

        return urls;
    }
}
