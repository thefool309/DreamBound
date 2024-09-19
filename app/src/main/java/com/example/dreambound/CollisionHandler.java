package com.example.dreambound;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.ArrayList;

public class CollisionHandler {

    Context context;
    int windowHeight;
    int windowWidth;
    int gridHeight;
    int gridWidth;

    //TODO: Data structure for collide-able entities
        ArrayList<GameObject> objects;
    //TODO: Data structure for Creature Entities
    //TODO: Constructor generate our tile maps
    CollisionHandler(Context context, ArrayList<GameObject> objects) {
        this.context = context;
        this.objects = objects;

        gridHeight = (int) (windowHeight / Constants.CHUNK_SIZE);
        gridWidth = (int) (windowWidth / Constants.CHUNK_SIZE);
        windowHeight = context.getResources().getDisplayMetrics().heightPixels;
        windowWidth = context.getResources().getDisplayMetrics().widthPixels;
    }
    //generic check between two objects for collision
    private boolean checkCollision(GameObject player, GameObject target) {
        return player.getX() < target.getX() + target.getWidth() &&
                player.getX() + player.getWidth() > target.getX() &&
                player.getY() < target.getY() + target.getHeight() &&
                player.getY() + player.getHeight() > target.getY();
    }

    void HandleCollision() {
        for (GameObject object : objects) {
            for (GameObject target : objects) {
                if (target == object) {
                    continue;
                }
                else if (target.getNoCollision() || object.getNoCollision()) {
                    continue;
                }
                else if (checkCollision(object, target)) {
                    if(object.getIsCharacter() || target.getIsCharacter()) {
                        if(object.getIsPlayer() || target.getIsPlayer()) {
                            if (object.getIsCreature() || target.getIsCreature()) {
                                collisionWithCreatureEntitiesEvent();
                            }
                            else {
                                collisionWithObjectEvent();
                            }
                        }
                        else {
                            collisionFromCreaturesToObjectsEvent();
                        }
                    }
                }
            }
        }
    }



    private void collisionWithObjectEvent() {
        Log.i("Player Collision Detected", "Collision with object event");
    }

    private void collisionWithCreatureEntitiesEvent() {
        //TODO: Implement fragment

        //TODO:Implement logic to receive boolean from intent or fragment

        //TODO:Implement Game Over logic and screen.


        Log.i("Collision Detected", "Collision with Creature event");
    }

    private void collisionFromCreaturesToObjectsEvent() {
        Log.i("CreatureEntity Collision Detected", "Collision From Creature To Objects event");
    }
}
