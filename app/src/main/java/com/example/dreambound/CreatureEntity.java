package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import java.io.Serializable;

public class CreatureEntity extends Character implements Serializable {
    private static final float enemiesSpeed = 3.0f;
    //constructor
    public CreatureEntity(float x, float y, float _enemiesWidth, float _enemiesHeight)  {
        super(x, y, _enemiesWidth, _enemiesHeight);
        setIsCreature(true);
        setNoCollision(false);
        initPaint(Color.BLUE);
    }

    //draw
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(box.position.x, box.position.y, box.position.x + box.width, box.position.y + box.height, paint);
    }
    //enemy movement ai
    public void followPlayer(Player player, float detectionRadius) {
        float deltaX = player.getX() - this.getX();
        float deltaY = player.getY() - this.getY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance < detectionRadius) {
            float stepX = enemiesSpeed * (deltaX / distance);
            float stepY = enemiesSpeed * (deltaY / distance);
            this.setX(this.getX() + stepX);
            this.setY(this.getY() + stepY);
        }
    }
}