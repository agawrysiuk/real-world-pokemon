package agawrysiuk.googlemapspokemonclone.model;

import agawrysiuk.googlemapspokemonclone.R;

public class Settings {
    private static Settings instance = new Settings();
    private boolean darkTheme = false;
    private boolean sound = true;
    private String avatar = "player_front";
    private int style = R.style.AppTheme;
    private int mapStyle = R.raw.style_json;

    private Settings() {

    }

    public static Settings getInstance() {
        return instance;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        style = darkTheme ? R.style.AppThemeDark : R.style.AppTheme;
        mapStyle = darkTheme? R.raw.style_json_dark : R.raw.style_json;
    }

    public boolean isSoundOn() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStyle() {
        return style;
    }

    public int getMapStyle() {
        return mapStyle;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }
}
