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
        canvas.drawRect(hitbox.x, hitbox.y, hitbox.x + hitbox.width, hitbox.y + hitbox.height, paint);
    }
    //enemy movement ai
    public void followPlayer(Player player, float detectionRadius) {
        float deltaX = player.getX() - this.hitbox.x;
        float deltaY = player.getY() - this.hitbox.y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance < detectionRadius) {
            float stepX = enemiesSpeed * (deltaX / distance);
            float stepY = enemiesSpeed * (deltaY / distance);
            this.hitbox.x += stepX;
            this.hitbox.y += stepY;
        }
    }
}