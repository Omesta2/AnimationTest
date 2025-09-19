package com.aregbesolamuhammad.animationtest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.core.content.ContextCompat;

public class PokemonImage extends RectF {
    private int dX, dY;
    private String name;
    private Context mContext;
    int x, y;

    public PokemonImage(Context context, float left, float top, float right, float bottom, int dX, int dY, String name) {
        super(left, top, right, bottom);
        this.mContext = context;
        this.dX = dX;
        this.dY = dY;
        this.name = name;
        this.x = (int) left;
        this.y = (int) top;
    }

    public int getdY() {
        return dY;
    }

    public void setdY(int dY) {
        this.dY = dY;
    }

    public int getdX() {
        return dX;
    }

    public void setdX(int dX) {
        this.dX = dX;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    // In PokemonImage.java

    // Add these constants to your PokemonImage class, or pass them in
// if they can vary per Pokemon type.
    private static final int NORMAL_ADVANCE_SPEED_PLAYER = 20; // Example speed
    private static final int RETREAT_SPEED_PLAYER = 5;      // Slower retreat
    private static final int NORMAL_ADVANCE_SPEED_ENEMY = -20; // Example speed (negative for right-to-left)
    private static final int RETREAT_SPEED_ENEMY = -5;       // Slower retreat (still negative)

    private boolean isRetreating = false; // State to track if the Pokemon is retreating

    public void update(boolean isPlayer) {
        // 1. Apply current movement
        offset(dX, 0); // Assuming dY is 0 for horizontal-only movement

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        float halfwayPoint = (float) screenWidth / 2;

        if (isPlayer) {
            // --- Player Logic (moves left-to-right, retreats right-to-left) ---
            if (!isRetreating) {
                // Player is advancing
                this.dX = NORMAL_ADVANCE_SPEED_PLAYER; // Ensure advancing speed
                if (right > halfwayPoint) {
                    // Reached halfway, start retreating
                    isRetreating = true;
                    this.dX = -RETREAT_SPEED_PLAYER; // Set to slower retreat speed, moving left
                } else if (right > screenWidth) {
                    // Hit the absolute right edge (shouldn't happen if halfway triggers retreat)
                    // but as a fallback, ensure it turns back.
                    isRetreating = true;
                    this.dX = -RETREAT_SPEED_PLAYER;
                    offsetTo(screenWidth - width(), top); // Correct position
                }
            } else {
                // Player is retreating
                this.dX = -RETREAT_SPEED_PLAYER; // Ensure retreating speed
                if (left < 0) {
                    // Reached the starting edge (left screen edge), stop retreating or reset
                    isRetreating = false;
                    this.dX = NORMAL_ADVANCE_SPEED_PLAYER; // Go back to normal advance speed
                    offsetTo(0, top); // Correct position
                }
            }

            // General boundary check for player (in case something unexpected happens)
            if (left < 0 && dX < 0) { // Moving left and out of bounds left
                offsetTo(0, top);
                isRetreating = false; // Reset state
                this.dX = NORMAL_ADVANCE_SPEED_PLAYER;
            } else if (right > screenWidth && dX > 0) { // Moving right and out of bounds right
                offsetTo(screenWidth - width(), top);
                isRetreating = true; // Should be retreating if this far
                this.dX = -RETREAT_SPEED_PLAYER;
            }

        } else {
            // --- Enemy Logic (moves right-to-left, retreats left-to-right) ---
            if (!isRetreating) {
                // Enemy is advancing
                this.dX = NORMAL_ADVANCE_SPEED_ENEMY; // Ensure advancing speed (negative)
                if (left < halfwayPoint) {
                    // Reached halfway (from the right), start retreating
                    isRetreating = true;
                    this.dX = -RETREAT_SPEED_ENEMY; // Positive, slower retreat speed, moving right
                    // Note: -RETREAT_SPEED_ENEMY because RETREAT_SPEED_ENEMY is negative
                } else if (left < 0) {
                    // Hit the absolute left edge (shouldn't happen if halfway triggers retreat)
                    isRetreating = true;
                    this.dX = -RETREAT_SPEED_ENEMY;
                    offsetTo(0, top); // Correct position
                }
            } else {
                // Enemy is retreating
                this.dX = -RETREAT_SPEED_ENEMY; // Ensure retreating speed (positive)
                if (right > screenWidth) {
                    // Reached the starting edge (right screen edge), stop retreating or reset
                    isRetreating = false;
                    this.dX = NORMAL_ADVANCE_SPEED_ENEMY; // Go back to normal advance speed
                    offsetTo(screenWidth - width(), top); // Correct position
                }
            }

            // General boundary check for enemy
            if (right > screenWidth && dX > 0) { // Moving right and out of bounds right
                offsetTo(screenWidth - width(), top);
                isRetreating = false; // Reset state
                this.dX = NORMAL_ADVANCE_SPEED_ENEMY;
            } else if (left < 0 && dX < 0) { // Moving left and out of bounds left
                offsetTo(0, top);
                isRetreating = true; // Should be retreating if this far
                this.dX = -RETREAT_SPEED_ENEMY;
            }
        }
    }

    // Optional: Method to reset the Pokemon's state if needed (e.g., for a new round)
    public void resetMovementState(boolean isPlayer) {
        this.isRetreating = false;
        if (isPlayer) {
            this.dX = NORMAL_ADVANCE_SPEED_PLAYER;
            // Optionally reset position too: offsetTo(0, this.top);
        } else {
            this.dX = NORMAL_ADVANCE_SPEED_ENEMY;
            // Optionally reset position too:
            // int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            // offsetTo(screenWidth - width(), this.top);
        }
    }



    public void drawImage(Canvas canvas, Paint paint) {
        String drawableName = name.toLowerCase();
        int resourceId = mContext.getResources().getIdentifier(drawableName, "drawable", mContext.getPackageName());
        Drawable pokemonDrawable = ContextCompat.getDrawable(mContext, resourceId);
        pokemonDrawable.setBounds((int) left, (int) top, (int) right, (int) bottom);
        pokemonDrawable.draw(canvas);

    }
}
