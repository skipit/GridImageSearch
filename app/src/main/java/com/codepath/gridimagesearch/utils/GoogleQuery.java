package com.codepath.gridimagesearch.utils;


public class GoogleQuery {
    private static final String URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    private static final String QUERY_OPT = "&q=";
    private static final String START_OPT = "&start=";

    public String queryString = null;
    public int startOpt = -1;

    @Override
    public String toString() {
        String returnString = URL;

        if (queryString != null) {
            returnString += QUERY_OPT;
            returnString += queryString;
        } else {
            return null;
        }

        if ( startOpt >= 0 ) {
            returnString += START_OPT;
            returnString += startOpt;
        }

        return returnString;
    }
}
