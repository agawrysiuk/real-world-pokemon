package agawrysiuk.googlemapspokemonclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Random;

import agawrysiuk.googlemapspokemonclone.support.MyWorkerThread;
import agawrysiuk.googlemapspokemonclone.support.SynchronousHandler;
import agawrysiuk.googlemapspokemonclone.views.TypeTextView;


public class FightActivity extends AppCompatActivity {

    private enum FightStages {
        START_TEXT,
        HERO_POKEMON, OPPONENT_POKEMON, // for the future opening fight implementation
        ACTUAL_FIGHT, //for the future fight implementation
        POKEBALL_THROW, //only for pokemons
        END_TEXT; //when we win/catch pokemon
    }

    private ConstraintLayout mFightLayout;
    private TypeTextView mFightTyper;
    private ImageView mPokeballAnim, mEnemyPicture, mPlayerPicture;
    private FightStages mStage = FightStages.START_TEXT;
    private boolean lockScreen = true;
    private int catchChance = 50;
    private static final int CATCH_SIZE = 100;

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
                int duration = 1000;
                // == changing constraints (moving views on the layout) ==
                Transition autoTransition = new AutoTransition();
                autoTransition.setDuration(duration);
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
                                mStage = FightStages.POKEBALL_THROW;
                                lockScreen = false;
                            }
                        })
                        // == start animation after 2 seconds ==
                        .animateTypeText(duration + 1000);
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
        if (lockScreen) {
            return;
        }
        switch (mStage) {
            case START_TEXT:
                return;
            case POKEBALL_THROW:
                lockScreen = true;
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
                pokeballPoof(false);
            }
        });
        a.start();
    }

    //out is false -> we got here from throwing a ball
    //out is true -> we got here because a pokemon got out from the ball (and we don't want jiggle animation)
    private void pokeballPoof(final boolean out) {
        // == animating explosion ==
        final ImageView expl = new ImageView(this);
        expl.setX(mEnemyPicture.getX());
        expl.setY(mEnemyPicture.getY());
        mFightLayout.addView(expl);
        expl.setBackgroundResource(R.drawable.animation_pokeball_poof);
        AnimationDrawable animation = (AnimationDrawable) expl.getBackground();
        animation.setVisible(true, true);
        animation.start();

        // == disappearing the layout after animation duration ==
        int duration = getAnimDuration(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFightLayout.removeView(expl);
                if (!out) {
                    jigglePokeball();
                } else {
                    lockScreen = false; //we go out and try to catch pokemon again
                }
            }
        }, duration);
    }

    private void jigglePokeball() {
        //== setting up visibility and animation ==
        mEnemyPicture.setVisibility(View.INVISIBLE);
        mPokeballAnim.setVisibility(View.VISIBLE);
        final AnimationDrawable animation = (AnimationDrawable) mPokeballAnim.getBackground();

        // == utils ==
        Random random = new Random();
        boolean[] doJiggle = new boolean[3];

        //setting up jiggle chance ==
        for (int i = 0; i < 3; i++) {
            if (random.nextInt(CATCH_SIZE) <= catchChance) {
                doJiggle[i] = true;
            }
        }

        // == checking animation duration ==
        int duration = getAnimDuration(animation);

        // == runnable if it stays (animation plays out) ==
        Runnable jiggleRunnable = new Runnable() {
            @Override
            public void run() {
                animation.setVisible(true, true);
                animation.start();
            }
        };

        // == runnable if it goes out ==
        Runnable outRunnable = new Runnable() {
            @Override
            public void run() {
                mEnemyPicture.setVisibility(View.VISIBLE);
                mPokeballAnim.setVisibility(View.INVISIBLE);
                pokeballPoof(true);
            }
        };

        Handler handler = new Handler();
        Log.i("INFO", Arrays.toString(doJiggle));

        // == setting up our animations ==
        for (int i = 0; i < doJiggle.length; i++) {
            if (doJiggle[i]) {
                handler.postDelayed(jiggleRunnable, 1000 + i * duration);
            } else {
                handler.postDelayed(outRunnable, 1000 + i * duration);
                break; // we don't want to continue animation if the pokemon goes out
            }
        }
    }

    private int getAnimDuration(AnimationDrawable animation) {
        int duration = 0;
        for (int i = 0; i < animation.getNumberOfFrames(); i++) {
            duration += animation.getDuration(i);
        }
        return duration;
    }
}
