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
    public static class RectangleBox {
        Point position;
        float width, height;

        public RectangleBox(float x, float y, float width, float height) {
            position = new Point(x, y);
            this.width = width;
            this.height = height;
        }
    }

    RectangleBox box;
    public Paint paint;

    public boolean isTile = false;
    public boolean noCollision = true;
    public boolean isPlayer = false;
    public boolean isCharacter = false;
    public boolean isCreature = false;
    public boolean isNPC = false;

    GameObject(float x, float y, float width, float height) {
        box = new RectangleBox(x, y, width, height);
        paint = new Paint();
        paint.setColor(Color.WHITE); //will be switched with a default of transparent
    }

    //accessors and mutators
    public void setX(float x) {
        box.position.x = x;
    }

    public void setY(float y) {
        box.position.y = y;
    }

    public float getX() { return box.position.x; }

    public float getY() { return box.position.y; }

    public boolean getIsCharacter() { return isCharacter; }

    public void setIsCharacter(boolean character) { isCharacter = character; }

    public boolean getIsNPC() { return isNPC; }

    public void setIsNPC(boolean npc) { isNPC = npc; }

    public boolean getIsTile() { return isTile; }

    public void setIsTile(boolean tile) { isTile = tile; }

    public boolean getNoCollision() { return noCollision; }

    public void setNoCollision(boolean walkable) { noCollision = walkable; }

    public boolean getIsPlayer() { return isPlayer; }

    public void setIsPlayer(boolean player) { isPlayer = player; }

    public boolean getIsCreature() { return isCreature; }

    public void setIsCreature(boolean creature) { isCreature = creature; }

    public RectangleBox getBox() {
        return box;
    }

    public float getWidth() {
        return box.width;
    }

    public float getHeight() {
        return box.height;
    }

    //change dimensions function
    public void changeDimensions(int width, int height) {
        box.width = width;
        box.height = height;
    }

    //set position functions
    public void setPosition(float _x, float _y) {
        box.position.x = _x - box.width / 2;
        box.position.y = _y - box.height / 2;
    }

    public void setPosition(Point point){
        box.position = new Point(point.x, point.y);
    }
    //draw functions
    public void draw(Canvas canvas) {
        canvas.drawRect(box.position.x, box.position.y, box.position.x + box.width, box.position.y + box.height, paint);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(box.position.x, box.position.y, box.position.x + box.width, box.position.y + box.height, paint);
    }
}

