package com.example.dreambound;

import java.util.HashMap;

import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DreamHandler extends DefaultHandler {
    /*
     This is the SAX2 XML parser that interprets the TMX
     file and creates a TileMapData object based on that
     I will be implementing map objectgroup loading
     I am rebuilding the code David made
      from the ground up to make some design changes,
     and for the sake of learning how it works.
    */

    /*
     BASED ON CODE FROM: davidmi/Android-TMX-Loader https://github.com/davidmi/Android-TMX-Loader
     TMX LOADER FOR ANDROID Second beta release
     0.8.1 Written by David Iserovich
     Big thank you to David who did a lot of the work for me.
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

        //startElement() takes in the XML element (tag) that is currently being parsed
        //and compares it to this conditional
        //Read more on SAXparser if you're interested in the specifics of how this works
        switch (localName) {        // instead of chaining if/else statements together
            case "map":             //I used a switch/case this is to improve readability
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
            case "image":
                currentTileSet.imageFilename = atts.getValue("source");
                currentTileSet.imageWidth = Integer.parseInt(atts.getValue("width"));
                currentTileSet.imageHeight = Integer.parseInt(atts.getValue("height"));
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
