package com.example.dreambound;

import android.graphics.Color;
import android.graphics.Paint;

public class GameObject {
    //member variables
    public float x, y;
    public float width, height;
    public Paint paint;

    GameObject(float x, float y, float width, float height) {
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
}


