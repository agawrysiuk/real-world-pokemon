package agawrysiuk.googlemapspokemonclone.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

@SuppressLint("AppCompatCustomView")
public class TypeTextView extends TextView {

    private CharSequence text;
    private int index;
    private int delay = 25;
    private Handler mHandler = new Handler();
    // == using recurrence to type text ==
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0, index++));
            if(index <= text.length()) {
                mHandler.postDelayed(characterAdder, delay);
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

    public TypeTextView setTextAttr(CharSequence  text) {
        this.text = text;
        return this;
    }

    public TypeTextView setDelayAttr(int delay) {
        this.delay = delay;
        return this;
    }

    public TypeTextView setVisible() {
        this.setVisibility(VISIBLE);
        return this;
    }

    public void animateTypeText() {
        index = 0;
        setText("");

        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, delay);
    }


}
