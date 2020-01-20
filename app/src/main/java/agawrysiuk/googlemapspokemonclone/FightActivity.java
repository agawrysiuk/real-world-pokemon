package agawrysiuk.googlemapspokemonclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import agawrysiuk.googlemapspokemonclone.views.TypeTextView;


public class FightActivity extends AppCompatActivity {

    private enum FightStages {
        START_TEXT,
        HERO_POKEMON, OPPONENT_POKEMON, // for the future opening fight implementation
        ACTUAL_FIGHT, //for the future fight implementation
        POKEBALL, //only for pokemons
        END_TEXT; //when we win/catch pokemon
    }

    private ConstraintLayout mFightLayout;
    private TypeTextView mFightTyper;
    private ImageView mPokeballAnim, mEnemyPicture, mPlayerPicture;
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
        mPlayerPicture = findViewById(R.id.fightPlayerBackImage);

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
                throwPokeball();
//                jigglePokeball();
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

    private void throwPokeball() {
        // == creating pokeball object ==
        final ImageView pkbl = new ImageView(this);
        pkbl.setBackgroundResource(R.drawable.pokeball_center);
        mFightLayout.addView(pkbl);

        // == animating pokeball ==
        Path path = new Path();
        path.moveTo(
                mPlayerPicture.getX() + (float) mPlayerPicture.getWidth() / 4, //starting X
                mPlayerPicture.getY() + (float) mPlayerPicture.getHeight() / 4); //starting Y
        path.quadTo(mFightLayout.getWidth() / 2, //curve's X
                mFightLayout.getHeight() / 8, //curve's Y
                mEnemyPicture.getX() + (float) mEnemyPicture.getWidth() / 3, //end X
                mEnemyPicture.getY() + (float) mEnemyPicture.getHeight() / 3); //end Y
        ValueAnimator a = ObjectAnimator.ofFloat(pkbl, "x", "y", path);
        a.setDuration(750);
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFightLayout.removeView(pkbl);
                pokeballPoof();
            }
        });
        a.start();
    }

    private void pokeballPoof() {
        // == animating explosion ==
        final ImageView expl = new ImageView(this);
        expl.setX(mEnemyPicture.getX());
        expl.setY(mEnemyPicture.getY());
        mFightLayout.addView(expl);
        expl.setBackgroundResource(R.drawable.animation_pokeball_poof);
        AnimationDrawable animation = (AnimationDrawable) expl.getBackground();
        animation.setVisible(true, true);
        animation.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFightLayout.removeView(expl);
            }
        }, 400);
    }

    private void jigglePokeball() {
        mEnemyPicture.setVisibility(View.INVISIBLE);
        mPokeballAnim.setVisibility(View.VISIBLE);
        AnimationDrawable animation = (AnimationDrawable) mPokeballAnim.getBackground();
        animation.setVisible(true, true);
        animation.start();
    }
}
