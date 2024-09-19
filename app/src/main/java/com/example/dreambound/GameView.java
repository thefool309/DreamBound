package com.example.dreambound;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;



public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private Player player;
    private CreatureEntity creatureEntity;
    private SurfaceHolder surfaceHolder;
    private float targetX, targetY;
    private static final float playerMovementSpeed = 5.0f;
    private static final float enemiesDetectionRadius = 400.0f;

    private CollisionHandler collisionHandler;
    private GameObject bush1;
    private ArrayList<GameObject> collidingEntities = new ArrayList<>();
    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        player = new Player(100, 500, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        creatureEntity = new CreatureEntity(2200, 500, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        bush1 = new Obstacle(1000, 500);


        collidingEntities.add(player);
        collidingEntities.add(creatureEntity);
        collidingEntities.add(bush1);

        targetX = player.getX();
        targetY = player.getY();
        collisionHandler = new CollisionHandler(context, collidingEntities);
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        float playerX = player.getX();
        float playerY = player.getY();
        float deltaX = targetX - playerX;
        float deltaY = targetY - playerY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > playerMovementSpeed) {
            float stepX = playerMovementSpeed * (deltaX / distance);
            float stepY = playerMovementSpeed * (deltaY / distance);
            player.setX(playerX + stepX);
            player.setY(playerY + stepY);
        } else {
            player.setX(targetX);
            player.setY(targetY);
        }

        creatureEntity.followPlayer(player, enemiesDetectionRadius);
        //checkCollisionEnemies(player, creatureEntity);
        collisionHandler.HandleCollision();
        checkBoundaries();
    }


    private void checkBoundaries() {
        if (player.getX() < 0) {
            player.setX(0);
        } else if (player.getX() + player.getWidth() > getWidth()) {
            player.setX(getWidth() - player.getWidth());
        }

        if (player.getY() < 0) {
            player.setY(0);
        } else if (player.getY() + player.getHeight() > getHeight()) {
            player.setY(getHeight() - player.getHeight());
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                player.draw(canvas);
                creatureEntity.draw(canvas);
                bush1.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                targetX = event.getX();
                targetY = event.getY();
                break;
        }
        return true;
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Interrupted", "Interrupted while pausing");      //cleaned up exception to get more receptive feedback
        }
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            Log.e("Interrupted", "Interrupted while sleeping");    //cleaned up exception to get more receptive feedback
        }
    }
}
