package agawrysiuk.googlemapspokemonclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import agawrysiuk.googlemapspokemonclone.views.TypeTextView;


public class FightActivity extends AppCompatActivity {

    private enum FightStages {
        START_TEXT,
        HERO_POKEMON,OPPONENT_POKEMON,
        ACTUAL_FIGHT,
        POKEBALL, //only for pokemons
        END_TEXT;
    }

    private ConstraintLayout mFightLayout;
    private TypeTextView mFightTyper;
    private int testId = 0;
    private FightStages mStage = FightStages.START_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        mFightLayout = findViewById(R.id.fightLayout);
        mFightTyper = findViewById(R.id.fightTyper);

        // == Start First Animation
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // == changing constraints (moving views on the layout) ==
                Transition autoTransition = new AutoTransition();
                autoTransition.setDuration(1000);
                TransitionManager.beginDelayedTransition(mFightLayout,autoTransition);
                final ConstraintSet constraint = new ConstraintSet();
                constraint.clone(FightActivity.this, R.layout.activity_fight_animation);
                constraint.applyTo(mFightLayout);

                // == typing text ==
                mFightTyper
                        // == typer text ==
                        .setTextAttr("Wild Bulbasaur appeared!")
                        // == what to do after typer finishes ==
                        .setOnTypeTextViewFinishedListener(new TypeTextView.OnTypeTextViewFinished() {
                            @Override
                            public void onTyperFinished() {
                                mStage = FightStages.POKEBALL;
                            }
                        })
                        // == start animation after 2 seconds ==
                        .animateTypeText(2000);
                // == when the typer ==
            }
        };
        mFightLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFightLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // == start animation ==
                new Handler().postDelayed(runnable, 10);
            }
        });

    }

    public void onScreenTapped(View view) {
        // == it's a root layout that will navigate between the stages
//        writeText("Test id " + ++testId);
    }

    private void writeText(String text) {
        mFightTyper.setText("");
        mFightTyper.setTextAttr(text).animateTypeText();
    }
}
