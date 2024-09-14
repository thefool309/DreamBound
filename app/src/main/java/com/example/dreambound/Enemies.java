package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Enemies extends Character {
    private static final float enemiesSpeed = 3.0f;
    //constructor
    public Enemies(float x, float y, float _enemiesWidth, float _enemiesHeight)  {
        super(x, y, _enemiesWidth, _enemiesHeight);
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    //draw
    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint);
    }
    //enemy movement ai
    public void followPlayer(Player player, float detectionRadius) {
        float deltaX = player.getX() - this.x;
        float deltaY = player.getY() - this.y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance < detectionRadius) {
            float stepX = enemiesSpeed * (deltaX / distance);
            float stepY = enemiesSpeed * (deltaY / distance);
            this.x += stepX;
            this.y += stepY;
        }
    }
}