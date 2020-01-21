package agawrysiuk.googlemapspokemonclone.model;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Database instance = new Database();
    private Map<String, Map<String, Object>> pokemons;

    private Database() {
        pokemons = new HashMap<>();
    }

    public static Database getInstance() {
        return instance;
    }

    public Map<String, Map<String, Object>> getPokemons() {
        return pokemons;
    }
}
