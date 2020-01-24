package agawrysiuk.googlemapspokemonclone.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;

import agawrysiuk.googlemapspokemonclone.R;

/**
 * This class is used to play sounds across activities, like the
 * background music in our program. Can be extended to play several
 * different sounds.
 * This code was heavily based on the code found here:
 * http://www.rbgrn.net/content/307-light-racer-20-days-61-64-completion
 *
 */
public class MusicManager {
    private static final String TAG = "MusicManager";
    public static final int MUSIC_MAP = R.raw.music_map;
    public static final int MUSIC_BATTLE = R.raw.music_map;
    public static final int MUSIC_LOGIN = R.raw.music_login;


    private static HashMap<Integer, MediaPlayer> players = new HashMap<>();
    private static int currentMusic = -1;

    public static float getMusicVolume(Context context) {
        //could be updated based on user preferences
        return 1.0f;
    }

    public static void start(Context context, int music) {
        start(context, music, false);
    }

    public static void start(Context context, int music, boolean force) {
        currentMusic = music;
        Log.d(TAG, "Current music is now [" +
                context.getResources().getResourceName(music)
                + "]");
        MediaPlayer mp = players.get(music);
        if (mp != null) {
            if (!mp.isPlaying()) {
                mp.stop();
                mp.start();
            }
        } else {
            if (music == MUSIC_MAP) {
                mp = MediaPlayer.create(context, R.raw.music_map);
//            } else if(music == MUSIC_GAMEPLAY){
//                mp = MediaPlayer.create(context, R.raw.gameplay);
            }else {
                Log.e(TAG, "unsupported music number - " + music);
                return;
            }

            players.put(music, mp);
            float volume = getMusicVolume(context);
            Log.d(TAG, "Setting music volume to " + volume);
            try {
                mp.setLooping(true);
                mp.start();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public static void pause() {
        Collection<MediaPlayer> mps = players.values();
        for (MediaPlayer p : mps) {
            if (p.isPlaying()) {
                p.stop();
            }
        }
        // previousMusic should always be something valid
        currentMusic = -1;
        Log.d(TAG, "Current music is now [" + currentMusic + "]");
    }

    public static void updateVolumeFromPrefs(Context context) {
        try {
            float volume = getMusicVolume(context);
            Log.d(TAG, "Setting music volume to " + volume);
            Collection<MediaPlayer> mps = players.values();
            for (MediaPlayer p : mps) {
                p.setVolume(volume, volume);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void release() {
        Log.d(TAG, "Releasing media players");
        Collection<MediaPlayer> mps = players.values();
        for (MediaPlayer mp : mps) {
            try {
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    mp.release();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        mps.clear();
        currentMusic = -1;
        Log.d(TAG, "Current music is now [" + currentMusic + "]");
    }


}
