package agawrysiuk.googlemapspokemonclone.model;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
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
            List<ParseObject> objects = parseQuery.find();
            if (objects.size() > 0) {
                for (ParseObject object : objects) {
                    String pokemonId = object.getString("pokemonId");
                    collection.add(pokemons.get(pokemonId));
                }
            }
        } catch (ParseException e) {
            Log.i("ERROR", "Download stopped.");
            e.printStackTrace();
        }
        Log.i("INFO", collection.toString());
    }

    public void addPokemonToYourCollection(Pokemon pokemon) {
        if (!collection.contains(pokemon)) {
            collection.add(pokemon);
            final ParseObject object = new ParseObject("Collection");
            object.put("username", ParseUser.getCurrentUser().getUsername());
            object.put("pokemonId",pokemon.getNumber());
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("INFO", "Pokemon saved.");
                    } else {
                        Log.i("ERROR", "Can't save the pokemon.");
                    }
                }
            });
        } else {
            Log.i("INFO", "Pokemon already in collection.");
        }
    }
}
