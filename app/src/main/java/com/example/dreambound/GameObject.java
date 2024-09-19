package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class GameObject {

    //member variables
    public static class Hitbox {
        public float x, y;
        float width, height;

        public Hitbox(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

        }
    }

    public Hitbox hitbox;

    public Paint paint;

    GameObject(float x, float y, float width, float height) {
        hitbox = new Hitbox(x, y,width,height);
        paint = new Paint();
        paint.setColor(Color.GREEN); //will be switched with a default of transparent
    }

    //accessors and mutators
    public void setX(float x) {
        hitbox.x = x;
    }

    public void setY(float y) {
        hitbox.y = y;
    }

    public float getX() {
        return hitbox.x;
    }

    public float getY() {
        return hitbox.y;
    }

    public Hitbox getPosition() {
        return hitbox;
    }

    public float getWidth() {
        return hitbox.width;
    }


    public float getHeight() {
        return hitbox.height;
    }

    //set position function
    public void setPosition(float _x, float _y) {
        hitbox.x = _x - hitbox.width / 2;
        hitbox.y = _y - hitbox.height / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(hitbox.x, hitbox.y, hitbox.x + hitbox.width, hitbox.y + hitbox.height, paint);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(hitbox.x, hitbox.y, hitbox.x + hitbox.width, hitbox.y + hitbox.height, paint);
    }
}


