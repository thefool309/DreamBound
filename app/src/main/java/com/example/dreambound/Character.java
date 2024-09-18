package com.example.dreambound;


public class Character extends GameObject {

    public static class Stats{
        int Attack = 10;
        int Defense = 10;
        int SpAttack = 10;
        int SpDefense = 10;
        int Health = 50;
    }

    Stats stats;
    //Constructor
    Character(float x, float y, float width, float height) {
        super(x, y, width, height);

        stats = new Stats(); //default character Stats
    }
}



