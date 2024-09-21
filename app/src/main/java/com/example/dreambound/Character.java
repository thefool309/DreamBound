package com.example.dreambound;

import java.io.Serializable;

public class Character extends GameObject implements Serializable {
    //Stats class
    public static class Stats implements Serializable {
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
        setHasCollision(true);
        stats = new Stats(); //default character Stats
    }
}



