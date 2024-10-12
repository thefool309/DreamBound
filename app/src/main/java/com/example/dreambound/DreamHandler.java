package com.example.dreambound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import android.util.Base64;
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
    private boolean inMap, inTileSet, inTile, inLayer, parsingData, inObjectGroup, inObject, inProperties, inProperty, inImage;

    //ID of the current tile we're adding properties to.
    //this is an OFFSET from the firstGID of the tile in the tileset.
    private String currentTileID;
    DreamMapData.DreamTMXObject currentTMXObject;

    DreamMapData.DreamTileSet currentTileSet;
    DreamMapData.DreamLayer currentLayer;
    DreamMapData.DreamObjectGroup currentObjectGroup;

    HashMap<String, DreamMapData.DreamProperty> currentTileSetProperties;
    HashMap<String, DreamMapData.DreamProperty> currentLayerProperties;

    private DreamMapData mapData;

    private int currentColumn = 0, currentRow = 0;

    //these fields hold the buffer and data to help
    //decode the long stream of gids in the data fields

    public int MAX_INT_DECIMAL_LENGTH = 10;
    private String encoding;
    private StringBuilder dataBuilder;
    private String compression;


    //constructor
    public DreamHandler() {
        super();
    }

    //accessor
    public DreamMapData getTileMapData() { return mapData; }

    @Override
    public void startDocument() throws SAXException{
        mapData = new DreamMapData();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.i("Element Started", "element: " + qName);

        //startElement() takes in the XML element (tag) that is currently being parsed
        //and compares it to this conditional to decide what attributes to pull
        //Read more on SAX2 parser if you're interested in the specifics of how this works
        switch (localName) {        // instead of chaining if/else statements together
            case "map":             //I used a switch/case this is to improve readability
                inMap = true;
                if (!(attributes.getValue("orientation").equals("orthogonal"))) {
                    throw new SAXException("Unsupported orientation. Parse Terminated.");
                }
                mapData.orientation = attributes.getValue("orientation");
                Log.d("Checking", mapData.orientation);
                mapData.height = Integer.parseInt(attributes.getValue("height"));
                mapData.width = Integer.parseInt(attributes.getValue("width"));
                mapData.tilewidth = Integer.parseInt(attributes.getValue("tilewidth"));
                mapData.tileheight = Integer.parseInt(attributes.getValue("tileheight"));
                break;
            case "tileset":
                inTileSet = true;
                currentTileSet = new  DreamMapData.DreamTileSet();
                currentTileSet.firstGID = Integer.parseInt(attributes.getValue("firstgid"));
                currentTileSet.tileWidth = Integer.parseInt(attributes.getValue("tilewidth"));
                currentTileSet.tileHeight = Integer.parseInt(attributes.getValue("tileheight"));
                currentTileSet.name = attributes.getValue("name");
                currentTileSetProperties = new HashMap<String, DreamMapData.DreamProperty>();
                inTileSet = true;
                break;
            case "image":
                inImage = true;
                currentTileSet.imageFilename = attributes.getValue("source");
                currentTileSet.imageWidth = Integer.parseInt(attributes.getValue("width"));
                currentTileSet.imageHeight = Integer.parseInt(attributes.getValue("height"));
                break;
            case "layer":
                inLayer = true;
                currentLayer = new DreamMapData.DreamLayer();
                currentLayer.name = attributes.getValue("name");
                currentLayer.width = Integer.parseInt(attributes.getValue("width"));
                currentLayer.height = Integer.parseInt(attributes.getValue("height"));
                if (attributes.getValue("opacity") != null) currentLayer.opacity = Double.parseDouble(attributes.getValue("opacity"));
                currentLayer.tiles = new long[currentLayer.height][currentLayer.width];

                currentLayerProperties = new HashMap<String, DreamMapData.DreamProperty>();
                break;
            case "data":
                parsingData = true;
                encoding = attributes.getValue("encoding");
                dataBuilder.setLength(0);
                compression = attributes.getValue("compression");
                break;
            case "tile":
                inTile = true;
                //get the GID
                long gid = Long.parseLong(attributes.getValue("gid"));
                //set tile
                currentLayer.setTile(currentColumn, currentRow, gid);
                //increment column
                currentColumn++;
                //if column is >= width
                if (currentColumn >= currentLayer.width){
                    currentColumn = 0;  //set column to zero
                    currentRow++;       //increment row
                }
            case "objectgroup":
                //TODO: implement object group logic
                inObjectGroup = true;   //create new object group
                currentObjectGroup = new DreamMapData.DreamObjectGroup(attributes.getValue("name"), Integer.parseInt(attributes.getValue("id")));
                break;
            case "object":
                //object logic
                inObject = true;
                //create object
                currentTMXObject = new DreamMapData.DreamTMXObject(Float.parseFloat(attributes.getValue("x")),
                                                                    Float.parseFloat(attributes.getValue("y")),
                                                                    Float.parseFloat(attributes.getValue("width")),
                                                                    Float.parseFloat(attributes.getValue("height")),
                                                                    attributes.getValue("name"));
                //add object to dreamMapData.objects
                currentObjectGroup.objects.add(currentTMXObject);
                break;
            case "properties":
                // properties logic
                inProperties = true;
                break;
            case "property":
                inProperty = true;
                if (inObject) {
                    currentTMXObject.properties.putIfAbsent(attributes.getValue("name"),
                                                    new DreamMapData.DreamProperty(attributes.getValue("type"),
                                                                                   attributes.getValue("value"),
                                                                                   attributes.getValue("name")));
                }
                else if (inLayer) {
                    currentLayer.properties.putIfAbsent(attributes.getValue("name"),
                                                        new DreamMapData.DreamProperty(attributes.getValue("type"),
                                                                                       attributes.getValue("value"),
                                                                                       attributes.getValue("name")));
                }
                else if (inObjectGroup) {
                    currentObjectGroup.properties.putIfAbsent(attributes.getValue("name"),
                                                              new DreamMapData.DreamProperty(attributes.getValue("type"),
                                                                                             attributes.getValue("value"),
                                                                                             attributes.getValue("name")));
                }
                break;
            default:
                Log.e("Unexpected value: ", "Unexpected value: " + localName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.i("Element Ended", "element: " + qName);
        switch (localName) {            //end element executes when the end of an element is reached
            case "map":
                inMap = false;
                break;
            case "tileset":
                inTileSet = false;
                break;
            case "data":
                if (encoding.equals("csv")) {   //check encoding
                    try {
                        processCSV();
                    } catch (IOException e) {
                        Log.e("CSV Parsing Error", "error" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                else if (encoding.equals("base64")) {
                    try {
                        processBase64Data();
                    } catch(IOException e) {
                        Log.e("base64 IO exception", "error" + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                parsingData = false;
                break;
            case "image":
                inImage = false;
                break;
            case "layer":
                inLayer = false;
                currentLayer = null;
                break;
            case "tile":
                inTile = false;
                break;
            case "objectgroup":
                inObjectGroup = false;  //add object group to map data
                mapData.objectGroups.add(currentObjectGroup);
                currentObjectGroup = null;
                break;
            case "object":
                inObject = false;
                currentTMXObject = null;
                break;
            case "properties":
                inProperties = false;
                break;
            case "property":
                inProperty = false;
                break;

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        //characters goes of when characters are detected in-between an element
        Log.i("Characters_In_Element Ended", "characters: " + new String(ch, start, length));
            //accumulate the character data
            dataBuilder.append(new String(ch, start, length));
    }

    private void processCSV() throws IOException {
        String data = dataBuilder.toString();
        //remove newlines and carriage returns
        data = data.replace("\n", "").replace("\r", "");

        String[] tiles = data.split(",");
        int width = currentLayer.width;  // Width of the layer (in tiles)
        int height = currentLayer.height; //Height of the layer (again in tiles)

        //iterate over each tile value
        for (String tile : tiles) {
            long tileValue = Long.parseLong(tile);
            //set the tile in the current layer at position
            currentLayer.setTile(currentColumn, currentRow, tileValue);

            currentColumn++; //increment column

            //once we hit the end of the row
            if (currentColumn >= width) {
                currentColumn = 0; //reset column
                currentRow++; //increment row
            }
        }

        //clear the StringBuilder for the next data block
        dataBuilder.setLength(0);
        parsingData = false;
    }

    private void processBase64Data() throws IOException {
        String data = dataBuilder.toString();
        //Decode base64 data into a byte array
        byte[] decodedBytes = Base64.decode(data, Base64.DEFAULT);    //must use android.util.Base64 for compatibility

        //Compression handling
        int numberOfBytes = decodedBytes.length;
        if (compression.equals("gzip")) {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(decodedBytes));
        }
        else if (compression.equals("zlib")) {
            Inflater inflater = new Inflater();
            inflater.setInput(decodedBytes, 0, numberOfBytes);
        }

        ByteBuffer buffer = ByteBuffer.wrap(decodedBytes); //buffer for decoded bytes

        //While loop to Iterate through each 32-bit GID (4 bytes per ID)
        while (buffer.remaining() >= 4) {
            long gid = buffer.getLong(); //take in GID

            currentLayer.setTile(currentColumn, currentRow, gid);
            currentColumn++;
            if (currentColumn >= currentLayer.width){
                currentColumn = 0;
                currentRow++;
            }

        }
        dataBuilder.setLength(0);
        parsingData = false;
    }
}
