package com.codepath.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResult implements Serializable {
    public int width;
    public int height;
    public int tbwidth;
    public int tbheight;
    public String url;
    public String title;
    public String tbUrl;

    public SearchResult(JSONObject jsonObj) throws JSONException {
        width = jsonObj.getInt("width");
        height = jsonObj.getInt("height");
        tbwidth = jsonObj.getInt("tbWidth");
        tbheight = jsonObj.getInt("tbHeight");
        url = jsonObj.getString("url");
        tbUrl = jsonObj.getString("tbUrl");
        title = jsonObj.getString("title");

    }

    public static ArrayList<SearchResult> parseJSONArray(JSONArray jsonResultArray) {
        ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>();
        for ( int i = 0; i < jsonResultArray.length(); i++ ) {
            try {
                SearchResult result = new SearchResult(jsonResultArray.getJSONObject(i));
                searchResults.add(result);
            } catch ( JSONException e ) {
                e.printStackTrace();
            }
        }

        return searchResults;
    }

    @Override
    public String toString() {
        return   "["
                + " width: " + width
                + " height: " + height
                + " tbWidth: " + tbwidth
                + " tbHeight: " + tbheight
                + " url: " + url
                + " tbUrl: " + tbUrl
                + " title: " + title
                + "]";
     }
}
