package agawrysiuk.googlemapspokemonclone.model;

import java.io.Serializable;

public final class Pokemon implements Serializable {

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
}
