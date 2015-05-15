package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.SearchResultAdapter;
import com.codepath.gridimagesearch.fragments.FilterFragment;
import com.codepath.gridimagesearch.models.FilterPreferences;
import com.codepath.gridimagesearch.models.SearchResult;
import com.codepath.gridimagesearch.utils.EndlessScrollListener;
import com.codepath.gridimagesearch.utils.GoogleQuery;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Filter;


public class SearchActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, FilterFragment.FilterFragmentListener {

    private GridView gvSearchResults;
    private SearchResultAdapter searchResultAdapter;
    private FilterPreferences preferences;
    private String queryString;
    private ArrayList<SearchResult> searchResults;
    private int currentOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResults = new ArrayList<SearchResult>();
        currentOffset = 0;

        /* Set up the GridView with the adapter */
        searchResultAdapter = new SearchResultAdapter(this, searchResults);
        StaggeredGridView gvSearchResults = (StaggeredGridView) findViewById(R.id.gvSearchResults);
        gvSearchResults.setAdapter(searchResultAdapter);
        gvSearchResults.setOnItemClickListener(this);

        gvSearchResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                getMoreResults(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        preferences = new FilterPreferences();
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
                queryString = query;
                currentOffset = 0;
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

    // Append more data into the adapter
    private void getMoreResults(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        currentOffset = currentOffset + GoogleQuery.querySize;

        if(currentOffset > GoogleQuery.maxQuerySize) {
            return;
        }
        // only load more if not at the end
        getResults();
    }


    public void showSearchFilter(MenuItem item) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("prefs", preferences);

        FilterFragment filterFrag = FilterFragment.newInstance();
        filterFrag.setArguments(bundle);
        filterFrag.show(getSupportFragmentManager(), "frag_filters");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSearchFilter(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getResults() {

        String resultQuery = GoogleQuery.getQuery(  this.preferences,
                                                    this.queryString,
                                                    currentOffset);
        Log.i("DEBUG", "Query: " + resultQuery);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(resultQuery, null, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response ) {
                    Log.i("DEBUG", response.toString());

                    try {
                        if ( currentOffset == 0 ) {
                            searchResults.clear();
                        }
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

    @Override
    public void onReceivePreferences(FilterPreferences preferences) {
            this.preferences = preferences;
            /* Reset Offset as search is performed again */
            currentOffset = 0;
            getResults();
    }
}
