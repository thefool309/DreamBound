package com.example.dreambound;

import android.graphics.Color;
import android.graphics.Paint;

public class Tile extends GameObject {
    public Tile(float x, float y) {
        super(x, y, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        setIsTile(true);
        setNoCollision(true);
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }


}
