package com.example.dreambound;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;



public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread = new Thread(this);
    private boolean isPlaying;
    private Player player;
    private CreatureEntity creatureEntity;
    private SurfaceHolder surfaceHolder;
    private float targetX, targetY;
    private static final float enemiesDetectionRadius = 400.0f;

    private long startTime, loopTime;

    private GameDataManager gameDataManager;
    private boolean isMoving;

    GameEngine gameEngine;

    private CollisionHandler collisionHandler;
    private Obstacle bush1;
    private Tile walkOnMe1;
    private Tile walkOnMe2;

    private DreamMapData mapData;    // The map data loaded from the TMX file
    private Bitmap visibleMap;       // The bitmap for the visible portion of the map
    private Rect viewport;           // The current viewport (visible area of the map)
    private int startLayer = 0;      // First layer to render
    private int endLayer;            // Last layer to render based on map layers
    int playerX;
    int playerY;


    private ArrayList<CreatureEntity> creatures = new ArrayList<>();
    private ArrayList<GameObject> collidables = new ArrayList<>();
    private ArrayList<GameObject> staticObjects = new ArrayList<>();
    private ArrayList<GameObject> allObjects = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        //start engine
        if (gameEngine == null) {
            startEngineAndPullData();
        }

        //load game if there is one
        gameDataManager = new GameDataManager();
        gameDataManager.LoadGameState(context, player, creatures);
        targetX = player.getX();
        targetY = player.getY();
        collisionHandler = new CollisionHandler(context, collidables, staticObjects);
    }

    // Initialize the map and set up the viewport
    private void initMap(Context context) {
        // Load the tile map data from the assets folder
        mapData = DreamLoader.readTMX("map.tmx", context);

        // Define the layers to be rendered
        endLayer = mapData.tileLayers.size();

        // Set an initial viewport (visible map area)
        viewport = new Rect(playerX, playerY, playerX + getWidth(), playerY + getHeight());

        // Load the visible portion of the map
        loadVisibleMap(context);
    }

    // Load the visible portion of the map based on the viewport
    private void loadVisibleMap(Context context) {
        visibleMap = DreamLoader.createBitmap(mapData, context, viewport, startLayer, endLayer);
    }

    // Method to update the viewport as the player moves
    public void updateViewport(int newPlayerX, int newPlayerY) {
        playerX = newPlayerX;
        playerY = newPlayerY;

        // Update the viewport based on the player's new position
        viewport.set(playerX, playerY, playerX + getWidth(), playerY + getHeight());

        // Load the updated visible map
        loadVisibleMap(getContext());

        // Redraw the view
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the visible portion of the map on the canvas

    }


    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            control();
        }
    }

    private void startEngineAndPullData(){
        if (gameEngine == null) {
            gameEngine = new GameEngine();
            creatures = gameEngine.getCreaturesLoadedIn();
            staticObjects = gameEngine.getStaticObjects();
            allObjects = gameEngine.getAllObjects();
            collidables = gameEngine.getCollisionObjects();
            player = gameEngine.getPlayer();
        }
    }

    private void createObjects() {
        player = new Player(100, 500, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        creatureEntity = new CreatureEntity(2200, 500, Constants.CHUNK_SIZE, Constants.CHUNK_SIZE);
        bush1 = new Obstacle(1000, 500);
        walkOnMe1 = new Tile(1000, 400);
        walkOnMe2 = new Tile(1000, 600);
    }



    private void update() {
        if (isMoving) {
            startTime = SystemClock.uptimeMillis();
            float playerX = player.getX();
            float playerY = player.getY();
            float deltaX = targetX - playerX;
            float deltaY = targetY - playerY;
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > player.getPlayerMovementSpeed()) {
                float stepX = player.getPlayerMovementSpeed() * (deltaX / distance);
                float stepY = player.getPlayerMovementSpeed() * (deltaY / distance);
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
        
        for (CreatureEntity entity: creatures) {
            entity.followPlayer(player);
        }
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
        initMap(this.getContext());
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                if (visibleMap != null) {
                    canvas.drawBitmap(visibleMap, 0, 0, null);
                }
                else {
                canvas.drawColor(Color.BLACK);
                }


                for (GameObject object : allObjects){
                    object.draw(canvas);
                }
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
        gameDataManager.LoadGameState(getContext(), player, creatures);
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
        gameDataManager.SaveGameState(getContext(), player, creatures);
    }

    private void control() {
        loopTime = SystemClock.uptimeMillis() - startTime;
        //pausing here to make sure we update the right number of times per second
        if (loopTime < Constants.DELAY) {
            try {
                Thread.sleep(Constants.DELAY - loopTime);
            } catch (InterruptedException e) {
                Log.e("Interrupted", "Interrupted while sleeping");
            }
        }
    }
}
