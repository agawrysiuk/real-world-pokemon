package agawrysiuk.googlemapspokemonclone.model;

import android.content.res.Resources;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agawrysiuk.googlemapspokemonclone.utils.UtilParseName;

public class Database {
    private static Database instance = new Database();
    private Map<String, Pokemon> pokemons = new HashMap<>();
    private List<Pokemon> collection = new ArrayList<>();
    private String settingsId;

    private Database() {
    }

    public static Database getInstance() {
        return instance;
    }

    public Map<String, Pokemon> getPokemons() {
        return pokemons;
    }

    public void downloadDatabase(Resources resources) {
        // == default pokemon if no connection ==
        pokemons.put("000", new Pokemon("000", "MISSINGNO.", 2131099793));

        // == download all ==
        try {
            ParseQuery<ParseObject> queryAll = ParseQuery.getQuery(UtilParseName.PARSE_POKEMON_CLASS);
            queryAll.setLimit(151);
            List<ParseObject> list = queryAll.find();
            for (ParseObject object : list) {
                String number = object.getString(UtilParseName.PARSE_POKEMON_NUMBER);
                String name = object.getString(UtilParseName.PARSE_POKEMON_NAME);
                int drawable = object.getInt(UtilParseName.PARSE_POKEMON_DRAWABLE); // to be implemented later on
                pokemons.put(number, new Pokemon(number, name, resources.getIdentifier("pokemon_"+number, "drawable", "agawrysiuk.googlemapspokemonclone")));
            }
            Log.i("INFO", "Download of pokemon data completed");
        } catch (ParseException e) {
            Log.i("ERROR", "Download stopped.");
            e.printStackTrace();
        }
    }

    public void downloadYourCollection() {
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(UtilParseName.PARSE_COLLECTION_CLASS);
            // == quering only pokemon that belong to this player ==
            parseQuery.whereEqualTo(UtilParseName.PARSE_USERNAME, ParseUser.getCurrentUser().getUsername());
            List<ParseObject> objects = parseQuery.find();
            // == adding pokemon to his collection ==
            if (objects.size() > 0) {
                for (ParseObject object : objects) {
                    String pokemonId = object.getString(UtilParseName.PARSE_COLLECTION_POKEMONID);
                    collection.add(pokemons.get(pokemonId));
                }
            }
        } catch (ParseException e) {
            Log.i("ERROR", "Download stopped.");
            e.printStackTrace();
        }
        // == sorting for good display ==
        Collections.sort(collection);
    }

    public void downloadYourSettings() {
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(UtilParseName.PARSE_SETTINGS_CLASS);
            parseQuery.whereEqualTo(UtilParseName.PARSE_USERNAME, ParseUser.getCurrentUser().getUsername());
            ParseObject object = parseQuery.getFirst();

            Settings settings = Settings.getInstance();
            settings.setDarkTheme(object.getBoolean(UtilParseName.PARSE_SETTINGS_DARKTHEME));
            settings.setSound(object.getBoolean(UtilParseName.PARSE_SETTINGS_SOUND));
            settingsId = object.getObjectId();

        } catch (ParseException e) {
            Log.i("ERROR", "Settings download stopped.");
            e.printStackTrace();
        }
    }

    public void addPokemonToYourCollection(Pokemon pokemon) {
        if (!collection.contains(pokemon)) {
            collection.add(pokemon);
            final ParseObject object = new ParseObject(UtilParseName.PARSE_COLLECTION_CLASS);
            object.put(UtilParseName.PARSE_USERNAME, ParseUser.getCurrentUser().getUsername());
            object.put(UtilParseName.PARSE_COLLECTION_POKEMONID,pokemon.getNumber());
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
            Collections.sort(collection);
        } else {
            Log.i("INFO", "Pokemon already in collection.");
        }
    }

    public List<Pokemon> getCollection() {
        return collection;
    }

    public void saveSettings() {
        final ParseObject object = new ParseObject(UtilParseName.PARSE_SETTINGS_CLASS);
        object.setObjectId(settingsId);
        object.put(UtilParseName.PARSE_SETTINGS_DARKTHEME,Settings.getInstance().isDarkTheme());
        object.put(UtilParseName.PARSE_SETTINGS_SOUND,Settings.getInstance().isSoundOn());
        object.put(UtilParseName.PARSE_SETTINGS_AVATAR,Settings.getInstance().getAvatar());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("INFO", "Settings saved.");
                } else {
                    Log.i("ERROR", "Can't save settings.");
                }
            }
        });
    }
}
