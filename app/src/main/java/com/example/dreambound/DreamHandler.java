package com.example.dreambound;

import java.util.HashMap;

import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DreamHandler extends DefaultHandler {
    /*
    * This is the SAX2 XML parser that interprets the input the TMX
    * file and creates a TileMapData object based on that
    * David handled a lot of the work for me here too, I will simply be implementing map object loading
    * I am rebuilding the code from the ground up to make some design change differences, and for the sake
    * of learning how it works.
    */

    /*
     * BASED ON CODE FROM: davidmi/Android-TMX-Loader https://github.com/davidmi/Android-TMX-Loader
     * TMX LOADER FOR ANDROID Second beta release
     * 0.8.1 Written by David Iserovich
     * Big thank you to David who did a lot of the work for me.
     */

    //Member Fields

    //markers for which tag we're in
    private boolean inMap, inTileSet, inTile, inLayer, inData, inObjectGroup, inObject, inProperties;

    //ID of the current tile we're adding properties to.
    //this is an OFFSET from the firstGID of the tile in the tileset.
    private String currentTileID;
    private String currentObjectLayerName;
    DreamMapData.DreamTMXObject currentTMXObject;

    DreamMapData.DreamTileSet currentTileSet;
    DreamMapData.DreamLayer currentLayer;

    HashMap<String, DreamMapData.PropertiesValue> currentTileSetProperties;
    HashMap<String, DreamMapData.PropertiesValue> currentLayerProperties;

    private DreamMapData data;


    //these fields hold the buffer and data to help
    //decode the long stream of gids in the data fields

    private char[] buffer;
    private int bufferIndex;
    private int currentX;
    private int currentY;
    public int MAX_INT_DECIMAL_LENGTH = 10;


    //constructor
    public DreamHandler() {
        super();
        buffer = new char[MAX_INT_DECIMAL_LENGTH];
        bufferIndex = 0;
        currentX = 0;
        currentY = 0;
    }

    //accessor
    public DreamMapData getTileMapData() { return data; }

    @Override
    public void startDocument() throws SAXException{
        data = new DreamMapData();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        Log.i("Element Started", "element: " + qName);
        switch (localName) {        // instead of chaining if/else statements together I used a switch/case
            case "map":             //this is to improve readability
                inMap = true;
                if (!(atts.getValue("orientation").equals("orthogonal"))) {
                    throw new SAXException("Unsupported orientation. Parse Terminated.");
                }
                data.orientation = atts.getValue("orientation");
                Log.d("Checking", data.orientation);
                data.height = Integer.parseInt(atts.getValue("height"));
                data.width = Integer.parseInt(atts.getValue("width"));
                data.tilewidth = Integer.parseInt(atts.getValue("tilewidth"));
                data.tileheight = Integer.parseInt(atts.getValue("tileheight"));
                break;
            case "tileset":
                inTileSet = true;
                currentTileSet = new  DreamMapData.DreamTileSet();
                currentTileSet.firstGID = Integer.parseInt(atts.getValue("firstgid"));
                currentTileSet.tileWidth = Integer.parseInt(atts.getValue("tilewidth"));
                currentTileSet.tileHeight = Integer.parseInt(atts.getValue("tileheight"));
                currentTileSet.name = atts.getValue("name");
                currentTileSetProperties = new HashMap<String, DreamMapData.PropertiesValue>();
                inTileSet = true;
                break;
            case "layer":
                inLayer = true;
                break;
            case "data":
                inData = true;
                break;
            case "objectgroup":
                inObjectGroup = true;
                break;
            case "object":
                inObject = true;
                break;
            case "properties":
                inProperties = true;
                break;
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.i("Element Ended", "element: " + qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        Log.i("Characters_In_Element Ended", "characters: " + new String(ch, start, length));
    }

}
