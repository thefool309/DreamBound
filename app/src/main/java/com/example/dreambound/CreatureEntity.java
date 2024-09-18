package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CreatureEntity extends Character {
    private static final float enemiesSpeed = 3.0f;
    //constructor
    public CreatureEntity(float x, float y, float _enemiesWidth, float _enemiesHeight)  {
        super(x, y, _enemiesWidth, _enemiesHeight);
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    //draw
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(position.x, position.y, position.x + width, position.y + height, paint);
    }
    //enemy movement ai
    public void followPlayer(Player player, float detectionRadius) {
        float deltaX = player.getX() - this.position.x;
        float deltaY = player.getY() - this.position.y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance < detectionRadius) {
            float stepX = enemiesSpeed * (deltaX / distance);
            float stepY = enemiesSpeed * (deltaY / distance);
            this.position.x += stepX;
            this.position.y += stepY;
        }
    }
}