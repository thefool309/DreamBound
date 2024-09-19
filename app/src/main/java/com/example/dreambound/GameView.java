package com.example.dreambound;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private Player player;
    private Enemies enemies;
    private SurfaceHolder surfaceHolder;
    private float targetX, targetY;
    private static final float playerMovementSpeed = 5.0f;
    private static final float enemiesDetectionRadius = 400.0f;
    private GameDataManager gameDataManager;
    private boolean isMoving;

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        gameDataManager = new GameDataManager();

        player = new Player(100, 500, 50, 100);
        enemies = new Enemies(2200, 500, 50, 100);

        gameDataManager.LoadGameState(context, player, enemies);

        targetX = player.getX();
        targetY = player.getY();
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
        if (isMoving) {
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
        }else {
            player.setX(targetX);
            player.setY(targetY);
            isMoving = false;
        }

        enemies.followPlayer(player, enemiesDetectionRadius);
        checkCollisionEnemies(player, enemies);
        checkBoundaries();
    }

    private void checkCollisionEnemies(Player player, Enemies enemies) {
        if(checkCollision(player, enemies)){
            Log.i("When Worlds Collide", "Collision Detected");
        }

    }

    private boolean checkCollision(Character player, Character target) {
        return player.getX() < target.getX() + target.getWidth() &&
                player.getX() + player.getWidth() > target.getX() &&
                player.getY() < target.getY() + target.getHeight() &&
                player.getY() + player.getHeight() > target.getY();
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
                enemies.draw(canvas);
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
                isMoving = true;
                break;
        }
        return true;
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
        gameDataManager.LoadGameState(getContext(), player, enemies);
        player.setX(player.getX());
        player.setY(player.getY());
    }

    public void pause() {
        try {
            isPlaying = false;
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Interrupted", "Interrupted while pausing");      //cleaned up exception to get more receptive feedback
        }
        gameDataManager.SaveGameState(getContext(), player, enemies);
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            Log.e("Interrupted", "Interrupted while sleeping");    //cleaned up exception to get more receptive feedback
        }
    }
}
