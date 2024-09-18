package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameObject {

    //member variables
    public static class Position {
        public float x, y;

        public Position(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
    public Position position;
    public float width, height;
    public Paint paint;

    GameObject(float x, float y, float width, float height) {
        position = new Position(x, y);
        this.width = width;
        this.height = height;
        paint = new Paint();
        paint.setColor(Color.GREEN); //will be switched with a default of transparent
    }

    //accessors and mutators
    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Position getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    //set position function
    public void setPosition(float _x, float _y) {
        position.x = _x - width / 2;
        position.y = _y - height / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(position.x, position.y, position.x + width, position.y + height, paint);
    }
}


