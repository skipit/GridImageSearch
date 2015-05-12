package com.codepath.gridimagesearch.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapter.SearchResultAdapter;
import com.codepath.gridimagesearch.model.SearchResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText etSearchQuery;
    private GridView gvSearchResults;
    private Button btSearch;
    private SearchResultAdapter searchResultAdapter;
    private int querySize = 8;

    private ArrayList<SearchResult> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        etSearchQuery = (EditText) findViewById(R.id.etQuery);
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setOnClickListener(this);

        searchResults = new ArrayList<SearchResult>();

        /* Set up the GridView with the adapter */
        searchResultAdapter = new SearchResultAdapter(this, searchResults);
        GridView gvSearchResults = (GridView) findViewById(R.id.gvSearchResults);
        gvSearchResults.setAdapter(searchResultAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleSearchClick() {
        String query = etSearchQuery.getText().toString();
        String googleQuery = "https://ajax.googleapis.com/ajax/services/search/images"
                  + "?v=1.0"
                  + "&q=" + query
                  + "&rsz=" +  querySize;

        Log.i("DEBUG", "Query: " + googleQuery);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(googleQuery, null, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response ) {
                    Log.i("DEBUG", response.toString());

                    try {
                        searchResults.clear();
                        searchResults.addAll(SearchResult
                                                .parseJSONArray(response
                                                        .getJSONObject("responseData")
                                                        .getJSONArray("results")) );
                        searchResultAdapter.notifyDataSetChanged();
                    } catch ( JSONException e ) {
                        e.printStackTrace();
                    }

                    Log.i("DEBUG", searchResults.toString());

                }

                public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable) {
                    Log.i("DEBUG", "JsonHttpResponseHandler:onFailure triggered with statusCode="+statusCode);
                }
            }
        );
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId()) {
            case R.id.btSearch:
                handleSearchClick();
                break;
        }
    }
}
