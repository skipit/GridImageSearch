package com.codepath.gridimagesearch.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.support.v7.widget.SearchView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapter.SearchResultAdapter;
import com.codepath.gridimagesearch.model.SearchResult;
import com.codepath.gridimagesearch.utils.EndlessScrollListener;
import com.codepath.gridimagesearch.utils.GoogleQuery;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private GridView gvSearchResults;
    private SearchResultAdapter searchResultAdapter;
    private int querySize = 8;

    GoogleQuery gQuery;

    private ArrayList<SearchResult> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        gQuery = new GoogleQuery();

        searchResults = new ArrayList<SearchResult>();

        /* Set up the GridView with the adapter */
        searchResultAdapter = new SearchResultAdapter(this, searchResults);
        GridView gvSearchResults = (GridView) findViewById(R.id.gvSearchResults);
        gvSearchResults.setAdapter(searchResultAdapter);
        gvSearchResults.setOnItemClickListener(this);

        gvSearchResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                gQuery.queryString = query;
                getResults();
                searchItem.collapseActionView();
                searchView.onActionViewCollapsed();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

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

    private void getResults() {
        String googleQuery = gQuery.toString();

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //if ( view.getId() == R.id.gvSearchResults) {
            Intent i = new Intent(SearchActivity.this, ImageDetailsActivity.class);
            SearchResult result = searchResults.get(position);
            i.putExtra("search_result", result);
            startActivity(i);

        //}
    }
}
