package com.codepath.gridimagesearch.utils;


import com.codepath.gridimagesearch.models.FilterPreferences;

public class GoogleQuery {

    private static final String QUERY_OPT = "&q=";
    private static final String START_OPT = "&start=";
    private static final String IMG_SIZE_OPT = "&imgsz=";
    private static final String IMG_COLOR_OPT = "&imgcolor=";
    private static final String IMG_TYPE_OPT = "&imgtype=";
    private static final String SITE_OPT = "&as_sitesearch=";


    private static final int querySize = 8;
    private static final String URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + querySize;



    public static String getQuery(FilterPreferences preferences, String queryString) {

        String returnString = URL;

        if (queryString != null) {
            returnString += QUERY_OPT;
            returnString += queryString;
        } else {
            return null;
        }

        if ( preferences.color != null ) {
            returnString += IMG_COLOR_OPT;
            returnString += preferences.color;
        }

        if ( preferences.size != null ) {
            returnString += IMG_SIZE_OPT;
            returnString += preferences.size;
        }

        if ( preferences.type != null ) {
            returnString += IMG_TYPE_OPT;
            returnString += preferences.type;
        }

        if ( preferences.site != null ) {
            returnString += SITE_OPT;
            returnString += preferences.site;
        }

        return returnString;
    }
}
