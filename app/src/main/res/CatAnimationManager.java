// CatAnimationManager.java
package com.rpawtaps.animations;
//IBAHIN MO NALANG YUNG PACKAGE NAME BOSS MEMA PACKAGE LANG AKO DITO E HAHA
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import java.util.Random;

public class CatAnimationManager {
    private ImageView catImageView;
    private Context context;
    private AnimationDrawable idleAnimation;
    private AnimationDrawable tapAnimation;
    private Random random = new Random();
    private boolean isIdle = true;
    
    // Animation states
    public enum AnimationState {
        IDLE, TAPPED, SLEEPING, HAPPY
    }
    
    private AnimationState currentState = AnimationState.IDLE;
    
    public CatAnimationManager(Context context, ImageView catImageView) {
        this.context = context;
        this.catImageView = catImageView;
        setupAnimations();
    }
    
    private void setupAnimations() {
        // Setup idle animation (blinking, breathing)
        idleAnimation = new AnimationDrawable();
        idleAnimation.addFrame(context.getDrawable(R.drawable.cat_idle_1), 800);
        idleAnimation.addFrame(context.getDrawable(R.drawable.cat_idle_2), 100); // blink
        idleAnimation.addFrame(context.getDrawable(R.drawable.cat_idle_1), 1200);
        idleAnimation.addFrame(context.getDrawable(R.drawable.cat_idle_3), 200); // slight movement
        idleAnimation.setOneShot(false);
        
        // Setup tap reaction animation
        tapAnimation = new AnimationDrawable();
        tapAnimation.addFrame(context.getDrawable(R.drawable.cat_happy_1), 150);
        tapAnimation.addFrame(context.getDrawable(R.drawable.cat_happy_2), 150);
        tapAnimation.addFrame(context.getDrawable(R.drawable.cat_happy_3), 200);
        tapAnimation.setOneShot(true);
        
        startIdleAnimation();
    }
    
    public void startIdleAnimation() {
        if (currentState != AnimationState.IDLE) {
            currentState = AnimationState.IDLE;
            catImageView.setImageDrawable(idleAnimation);
            idleAnimation.start();
            
            // Add subtle breathing animation
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(catImageView, "scaleX", 1.0f, 1.02f, 1.0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(catImageView, "scaleY", 1.0f, 1.02f, 1.0f);
            
            AnimatorSet breathingSet = new AnimatorSet();
            breathingSet.playTogether(scaleX, scaleY);
            breathingSet.setDuration(2000);
            breathingSet.setRepeatCount(ValueAnimator.INFINITE);
            breathingSet.setInterpolator(new AccelerateDecelerateInterpolator());
            breathingSet.start();
        }
    }
    
    public void playTapAnimation() {
        currentState = AnimationState.TAPPED;
        
        // Stop current animations
        catImageView.clearAnimation();
        
        // Tap squish effect
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(catImageView, "scaleX", 1.0f, 0.9f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(catImageView, "scaleY", 1.0f, 0.9f);
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(catImageView, "scaleX", 0.9f, 1.1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(catImageView, "scaleY", 0.9f, 1.1f);
        ObjectAnimator scaleBackX = ObjectAnimator.ofFloat(catImageView, "scaleX", 1.1f, 1.0f);
        ObjectAnimator scaleBackY = ObjectAnimator.ofFloat(catImageView, "scaleY", 1.1f, 1.0f);
        
        // Rotation for playful effect
        ObjectAnimator rotation = ObjectAnimator.ofFloat(catImageView, "rotation", 0f, 5f, -5f, 0f);
        
        AnimatorSet tapAnimSet = new AnimatorSet();
        tapAnimSet.play(scaleDownX).with(scaleDownY);
        tapAnimSet.play(scaleUpX).with(scaleUpY).after(scaleDownX);
        tapAnimSet.play(scaleBackX).with(scaleBackY).after(scaleUpX);
        tapAnimSet.play(rotation).with(scaleUpX);
        
        scaleDownX.setDuration(80);
        scaleDownY.setDuration(80);
        scaleUpX.setDuration(120);
        scaleUpY.setDuration(120);
        scaleBackX.setDuration(200);
        scaleBackY.setDuration(200);
        rotation.setDuration(400);
        
        tapAnimSet.setInterpolator(new BounceInterpolator());
        
        // Change sprite during animation
        catImageView.setImageDrawable(tapAnimation);
        tapAnimation.start();
        
        tapAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Return to idle after tap animation
                catImageView.postDelayed(() -> startIdleAnimation(), 300);
            }
        });
        
        tapAnimSet.start();
    }
    
    public void playSleepAnimation() {
        currentState = AnimationState.SLEEPING;
        catImageView.clearAnimation();
        
        // Set sleeping sprite
        catImageView.setImageResource(R.drawable.cat_sleeping);
        
        // Gentle sleeping breathing
        ObjectAnimator sleepBreathing = ObjectAnimator.ofFloat(catImageView, "alpha", 0.8f, 1.0f, 0.8f);
        sleepBreathing.setDuration(3000);
        sleepBreathing.setRepeatCount(ValueAnimator.INFINITE);
        sleepBreathing.setInterpolator(new AccelerateDecelerateInterpolator());
        sleepBreathing.start();
    }
    
    public void playUpgradeAnimation() {
        // Celebration animation for upgrades
        ObjectAnimator jump = ObjectAnimator.ofFloat(catImageView, "translationY", 0f, -50f, 0f);
        ObjectAnimator spin = ObjectAnimator.ofFloat(catImageView, "rotation", 0f, 360f);
        ObjectAnimator scaleUp = ObjectAnimator.ofFloat(catImageView, "scaleX", 1.0f, 1.3f, 1.0f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(catImageView, "scaleY", 1.0f, 1.3f, 1.0f);
        
        AnimatorSet celebrationSet = new AnimatorSet();
        celebrationSet.playTogether(jump, spin, scaleUp, scaleUpY);
        celebrationSet.setDuration(800);
        celebrationSet.setInterpolator(new DecelerateInterpolator());
        
        celebrationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startIdleAnimation();
            }
        });
        
        celebrationSet.start();
    }
    
    // Random idle behaviors
    public void triggerRandomBehavior() {
        if (currentState == AnimationState.IDLE && random.nextFloat() < 0.1f) { // 10% chance
            int behavior = random.nextInt(3);
            switch (behavior) {
                case 0:
                    playStretchAnimation();
                    break;
                case 1:
                    playTailSwishAnimation();
                    break;
                case 2:
                    playYawnAnimation();
                    break;
            }
        }
    }
    
    private void playStretchAnimation() {
        ObjectAnimator stretch = ObjectAnimator.ofFloat(catImageView, "scaleX", 1.0f, 1.2f, 1.0f);
        stretch.setDuration(1500);
        stretch.setInterpolator(new AccelerateDecelerateInterpolator());
        stretch.start();
    }
    
    private void playTailSwishAnimation() {
        ObjectAnimator swish = ObjectAnimator.ofFloat(catImageView, "rotation", 0f, 3f, -3f, 0f);
        swish.setDuration(1000);
        swish.setRepeatCount(2);
        swish.start();
    }
    
    private void playYawnAnimation() {
        catImageView.setImageResource(R.drawable.cat_yawn);
        catImageView.postDelayed(() -> startIdleAnimation(), 2000);
    }
    
    public void cleanup() {
        catImageView.clearAnimation();
        if (idleAnimation != null) {
            idleAnimation.stop();
        }
        if (tapAnimation != null) {
            tapAnimation.stop();
        }
    }
}
