package com.example.dreambound;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private Player player;
    private SurfaceHolder surfaceHolder;
    private float targetX, targetY;
    private static final float movementSpeed = 5.0f;

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        player = new Player(100, 100, 50, 100);
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
        float playerX = player.getX();
        float playerY = player.getY();
        float deltaX = targetX - playerX;
        float deltaY = targetY - playerY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > movementSpeed) {
            float stepX = movementSpeed * (deltaX / distance);
            float stepY = movementSpeed * (deltaY / distance);
            player.setX(playerX + stepX);
            player.setY(playerY + stepY);
        } else {
            player.setX(targetX);
            player.setY(targetY);
        }

        checkBoundaries();
    }

    private void checkBoundaries() {
        if (player.getX() < 0) {
            player.setX(0);
        } else if (player.getX() + player.getPlayerWidth() > getWidth()) {
            player.setX(getWidth() - player.getPlayerWidth());
        }

        if (player.getY() < 0) {
            player.setY(0);
        } else if (player.getY() + player.getPlayerHeight() > getHeight()) {
            player.setY(getHeight() - player.getPlayerHeight());
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                player.draw(canvas);
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
            e.printStackTrace();
        }
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
