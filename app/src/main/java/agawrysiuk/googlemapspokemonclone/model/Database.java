package agawrysiuk.googlemapspokemonclone.model;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Database instance = new Database();
    private Map<String, Pokemon> pokemons = new HashMap<>();

    private Database() {
    }

    public static Database getInstance() {
        return instance;
    }

    public Map<String, Pokemon> getPokemons() {
        return pokemons;
    }

    public void downloadDatabase() {

    }
}
