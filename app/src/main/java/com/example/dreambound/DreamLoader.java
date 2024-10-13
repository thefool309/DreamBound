package com.example.dreambound;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**
 * Loader for .tmx XML map file with tile streaming (on-the-fly loading).
 */
public class DreamLoader {

    // Cache for loaded tiles
    private static Map<Long, Bitmap> tileCache = new HashMap<>();

    /**
     * Loads only the visible portion of the map.
     *
     * @param mapData             Data structure describing the TileMap.
     * @param context       Application context.
     * @param viewport      The current visible portion of the map (Rect with left, top, right, bottom coordinates).
     * @param startLayer    Index of the first layer to render.
     * @param endLayer      Index of the last layer to render.
     * @return              Bitmap of the visible map portion.
     */
    public static Bitmap createBitmap(DreamMapData mapData, Context context, Rect viewport, int startLayer, int endLayer) {
        Bitmap visibleMapImage = null;

        try {
            AssetManager assetManager = context.getAssets();

            // Create a bitmap only of the size of the visible map area
            int visibleWidth = viewport.width();
            int visibleHeight = viewport.height();
            visibleMapImage = Bitmap.createBitmap(visibleWidth, visibleHeight, Bitmap.Config.ARGB_8888);

            // Create a Canvas to draw on the bitmap
            Canvas mapCanvas = new Canvas(visibleMapImage);
            Paint paint = new Paint();

            Rect source = new Rect();
            Rect dest = new Rect();

            // Loop through layers, rows, and columns within the visible area to render only visible tiles
            for (int layerIndex = startLayer; layerIndex < endLayer; layerIndex++) {
                for (int row = viewport.top / mapData.tileheight; row < Math.min(viewport.bottom / mapData.tileheight, mapData.tileLayers.get(layerIndex).height); row++) {
                    for (int col = viewport.left / mapData.tilewidth; col < Math.min(viewport.right / mapData.tilewidth, mapData.tileLayers.get(layerIndex).width); col++) {

                        long currentGID = mapData.getGIDAtLayer(col, row, layerIndex);
                        if (currentGID == 0) continue; // Empty tile, skip

                        Bitmap tileBitmap = getTileBitmap(currentGID, mapData, assetManager);

                        if (tileBitmap != null) {
                            int localGID = mapData.getLocalID(currentGID).intValue();
                            int tilesetImageWidth = mapData.tilesets.get(mapData.getTileSetIndex(currentGID)).imageWidth;

                            // Calculate the source rectangle from the tileset based on the local GID
                            source.top = (localGID / (tilesetImageWidth / mapData.tilewidth)) * mapData.tileheight;
                            source.left = (localGID % (tilesetImageWidth / mapData.tilewidth)) * mapData.tilewidth;
                            source.bottom = source.top + mapData.tileheight;
                            source.right = source.left + mapData.tilewidth;

                            // Set destination rectangle on the visible portion of the map
                            dest.top = (row * mapData.tileheight) - viewport.top;
                            dest.left = (col * mapData.tilewidth) - viewport.left;
                            dest.bottom = dest.top + mapData.tileheight;
                            dest.right = dest.left + mapData.tilewidth;

                            // Draw the tile to the visible map area
                            mapCanvas.drawBitmap(tileBitmap, source, dest, paint);
                        }
                    }
                }
            }

        } catch (IOException e) {
            // Log in case the tileset or map files are missing
            Log.e("TMXLoader", "Error loading TMX map or tileset: " + e.getMessage(), e);
        }

        return visibleMapImage;
    }

    /**
     * Retrieves the Bitmap of the tile using its GID, either from the cache or by loading it from the tileset.
     *
     * @param currentGID    Global tile ID.
     * @param mapData             TileMapData structure.
     * @param assetManager  AssetManager for loading tileset images.
     * @return              Bitmap of the tile.
     */
    private static Bitmap getTileBitmap(long currentGID, DreamMapData mapData, AssetManager assetManager) throws IOException {
        if (tileCache.containsKey(currentGID)) {
            return tileCache.get(currentGID);  // Return from cache
        }

        Integer currentTileSetIndex = mapData.getTileSetIndex(currentGID);
        if (currentTileSetIndex == null) {
            return null;
        }

        // Load the tileset image into a Bitmap
        Bitmap tileset = BitmapFactory.decodeStream(assetManager.open(mapData.tilesets.get(currentTileSetIndex).imageFilename));

        // Cache the tileset Bitmap for future use
        tileCache.put(currentGID, tileset);
        return tileset;
    }

    /**
     * Reads an XML file from assets and returns a TileMapData structure describing its contents.
     *
     * @param filename Path to the file in assets.
     * @param context  Application context to resolve assets folder.
     * @return         Data structure containing map data.
     */
    public static DreamMapData readTMX(String filename, Context context) {
        DreamMapData mapData = null;

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XMLReader reader = parser.getXMLReader();

            // Create an instance of the TMXHandler for parsing
            DreamHandler handler = new DreamHandler();
            reader.setContentHandler(handler);

            // Open and parse the XML file from assets
            AssetManager assetManager = context.getAssets();
            reader.parse(new InputSource(assetManager.open(filename)));

            // Extract the created object
            mapData = handler.getTileMapData();

        } catch (ParserConfigurationException pce) {
            Log.e("DreamLoader", "Parser configuration error" + pce);
        } catch (SAXException se) {
            Log.e("DreamLoader", "SAX parsing error" + se);
        } catch (IOException ioe) {
            Log.e("DreamLoader", "I/O error during SAX parsing" + ioe);
        }

        return mapData;
    }
}