package com.example.dreambound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameObject {




    public static class Point {
        public float x;
        public float y;
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    //member variables
    public static class Hitbox {
        Point position;
        float width, height;

        public Hitbox(float x, float y, float width, float height) {
            this.position.x = x;
            this.position.y = y;
            this.width = width;
            this.height = height;

        }
    }

    public Hitbox hitbox;
    public Paint paint;

    public boolean isTile = false;
    public boolean isWalkable = true;
    public boolean isPlayer = false;
    public boolean isCharacter = false;
    public boolean isCreature = false;
    public boolean isNPC = false;

    GameObject(float x, float y, float width, float height) {
        hitbox = new Hitbox(x, y,width,height);
        paint = new Paint();
        paint.setColor(Color.GREEN); //will be switched with a default of transparent
    }

    //accessors and mutators
    public void setX(float x) {
        hitbox.position.x = x;
    }

    public void setY(float y) {
        hitbox.position.y = y;
    }

    public float getX() {
        return hitbox.position.x;
    }

    public float getY() { return hitbox.position.y; }

    public boolean isCharacter() { return isCharacter; }

    public void setIsCharacter(boolean character) { isCharacter = character; }

    public boolean isNPC() { return isNPC; }

    public void setIsNPC(boolean npc) { isNPC = npc; }

    public boolean isTile() { return isTile; }

    public void setIsTile(boolean tile) { isTile = tile; }

    public boolean isWalkable() { return isWalkable; }

    public void setIsWalkable(boolean walkable) { isWalkable = walkable; }

    public boolean isPlayer() { return isPlayer; }

    public void setIsPlayer(boolean player) { isPlayer = player; }

    public boolean isCreature() { return isCreature; }

    public void setIsCreature(boolean creature) { isCreature = creature; }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public float getWidth() {
        return hitbox.width;
    }

    public float getHeight() {
        return hitbox.height;
    }

    //set position functions
    public void setPosition(float _x, float _y) {
        hitbox.position.x = _x - hitbox.width / 2;
        hitbox.position.y = _y - hitbox.height / 2;
    }

    public void setPosition(Point point){
        hitbox.position = new Point(point.x, point.y);
    }
    //draw functions
    public void draw(Canvas canvas) {
        canvas.drawRect(hitbox.position.x, hitbox.position.y, hitbox.position.x + hitbox.width, hitbox.position.y + hitbox.height, paint);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(hitbox.position.x, hitbox.position.y, hitbox.position.x + hitbox.width, hitbox.position.y + hitbox.height, paint);
    }
}


