package com.example.dreambound;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;

public class CollisionHandler {
    private static final int CHUNK_SIZE = 64;

    Player player;
    Context context = null;
    int windowHeight;
    int windowWidth;
    int gridHeight;
    int gridWidth;

    //TODO: Data structure for collide-able entities
        ArrayList<GameObject> objects = new ArrayList<GameObject>();
    //TODO: Data structure for Creature Entities
         ArrayList<CreatureEntity> creatureEntities = new ArrayList<CreatureEntity>();
    //TODO: Constructor generate our tile maps
    CollisionHandler(Context context, Player player, ArrayList<GameObject> objects, ArrayList<CreatureEntity> creatureEntities) {
        this.context = context;
        this.player = player;
        this.objects = objects;
        this.creatureEntities = creatureEntities;

        gridHeight = windowHeight / 16;
        gridWidth = windowWidth / 16;
        windowHeight = context.getResources().getDisplayMetrics().heightPixels;
        windowWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    private boolean checkCollision(GameObject player, GameObject target) {
        return player.getX() < target.getX() + target.getWidth() &&
                player.getX() + player.getWidth() > target.getX() &&
                player.getY() < target.getY() + target.getHeight() &&
                player.getY() + player.getHeight() > target.getY();
    }

    void HandleCollision() {
        checkCollisionWithObjects();
        checkCollisionWithCreatureEntities();
        checkCollisionFromCreaturesToObjects();
    }

    void checkCollisionWithObjects() throws NullPointerException {
        boolean collision = false;
        for (GameObject object : objects) {
            collision = checkCollision(player, object);
            if (collision) {
                collisionWithObjectEvent();
            }
        }
    }

    private void collisionWithObjectEvent() {
        Log.i("Collision Detected", "Collision with object event");
    }

    void checkCollisionWithCreatureEntities() {
        boolean collision = false;
        for (CreatureEntity creatureEntity : creatureEntities) {
            collision = checkCollision(player, creatureEntity);
            if (collision) {
                collisionWithCreatureEntitiesEvent();
            }
        }
    }

    void checkCollisionFromCreaturesToObjects() {
        boolean collision = false;
        for (CreatureEntity creatureEntity : creatureEntities) {
            for (GameObject object : objects) {
                collision = checkCollision(creatureEntity, object);
                if (collision){
                    collisionFromCreaturesToObjectsEvent();
                }
            }
        }
    }

    private void collisionWithCreatureEntitiesEvent() {
        //TODO: Implement fragment

        //TODO:Implement logic to receive boolean from intent or fragment

        //TODO:Implement Game Over logic and screen.


        Log.i("Collision Detected", "Collision with Creature event");
    }

    private void collisionFromCreaturesToObjectsEvent() {
        Log.i("Collision Detected", "Collision From Creature To Objects event");
    }
}
