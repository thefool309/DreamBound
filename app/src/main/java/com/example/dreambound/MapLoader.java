package com.example.dreambound;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.graphics.Bitmap;

import android.widget.ImageView;
import android.widget.Toast;

import davidiserovich.TMXLoader.*;

//comment for change to commit
public class MapLoader extends Activity {
    private Context context;
    ImageView mapView;
    String fileName;
    private int screenWidth;
    private int screenHeight;

    public MapLoader(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //start parsing the .tmx
        TileMapData mapData = TMXLoader.readTMX(fileName, this);

        mapView = (ImageView) findViewById(R.id.mapImage);

        //create bitmap
        Bitmap mapImage = TMXLoader.createBitmap(mapData, this, 0, 3);

        if (mapImage != null) {
            mapView.setImageBitmap(mapImage);
        }

        //else problem loading map
        else {
            Toast errorMessage = Toast.makeText(getApplicationContext(), "Map could not be loaded", Toast.LENGTH_LONG);
            errorMessage.show();
        }


    }


    // public Bitmap renderMap(Context context, int screenWidth, int screenHeight) {
    //    this.screenWidth = screenWidth;
    //     this.screenHeight = screenHeight;
    //
    //     // Load the TMX map data
    //     TileMapData mapData = TMXLoader.readTMX(fileName, context);
    //
    //     // Create the map bitmap
    //     Bitmap mapImage = TMXLoader.createBitmap(mapData, context, 0, mapData.layers.size());
    //
    //     return mapImage;
    // }
}

