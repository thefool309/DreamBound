package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player extends Character{


    public Player(float _x, float _y, float _playerWidth, float _playerHeight) {
        super(_x, _y, _playerWidth, _playerHeight);
        this.x = _x;
        this.y = _y;
        this.width = _playerWidth;
        this.height = _playerHeight;


    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint);
    }
}