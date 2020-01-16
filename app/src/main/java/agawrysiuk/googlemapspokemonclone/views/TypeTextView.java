package agawrysiuk.googlemapspokemonclone.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class TypeTextView extends TextView {

    // == custom listener for the view ==
    private OnTypeTextViewFinished listener = null;

    // == class fields ==
    private CharSequence text;
    private int index;
    private int delay = 25;

    // == creating handler to start typing
    private Handler mHandler = new Handler();
    // == using recurrence to type text ==
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0, index++));
            // == we type the text
            if(index <= text.length()) {
                mHandler.postDelayed(characterAdder, delay);
            // == we call for the listener if it's available after the typer has finished its job ==
            } else {
                if (listener != null) {
                    listener.onTyperFinished();
                }
            }
        }
    };

    public TypeTextView(Context context) {
        super(context);
    }

    // == we need this constructor because we put it into our xml, otherwise the app will crash
    public TypeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // == set up text to type ==
    public TypeTextView setTextAttr(CharSequence  text) {
        this.text = text;
        return this;
    }

    // == change the delay value between characters ==
    public TypeTextView setDelayAttr(int delay) {
        this.delay = delay;
        return this;
    }

    // == if we want to change the visibility ==
    public TypeTextView setVisible(boolean isVisible) {
        this.setVisibility(isVisible? VISIBLE : INVISIBLE);
        return this;
    }

    // == starts animating text ==
    public void animateTypeText() {
        index = 0;
        setText("");

        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, delay);
    }

    // == starts animating text ==
    public void animateTypeText(long millis) {
        index = 0;
        setText("");

        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, millis);
    }

    // == setter for the listener ==
    public TypeTextView setOnTypeTextViewFinishedListener(TypeTextView.OnTypeTextViewFinished listener) {
        this.listener = listener;
        return this;
    }

    // == listener ==
    public interface OnTypeTextViewFinished {
        void onTyperFinished();
    }
}
