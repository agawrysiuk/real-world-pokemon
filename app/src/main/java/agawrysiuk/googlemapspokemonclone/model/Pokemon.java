package agawrysiuk.googlemapspokemonclone.model;

public final class Pokemon {

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
