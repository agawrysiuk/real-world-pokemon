package agawrysiuk.googlemapspokemonclone.support;

import android.os.Handler;
import android.os.HandlerThread;

public class MyWorkerThread extends HandlerThread {

    private Handler mWorkerHandler;

    public MyWorkerThread(String name) {
        super(name);
    }

    public void postTask(Runnable task){
        mWorkerHandler.postDelayed(task, 1000); // set timeout which needed
    }

    public void prepareHandler(){
        mWorkerHandler = new Handler(getLooper());
    }
}
