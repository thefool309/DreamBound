package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;


public class Player extends Character{

    //constructor
    public Player(float _x, float _y, float _playerWidth, float _playerHeight) {
        super(_x, _y, _playerWidth, _playerHeight);
        paint.setColor(Color.RED);
    }
    //draw
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(hitbox.x, hitbox.y, hitbox.x + hitbox.width, hitbox.y + hitbox.height, paint);
    }
}