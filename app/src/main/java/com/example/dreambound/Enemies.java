package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Enemies {
    private float x, y;
    private float enemiesWidth, enemiesHeight;
    private Paint enemiesPaint;
    private static final float enemiesSpeed = 3.0f;

    public Enemies(float x, float y, float _enemiesWidth, float _enemiesHeight)  {
        this.x = x;
        this.y = y;
        this.enemiesWidth = _enemiesWidth;
        this.enemiesHeight = _enemiesHeight;

        enemiesPaint = new Paint();
        enemiesPaint.setColor(Color.BLUE);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getEnemiesWidth() {
        return enemiesWidth;
    }

    public float getEnemiesHeight() {
        return enemiesHeight;
    }

    public void setPosition(float _x, float _y) {
        this.x = _x - enemiesWidth / 2;
        this.y = _y - enemiesHeight / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + enemiesWidth, y + enemiesHeight, enemiesPaint);;
    }

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