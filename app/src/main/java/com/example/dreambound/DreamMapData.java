package com.example.dreambound;

import java.util.ArrayList;
import java.util.HashMap;

public class DreamMapData {
    /*A Data structure to hold the tilemap data.
    * This can be used to construct the tilemap
    * and the object layers.
    * David handled a lot of the work for me here, I will simply be implementing map object loading
     * I am rebuilding the code from the ground up to make some design change differences however.
    */

    /*
    * BASED ON CODE FROM: davidmi/Android-TMX-Loader https://github.com/davidmi/Android-TMX-Loader
    * TMX LOADER FOR ANDROID Second beta release
    * 0.8.1 Written by David Iserovich
    * I will be taking David's Android-TMX-Loader and
    * adding the missing implementation of TMX Object Layers
    */

    static class DreamTileSet { //data for the tileset as a whole
        public String name;

        public int firstGID;
        public int tileWidth, tileHeight;

        public String ImageFilename;
        public int imageWidth, imageHeight;

        public HashMap<String, HashMap<String, String>> properties;
    }

    static class DreamLayer {  //data for each individual layer
        public String name;
        // By tmx design, the actual tilemap is just a set of gids.
        // The array can be initialized to new int[width][height]
        //tiles are not stored as objects for the sake of memory efficiency
        public long[][] tiles;

        public int width, height;
        public double opacity;

        public HashMap<String, String> properties;
    }

    static class ObjectPropertiesValue {    //Data for the Properties on an object
        public String type;
        public String value;
    }

    static class DreamTMXObject{    //a TMXObject for storing in object layers and collision information
        public String name;
        public String type;
        int x, y;
        int width, height;

        public HashMap<String, ObjectPropertiesValue> properties; //HashMap for properties as you only need the name, type, and value
    }                                                      //I will be using the name as a "key" here, and the type and value as the data

    static class DreamObjectLayer{      //a layer of objects usually with the same properties
        public String name;
        public int index;
        public ArrayList<DreamTMXObject> objects;
    }

    //accessors
    public long  getGIDAtLayer(int x, int y, int layerIndex) {
        return ((tileLayers.get(layerIndex).tiles[y][x]));
    }

    public Long getLocalID(long GID){       //get the local id of a tile
        long currentFirstGID = GID;
        Long localId = null; //null for error checking.
        for (int i = tilesets.size() - 1; i >= 0; i--) {
            currentFirstGID = tilesets.get(i).firstGID;
            if (currentFirstGID <= GID) {
                localId = GID - currentFirstGID;
            }
        }

        return localId;
    }

    public Integer getTileSetIndex(long GID) {  //search for a specific tile's Tilemap's Index
        Integer tileSetIndex = null;
        long currentFirstGID = GID;
        for(int i = tilesets.size() - 1; i >= 0; i--) {
            currentFirstGID = tilesets.get(i).firstGID;
            if (currentFirstGID <= GID) {
                tileSetIndex = i;
            }
        }

        return tileSetIndex; // If the GID is not valid return null
    }

    public Integer getTileSetIndex(String name){        //get a tileset index by name
        Integer tileSetIndex = null; //returns null for error checking

        for(int i = tilesets.size() - 1; i >= 0; i--) {
            if(tilesets.get(i).name.equals(name)){
                tileSetIndex = i;
                break;
            }
        }
        return tileSetIndex;
    }

    public Integer getLayerIndex(String name){      //get a layer index by name
        Integer layerIndex = null; //returns null for error checking
        for(int i = 0; i < tileLayers.size(); i++) {
            if(tileLayers.get(i).name.equals(name)){
                layerIndex = i;
            }
        }
        return layerIndex;
    }

    //member fields
    public String name;
    public int	height, width;
    public int	tilewidth, tileheight;
    public String orientation; //Must be "orthogonal", per David.

    public ArrayList<DreamTileSet> tilesets; //<tileset.name, tileset> was a comment included in davids code
    public ArrayList<DreamTMXObject> objects; //We can search by several parameters (not just name) so a linear search is probably best
    public ArrayList<DreamLayer> tileLayers;
    public ArrayList<DreamObjectLayer> objectLayers;

    //constructor
    public DreamMapData(){
        tilesets = new ArrayList<DreamTileSet>();
        objects = new ArrayList<DreamTMXObject>();
        tileLayers = new ArrayList<DreamLayer>();
        objectLayers = new ArrayList<DreamObjectLayer>();
    }

}
