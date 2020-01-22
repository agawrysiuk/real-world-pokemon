package agawrysiuk.googlemapspokemonclone;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import agawrysiuk.googlemapspokemonclone.model.Database;
import agawrysiuk.googlemapspokemonclone.model.Pokemon;
import agawrysiuk.googlemapspokemonclone.views.PokemonListAdapter;

public class CollectionActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        List<Pokemon> pokemonList = Database.getInstance().getCollection();

        int size = pokemonList.size();

        String[] maintitle = new String[size];
        String[] subtitle = new String[size];
        Integer[] imgid = new Integer[size];

        for (int i = 0; i < size; i++) {
            Pokemon pokemon = pokemonList.get(i);
            maintitle[i] = pokemon.getName();
            subtitle[i] = "Number "+pokemon.getNumber();
            imgid[i] = pokemon.getDrawable();
        }

        PokemonListAdapter adapter = new PokemonListAdapter(this, maintitle, subtitle, imgid);
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
    }
}
