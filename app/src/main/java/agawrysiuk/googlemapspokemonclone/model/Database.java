package agawrysiuk.googlemapspokemonclone.model;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Database instance = new Database();
    private Map<String, Pokemon> pokemons = new HashMap<>();
    private List<Pokemon> collection = new ArrayList<>();

    private Database() {
    }

    public static Database getInstance() {
        return instance;
    }

    public Map<String, Pokemon> getPokemons() {
        return pokemons;
    }

    public void downloadDatabase() {
        // == default pokemon if no connection ==
        pokemons.put("000", new Pokemon("000", "MISSINGNO.", 2131099793));

        // == download all ==
        try {
            ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("Pokemon");
            queryAll.setLimit(151);
            List<ParseObject> list = queryAll.find();
            for (ParseObject object : list) {
                String number = object.getString("number");
                String name = object.getString("name");
                int drawable = object.getInt("drawable");
                pokemons.put(number, new Pokemon(number, name, drawable));
            }
            Log.i("INFO", "Download of pokemon data completed");
        } catch (ParseException e) {
            Log.i("ERROR", "Download stopped.");
            e.printStackTrace();
        }
    }

    public void downloadYourCollection() {
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Collection");
            parseQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            ParseObject object = parseQuery.getFirst();
            List<Pokemon> list = object.getList("collection");
            Log.i("INFO", "Download of user list completed");
            Log.i("INFO", "List = "+list.toString());
            Collections.copy(list,collection);
        } catch (ParseException e) {
            Log.i("ERROR", "Download stopped.");
            e.printStackTrace();
        }
    }

    public void addPokemonToYourCollection(Pokemon pokemon) {
        collection.add(pokemon);
    }
}
