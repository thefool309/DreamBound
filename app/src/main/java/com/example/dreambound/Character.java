package com.example.dreambound;

import android.graphics.Color;
import android.graphics.Paint;
import java.io.Serializable;

public class Character implements Serializable {
    protected float x, y;
    protected float width, height;
    protected transient Paint paint;
    //Constructor
    Character(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        paint = new Paint();
        paint.setColor(Color.RED);
    }
    //accessors and mutators
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

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    //set position function
    public void setPosition(float _x, float _y) {
        this.x = _x - width / 2;
        this.y = _y - height / 2;
    }

    public void initPaint(int color) {
        paint = new Paint();
        paint.setColor(color);
    }
}

