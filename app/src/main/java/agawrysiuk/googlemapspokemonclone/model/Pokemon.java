package agawrysiuk.googlemapspokemonclone.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public final class Pokemon implements Serializable,Comparable<Pokemon> {

    private String number;
    private String name;
    private int drawable;

    public Pokemon(String number, String name, int drawable) {
        this.number = number;
        this.name = name;
        this.drawable = drawable;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getDrawable() {
        return drawable;
    }

    @Override
    public int compareTo(Pokemon o) {
        return o.getName().compareTo(this.name);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
