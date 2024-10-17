package com.example.dreambound;
import android.content.Context;
import android.graphics.Bitmap;
import davidiserovich.TMXLoader.*;




public class MapLoader {
    private Context context;
    private String fileName;
    private int screenWidth;
    private int screenHeight;

    public MapLoader(Context context, String fileName) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        this.fileName = fileName;
    }

    public Bitmap renderMap(int screenWidth, int screenHeight) {
        if (context == null) {
            throw new IllegalStateException("Context is not initialized");
        }
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Load the TMX map data using the correct path
        TileMapData mapData = TMXLoader.readTMX(fileName, context);

        // Create the map bitmap
        Bitmap mapImage = TMXLoader.createBitmap(mapData, context, 1, 6);

        // Optionally, resize the mapImage to fit screen dimensions
        if (mapImage != null) {
            mapImage = Bitmap.createScaledBitmap(mapImage, screenWidth, screenHeight, true);
        }

        return mapImage;
    }
}
