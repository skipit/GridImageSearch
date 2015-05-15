package com.codepath.gridimagesearch.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.FilterPreferences;

public class FilterFragment extends DialogFragment {

    public interface FilterFragmentListener {
        void onReceivePreferences(FilterPreferences preferences);
    }

    private Spinner spTypes;
    private Spinner spColors;
    private Spinner spSizes;
    private EditText etSite;
    private Button btSave;
    private Button btCancel;

    public FilterFragment() {
        // Empty constructor required for DialogFragment
    }




    public static FilterFragment newInstance() {
        FilterFragment frag = new FilterFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_filters, container);

        FilterPreferences pref = (FilterPreferences) getArguments().getParcelable("prefs");

        spTypes = (Spinner) view.findViewById(R.id.spImageTypes);
        spColors = (Spinner) view.findViewById(R.id.spColor);
        spSizes = (Spinner) view.findViewById(R.id.spImageSize);
        etSite = (EditText) view.findViewById(R.id.etSite);
        btSave = (Button) view.findViewById(R.id.btSave);


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = spSizes.getSelectedItem().toString();
                String color = spColors.getSelectedItem().toString();
                String type = spTypes.getSelectedItem().toString();
                String site = etSite.getText().toString();

                FilterPreferences preferences = new FilterPreferences(
                        (size.equals("any")?null:size),
                        (color.equals("any")?null:color),
                        (type.equals("any")?null:type),
                        ((site.length()==0)?null:site)
                );

                Log.i(getActivity().toString(), "Preferences: " + preferences);
                FilterFragmentListener listener = (FilterFragmentListener) getActivity();
                listener.onReceivePreferences(preferences);
                dismiss();
            }
        });

        btCancel = (Button) view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /* Fill up the preferences */
        if ( pref != null ) {
            ArrayAdapter<String> arrayAdapter = (ArrayAdapter) spTypes.getAdapter();
            int position = arrayAdapter.getPosition(pref.type);
            spTypes.setSelection(position);

            arrayAdapter = (ArrayAdapter) spColors.getAdapter();
            position = arrayAdapter.getPosition(pref.color);
            spColors.setSelection(position);

            arrayAdapter = (ArrayAdapter) spSizes.getAdapter();
            position = arrayAdapter.getPosition(pref.size);
            spSizes.setSelection(position);

            etSite.setText(pref.site);
        }

        getDialog().setTitle("Set Filters");
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}