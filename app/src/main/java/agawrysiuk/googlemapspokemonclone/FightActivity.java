package agawrysiuk.googlemapspokemonclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.ViewTreeObserver;

public class FightActivity extends AppCompatActivity {

    private ConstraintLayout mFightLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        mFightLayout = findViewById(R.id.fightLayout);

        final Handler mHandler = new Handler();
        // == using recurrence to type text ==
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(1000);
                TransitionManager.beginDelayedTransition(mFightLayout,autoTransition);
                final ConstraintSet constraint = new ConstraintSet();
                constraint.clone(FightActivity.this, R.layout.activity_fight_end);
                constraint.applyTo(mFightLayout);
            }
        };

        // == animating the layout ==
        mFightLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFightLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHandler.postDelayed(runnable, 10);
            }
        });
    }
}
