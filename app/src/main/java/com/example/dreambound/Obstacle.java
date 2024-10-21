package com.example.dreambound;

import android.graphics.Color;
import android.graphics.Paint;

public class Obstacle extends Tile {
    public Obstacle(float x, float y) {
        super(x, y);
        setHasCollision(true);
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    //constructor with Dynamic Size
    public Obstacle(float x, float y, float width, float height) {
        super(x, y, width, height, null);
        setHasCollision(true);
        paint = new Paint();
        paint.setColor(Color.DKGRAY);
    }
}
