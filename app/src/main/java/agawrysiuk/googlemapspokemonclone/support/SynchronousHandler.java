package agawrysiuk.googlemapspokemonclone.support;

import android.os.Handler;
import android.os.Looper;

public class SynchronousHandler  {

    private static class NotifyRunnable implements Runnable {
        private final Runnable mRunnable;
        private final Handler mHandler;
        private boolean mFinished = false;

        public  NotifyRunnable(final Handler handler, final Runnable r) {
            mRunnable = r;
            mHandler = handler;
        }

        public boolean isFinished() {
            return mFinished;
        }

        @Override
        public void run() {
            synchronized (mHandler) {
                mRunnable.run();
                mFinished = true;
                mHandler.notifyAll();
            }
        }
    }

    /**
     * Posts a runnable on a handler's thread and waits until it has finished running.
     *
     * The handler may be on the same or a different thread than the one calling this method.
     *
     */
    public static void postAndWait(final Handler handler, final Runnable r) {

        if (handler.getLooper() == Looper.myLooper()) {
            r.run();
        } else {
            synchronized (handler) {
                NotifyRunnable runnable = new NotifyRunnable(handler, r);
                handler.post(runnable);
                while (!runnable.isFinished()) {
                    try {
                        handler.wait();
                    } catch (InterruptedException is) {
                        // ignore
                    }
                }
            }
        }
    }

    public static void postDelayedAndWait (final Handler handler, final Runnable r, final long delayMillis) {

        if (handler.getLooper() == Looper.myLooper()) {
            r.run();
        } else {
            synchronized (handler) {
                NotifyRunnable runnable = new NotifyRunnable(handler, r);
                handler.postDelayed(runnable,delayMillis);
                while (!runnable.isFinished()) {
                    try {
                        handler.wait();
                    } catch (InterruptedException is) {
                        // ignore
                    }
                }
            }
        }
    }
}