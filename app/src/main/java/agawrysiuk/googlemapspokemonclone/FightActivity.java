package agawrysiuk.googlemapspokemonclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import agawrysiuk.googlemapspokemonclone.support.AnimationDrawableCallback;
import agawrysiuk.googlemapspokemonclone.views.TypeTextView;


public class FightActivity extends AppCompatActivity {

    private enum FightStages {
        START_TEXT,
        HERO_POKEMON, OPPONENT_POKEMON,
        ACTUAL_FIGHT,
        POKEBALL, //only for pokemons
        END_TEXT;
    }

    private ConstraintLayout mFightLayout;
    private TypeTextView mFightTyper;
    private ImageView mPokeballAnim,mEnemyPicture;
    private FightStages mStage = FightStages.START_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        mFightLayout = findViewById(R.id.fightLayout);
        mFightTyper = findViewById(R.id.fightTyper);
        mPokeballAnim = findViewById(R.id.fightPokeballAnim);
        mPokeballAnim.setBackgroundResource(R.drawable.animation_pokeball_jiggle);
        mEnemyPicture = findViewById(R.id.fightEnemyPicture);

        // == Start First Animation
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // == changing constraints (moving views on the layout) ==
                Transition autoTransition = new AutoTransition();
                autoTransition.setDuration(1000);
                TransitionManager.beginDelayedTransition(mFightLayout, autoTransition);
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
        switch (mStage) {
            case START_TEXT:
                return;
            case POKEBALL:
                jigglePokeball();
//                writeText("Throw pokeball?");
            default:
                return;
        }
        // == it's a root layout that will navigate between the stages
//        writeText("Test id " + ++testId);
    }

    private void writeText(String text) {
        mFightTyper.setText("");
        mFightTyper.setTextAttr(text).animateTypeText();
    }

    private void jigglePokeball() {
        mEnemyPicture.setVisibility(View.INVISIBLE);
        mPokeballAnim.setVisibility(View.VISIBLE);
        final AnimationDrawable animation = (AnimationDrawable) mPokeballAnim.getBackground();
        Log.i("INFO",
                animation.getNumberOfFrames()+"");
        animation.setVisible(true, true);
        animation.start();
    }
}
