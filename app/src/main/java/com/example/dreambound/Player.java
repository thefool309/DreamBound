package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import java.io.Serializable;

public class Player extends gameCharacter implements Serializable {
    float playerMovementSpeed = 5.0f;
    //constructor
    public Player(float _x, float _y, float _playerWidth, float _playerHeight) {
        super(_x, _y, _playerWidth, _playerHeight);
        setIsPlayer(true);
        initPaint(Color.RED);
        setCanMove();
    }
    //accessors and mutators
    public void setPlayerMovementSpeed(float _playerMovementSpeed) {
        this.playerMovementSpeed = _playerMovementSpeed;
    }

    public float getPlayerMovementSpeed() {
        return playerMovementSpeed;
    }

    //draw
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), paint);
    }
}