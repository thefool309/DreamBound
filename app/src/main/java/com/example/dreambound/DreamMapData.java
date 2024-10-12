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

        public String imageFilename;
        public int imageWidth, imageHeight;

        public HashMap<String, DreamProperty> properties;

        public void AddProperty(String name, DreamProperty property){ properties.put(name, property); }
    }

    static class DreamLayer {  //data for each individual layer
        public String name;
        // By tmx design, the actual tilemap is just a set of gids.
        // The array can be initialized to new int[width][height]
        //tiles are not stored as objects for the sake of memory efficiency
        public long[][] tiles;
        public int width, height;
        public double opacity;

        public HashMap<String, DreamProperty> properties;

        public void setTile(int x, int y, long GID) {
            tiles[x][y] = GID;
        }
        private void AddProperty(String name, DreamProperty property) {
            properties.put(name, property);
        }

    }

    static class DreamProperty {    //Data for the Properties on an object
        public String type;
        public String value;
        public String name;

        DreamProperty(String type, String value, String name) {
            this.type = type;
            this.value = value;
            this.name = name;
        }
    }

    static class DreamTMXObject{    //a TMXObject for storing in object layers and collision information
        public String name;
        public String type;
        float x, y;
        float width, height;

        public HashMap<String, DreamProperty> properties; //HashMap for properties as you only need the name, type, and value

        DreamTMXObject(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            name = "";
        }

        DreamTMXObject(float x, float y, float width, float height, String name) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
        }

        public void AddProperty(String name, DreamProperty property) {
            properties.put(name, property);
        }
    }                                                      //I will be using the name as a "key" here, and the type and value as the data

    static class DreamObjectGroup {      //a layer of objects usually with the same properties
        public String name;
        public int index;
        public ArrayList<DreamTMXObject> objects;
        public HashMap<String, DreamProperty> properties; //<name, property>

        DreamObjectGroup(String name, int index) {
            this.name = name;
            this.index = index;
            objects = new ArrayList<>();
        }

        private void AddProperty(String name, DreamProperty property) { properties.put(name, property); }

        public void AddObject(DreamTMXObject object) {
            objects.add(object);
        }
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
    public ArrayList<DreamLayer> tileLayers;
    public ArrayList<DreamObjectGroup> objectGroups;

    //constructor
    public DreamMapData(){
        tilesets = new ArrayList<DreamTileSet>();
        tileLayers = new ArrayList<DreamLayer>();
        objectGroups = new ArrayList<DreamObjectGroup>();
    }

}
