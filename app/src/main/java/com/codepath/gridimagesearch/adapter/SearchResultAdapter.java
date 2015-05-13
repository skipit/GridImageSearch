package com.codepath.gridimagesearch.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.model.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {

    private static class ViewHolder {
        ImageView ivPhoto;
        TextView tvCaption;
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
            viewHolder.tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext())
                .load(searchResult.tbUrl)
                .into(viewHolder.ivPhoto);
        viewHolder.tvCaption.setText(Html.fromHtml( searchResult.title ));

        return convertView;
    }
}
