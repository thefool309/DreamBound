package com.example.dreambound;

public class Tile extends GameObject {
    public Tile(float x, float y) {
        super(x, y, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        setIsTile(true);
        setIsWalkable(true);
    }


}
