package com.example.dreambound;

import android.graphics.Paint;

public class Character extends GameObject {
    //Stats class
    public static class Stats{
        int Attack = 10;
        int Defense = 10;
        int SpAttack = 10;
        int SpDefense = 10;
        int Health = 50;
    }
    //stats variable
    Stats stats;
    //Constructor
    Character(float x, float y, float width, float height) {
        super(x, y, width, height);
        setIsCharacter(true);
        setNoCollision(false);
        stats = new Stats(); //default character Stats
    }

    public void initPaint(int color) {
        paint = new Paint();
        paint.setColor(color);
    }
}



