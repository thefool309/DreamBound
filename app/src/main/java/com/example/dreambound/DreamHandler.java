package com.example.dreambound;

import java.io.IOException;
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
     I will also attempt to implement CSV support
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
    private boolean inMap, inTileSet, inTile, inLayer, parsingData, inObjectGroup, inObject, inProperties;

    //ID of the current tile we're adding properties to.
    //this is an OFFSET from the firstGID of the tile in the tileset.
    private String currentTileID;
    private String currentObjectLayerName;
    DreamMapData.DreamTMXObject currentTMXObject;

    DreamMapData.DreamTileSet currentTileSet;
    DreamMapData.DreamLayer currentLayer;

    HashMap<String, DreamMapData.PropertiesValue> currentTileSetProperties;
    HashMap<String, DreamMapData.PropertiesValue> currentLayerProperties;

    private DreamMapData dreamMapData;


    //these fields hold the buffer and data to help
    //decode the long stream of gids in the data fields

    private char[] buffer;
    private int bufferIndex;
    private int currentX;
    private int currentY;
    public int MAX_INT_DECIMAL_LENGTH = 10;
    private String encoding;
    private StringBuilder dataBuilder;
    private String compression;

    //constructor
    public DreamHandler() {
        super();
        buffer = new char[MAX_INT_DECIMAL_LENGTH];
        bufferIndex = 0;
        currentX = 0;
        currentY = 0;
    }

    //accessor
    public DreamMapData getTileMapData() { return dreamMapData; }

    @Override
    public void startDocument() throws SAXException{
        dreamMapData = new DreamMapData();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        Log.i("Element Started", "element: " + qName);

        //startElement() takes in the XML element (tag) that is currently being parsed
        //and compares it to this conditional to decide what attributes to pull
        //Read more on SAXparser if you're interested in the specifics of how this works
        switch (localName) {        // instead of chaining if/else statements together
            case "map":             //I used a switch/case this is to improve readability
                inMap = true;
                if (!(atts.getValue("orientation").equals("orthogonal"))) {
                    throw new SAXException("Unsupported orientation. Parse Terminated.");
                }
                dreamMapData.orientation = atts.getValue("orientation");
                Log.d("Checking", dreamMapData.orientation);
                dreamMapData.height = Integer.parseInt(atts.getValue("height"));
                dreamMapData.width = Integer.parseInt(atts.getValue("width"));
                dreamMapData.tilewidth = Integer.parseInt(atts.getValue("tilewidth"));
                dreamMapData.tileheight = Integer.parseInt(atts.getValue("tileheight"));
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
                currentLayer = new DreamMapData.DreamLayer();
                currentLayer.name = atts.getValue("name");
                currentLayer.width = Integer.parseInt(atts.getValue("width"));
                currentLayer.height = Integer.parseInt(atts.getValue("height"));
                if (atts.getValue("opacity") != null) currentLayer.opacity = Double.parseDouble(atts.getValue("opacity"));
                currentLayer.tiles = new long[currentLayer.height][currentLayer.width];

                currentLayerProperties = new HashMap<String, DreamMapData.PropertiesValue>();
                break;
            case "data":
                encoding = atts.getValue("encoding");
                dataBuilder.setLength(0);
                compression = atts.getValue("compression");
                parsingData = true;
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
        switch (localName) {            //end element executes when the end of an element is reached
            case "map":
                inMap = false;
                break;
            case "data":
                if (encoding.equals("csv")) {   //check encoding
                    processCSV();
                }
                else if (encoding.equals("xml")) {
                    processXML();
                }
                else if (encoding.equals("base64")) {
                    try {
                        processBase64Data();
                    } catch(IOException e) {
                        Log.e("base64 IO exception", e.getMessage());
                    }
                }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        //characters goes of when characters are detected in-between an element
        Log.i("Characters_In_Element Ended", "characters: " + new String(ch, start, length));
        if (parsingData && encoding.equals("csv")) {
            //accumulate the character data
            dataBuilder.append(new String(ch, start, length));
        }
    }

    private void processCSV() {
        String data = dataBuilder.toString();
        //remove newlines and carriage returns
        data = data.replace("\n", "").replace("\r", "");

        String[] tiles = data.split(",");
        int width = currentLayer.width;  // Width of the layer (in tiles)
        int height = currentLayer.height; //Height of the layer (again in tiles)

        int x = 0; //column
        int y = 0; //row

        //iterate over each tile value
        for (String tile : tiles) {
            long tileValue = Long.parseLong(tile);
            //set the tile in the current layer at position
            currentLayer.setTile(x, y, tileValue);

            x++; //increment column

            //once we hit the end of the row
            if (x >= width) {
                x = 0; //reset column
                y++; //increment row
            }
        }

        //clear the StringBuilder for the next data block
        dataBuilder.setLength(0);
        parsingData = false;
    }

    private void processXML() {
        //TODO: Create XML Processor.

    }

    private void processBase64Data() throws IOException {
        String data = dataBuilder.toString();
        //TODO: Decode base64 data into a byte array


        //TODO: Compression handling


        //TODO: While loop to Iterate through each 32-bit GID (4 bytes per ID)

    }
}
