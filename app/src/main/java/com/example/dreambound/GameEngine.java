package com.example.dreambound;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;

import android.graphics.Bitmap;


public class GameEngine {
    private Context context;
    private Player player;
    private ArrayList<CreatureEntity> creaturesLoadedIn = new ArrayList();
    private ArrayList<Obstacle> obstacles = new ArrayList();
    private ArrayList<GameObject> collideables = new ArrayList<>();
    private ArrayList<GameObject> allObjects = new ArrayList<>();
    private ArrayList<GameObject> staticObjects = new ArrayList<>();

    public GameEngine(Context context) {
        this.context = context;
        createObjects();
    }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public ArrayList<GameObject> getAllObjects() { return allObjects; }
    public void setAllObjects(ArrayList<GameObject> allObjects) { this.allObjects = allObjects; }

    public ArrayList<GameObject> getStaticObjects() { return staticObjects; }

    public ArrayList<GameObject> getCollisionObjects() { return collideables; }

    public ArrayList<Obstacle> getObstacles() { return obstacles; }


    public ArrayList<CreatureEntity> getCreaturesLoadedIn() { return creaturesLoadedIn; }


    private void createObjects() {
        // here we will parse json data to create objects needed elsewhere in the game

        CreatureEntity enemy1;
        CreatureEntity enemy2;
        Obstacle bush1;
        Obstacle wall1;
        Obstacle wall2;
        Obstacle wall3;
        Obstacle wall4;
        Obstacle wall5;
        Obstacle wall6;
        Obstacle wall7;
        Obstacle wall8;
        Tile walkOnMe1;
        Tile walkOnMe2;
        Tile walkOnMe3;
        Tile walkOnMe4;
        Tile walkOnMe5;
        Tile walkOnMe6;


        player = new Player(100, 800, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        enemy1 = new CreatureEntity(550, 650, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        enemy2 = new CreatureEntity(2200, 800, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        bush1 = new Obstacle(1000, 500);
        wall1 = new Obstacle(0, 0, 2400, 32);
        wall2= new Obstacle (2368,0, 32 , 1080);
        wall3 = new Obstacle(0,922, 2400, 32);
        wall4 = new Obstacle(0,0, 32, 1080);
        wall5 = new Obstacle(250,0,32 , 150);
        wall6 = new Obstacle(250,400,32 , 600);
        wall7 = new Obstacle(600,0,80 , 550);
        wall8 = new Obstacle(1550,200,550 , 550);

        //Bitmap for GRASS
        Bitmap grass = BitmapFactory.decodeResource(context.getResources(), R.drawable._grass);
        walkOnMe1 = new Tile(0, 0,2400,1000, grass);


        //allObjects.add(player);
        allObjects.add(enemy1);
        allObjects.add(enemy2);

        creaturesLoadedIn.add(enemy1);
        creaturesLoadedIn.add(enemy2);

        allObjects.add(bush1);
        obstacles.add(bush1);
        allObjects.add(wall1);
        obstacles.add(wall1);
        allObjects.add(wall2);
        obstacles.add(wall2);
        allObjects.add(wall3);
        obstacles.add(wall3);
        allObjects.add(wall4);
        obstacles.add(wall4);
        allObjects.add(wall5);
        obstacles.add(wall5);
        allObjects.add(wall6);
        obstacles.add(wall6);
        allObjects.add(wall7);
        obstacles.add(wall7);
        allObjects.add(wall8);
        obstacles.add(wall8);

        allObjects.add(walkOnMe1);

        upcastObjectsIntoArrays();


    }
    private void upcastObjectsIntoArrays() {
        for (GameObject object : allObjects){
            if (object.getHasCollision() && object.getCanMove()){
                collideables.add(object);
            }
            else if (object.getHasCollision() && !object.getCanMove()){
                staticObjects.add(object);
            }
        }
    }
}
