package agawrysiuk.googlemapspokemonclone.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import agawrysiuk.googlemapspokemonclone.R;

public class MapManager {
    private final int RESIZE_MULTIPLIER = 7;
    private Bitmap playersIcon;
    Context context;

    public MapManager(Context context) {
        this.context = context;
        this.playersIcon = createMapIcons(R.drawable.player_back);
    }

    public Bitmap createMapIcons(int iconDrawable){
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), iconDrawable);
        return Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth() * RESIZE_MULTIPLIER, imageBitmap.getHeight() * RESIZE_MULTIPLIER, false);
    }

    public Bitmap getPlayersIcon() {
        return playersIcon;
    }
}
