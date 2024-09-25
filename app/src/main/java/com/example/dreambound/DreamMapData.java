package com.example.dreambound;

import java.util.ArrayList;
import java.util.HashMap;

public class DreamMapData {
    /*A Data structure to hold the tilemap data.
    * This can be used to construct the tilemap
    * and the object layers.
    */

    /*
    * BASED ON CODE FROM: davidmi/Android-TMX-Loader https://github.com/davidmi/Android-TMX-Loader
    * TMX LOADER FOR ANDROID Second beta release
    * 0.8.1 Written by David Iserovich
    * Big thank you to David who did a lot of the work for me.
    * I will be taking David's Android-TMX-Loader and
    * adding the missing implementation of TMX Object Layers
    */

    static class DreamTileSet {
        public String name;

        public int firstGID;
        public int tileWidth, tileHeight;

        public String ImageFilename;
        public int imageWidth, imageHeight;

        public HashMap<String, HashMap<String, String>> properties;
    }

    static class DreamLayer {
        public String name;

        public long[][] tiles;

        public int width, height;
        public double opacity;

        HashMap<String, String> properties;
    }

    static class DreamTMXObject{
        public String name;
        public String type;
        int x, y;
        int width, height;

        String group;
    }

    public long  getGIDAtLayer(int x, int y, int layerIndex) {

        return ((layers.get(layerIndex).tiles[y][x]));
    }

    public Long getLocalID(long GID){
        long currentFirstGID = GID;
        Long localId = null; //null for error checking.
        for (int i = tilesets.size() - 1; i >= 0; i--) {
            currentFirstGID = tilesets.get(i).firstGID;
            if (currentFirstGID <= GID) {
                localId = new Long(GID - currentFirstGID);
            }
        }

        return localId;
    }

    public Integer getTileSetIndex(long GID) {
        //search for a specific tilesTileIndex
        Integer tileSetIndex = null;
        long currentFirstGID = GID;
        for(int i = tilesets.size() - 1; i >= 0; i--) {
            currentFirstGID = tilesets.get(i).firstGID;
            if (currentFirstGID <= GID) {
                tileSetIndex = i;
            }
        }
        // If the GID is not valid return null
        return tileSetIndex;
    }

    public String name;
    public int	height, width;
    public int	tilewidth, tileheight;
    public String orientation; // Must be "orthogonal", for now.

    public ArrayList<DreamTileSet> tilesets; // <tileset.name, tileset>
    public ArrayList<DreamTMXObject> objects; // We can search by several parameters (not just name) so a linear search is probably best
    public ArrayList<DreamLayer> layers;

    public DreamMapData(){
        tilesets = new ArrayList<DreamTileSet>();
        objects = new ArrayList<DreamTMXObject>();
        layers = new ArrayList<DreamLayer>();
    }

}
