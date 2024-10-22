package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import java.io.Serializable;

public class Player extends gameCharacter implements Serializable {



    //constructor
    public Player(float _x, float _y, float _playerWidth, float _playerHeight) {
        super(_x, _y, _playerWidth, _playerHeight);
        setIsPlayer(true);
        initPaint(Color.RED);
        setCanMove();
        setVelocity(5.00f);
    }
    //accessors and mutators


    //draw
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), paint);
    }
}