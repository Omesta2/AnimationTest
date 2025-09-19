package com.aregbesolamuhammad.animationtest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint; // Import Paint
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class AnimationClass extends View {
    MainActivity thing; // Or just Context thing;
    PokemonImage pokemonImage;
    PokemonImage enemyPokemon;
    private Paint mPaint; // Add a Paint object
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public AnimationClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        thing = (MainActivity) context;
        pokemonImage = new PokemonImage(thing, 0, 0, 128, 128, 20, 0, "Ampharos");
        enemyPokemon = new PokemonImage(thing, screenWidth, 0, 128, 128, -20, 0, "Baxcalibur");

        mPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
         pokemonImage.left = 0;
         pokemonImage.top = 200;
         pokemonImage.right = w / 4;
         pokemonImage.bottom = (h/4) + 200;
        enemyPokemon.left = screenWidth - (w/4);
        enemyPokemon.top = 200; // Or 0
        enemyPokemon.right = screenWidth; // Or w
        enemyPokemon.bottom = (h/4) + 200;

    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pokemonImage != null) {
            pokemonImage.update(true);
            pokemonImage.drawImage(canvas, mPaint);

            enemyPokemon.update(false);
            enemyPokemon.drawImage(canvas, mPaint);
        }
        invalidate();
    }
}
