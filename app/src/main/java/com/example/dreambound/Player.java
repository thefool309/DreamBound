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


    public void playerMovementDetection(float targetX, float targetY) {
        if (getIsMoving()) {

            float playerX = getX();
            float playerY = getY();
            float deltaX = targetX - playerX;
            float deltaY = targetY - playerY;
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > getVelocity()) {
                float stepX = getVelocity() * (deltaX / distance);
                float stepY = getVelocity() * (deltaY / distance);
                setX(playerX + stepX);
                setY(playerY + stepY);
            } else {
                setX(targetX);
                setY(targetY);
            }
        }else {
            setX(targetX);
            setY(targetY);
            isMoving = false;
        }
    }

    //draw
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), paint);
    }
}