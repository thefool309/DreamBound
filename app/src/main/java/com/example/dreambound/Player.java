package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player {
    private float x, y;
    private float playerWidth, playerHeight;
    private Paint playerPaint;

    public Player(float _x, float _y, float _playerWidth, float _playerHeight) {
        this.x = _x;
        this.y = _y;
        this.playerWidth = _playerWidth;
        this.playerHeight = _playerHeight;

        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);
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

    public float getPlayerWidth() {
        return playerWidth;
    }

    public float getPlayerHeight() {
        return playerHeight;
    }

    public void setPosition(float _x, float _y) {
        this.x = _x - playerWidth / 2;
        this.y = _y - playerHeight / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + playerWidth, y + playerHeight, playerPaint);
    }
}