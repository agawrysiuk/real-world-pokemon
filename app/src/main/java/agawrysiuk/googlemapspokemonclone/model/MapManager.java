package agawrysiuk.googlemapspokemonclone.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import agawrysiuk.googlemapspokemonclone.R;

public class MapManager {
    private final int RESIZE_MULTIPLIER = 7;
    private Bitmap playersIcon;
    Context context;

    public MapManager(Context context) {
        this.context = context;
        this.playersIcon = createMapIcon(R.drawable.player_back);
    }

    public Bitmap createMapIcon(int iconDrawable){
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), iconDrawable);
        return Bitmap.createScaledBitmap(
                imageBitmap,
                imageBitmap.getWidth(),
                imageBitmap.getHeight(),
                false);
    }

    public Bitmap getPlayersIcon() {
        return playersIcon;
    }
}
