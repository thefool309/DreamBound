package com.example.dreambound;

public class Obstacle extends Tile {
    public Obstacle(float x, float y) {
        super(x, y);
        setIsWalkable(false);
    }
}
