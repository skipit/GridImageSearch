package com.codepath.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {

    private static class ViewHolder {
        ImageView ivPhoto;
    }


    public SearchResultAdapter(Context context, List<SearchResult> objects) {
        super(context, R.layout.search_result_view, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResult searchResult = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null ) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_view,
                    parent,
                    false);
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext())
                .load(searchResult.tbUrl)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.ivPhoto);

        return convertView;
    }
}
