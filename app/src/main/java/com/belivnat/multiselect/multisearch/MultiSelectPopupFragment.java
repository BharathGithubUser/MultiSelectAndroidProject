package com.belivnat.multiselect.multisearch;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.belivnat.multiselect.R;

import java.util.ArrayList;

public class MultiSelectPopupFragment extends DialogFragment implements MultiSelectAdapter.MultiSelectClickedListener {
    ArrayList<MultiSelectModel> multiSelectList;
    ArrayList<MultiSelectModel> multiSelectFinalList;
    RecyclerView multiSelectRecyclerView;
    SearchView multiSelectSearchView;
    MultiSelectModel multiSelectModel;
    MultiSelectAdapter multiSelectAdapter;
    TextView txtNoData;
    Activity activity;
    int dataSelectionLimit = 1;
    int currentSelectionLimit = 0;
    ISelectedChoice selectedChoice;

    public interface ISelectedChoice {
        void selectedChoice(MultiSelectModel multiSelectedData, int position);
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }
        //Makesure you calculate the density pixel and multiply it with the size of width/height
        float dpCalculation = getResources().getDisplayMetrics().density;

        int dialogWidth = (int) (350 * dpCalculation); // specify a value here
        int dialogHeight = ViewGroup.LayoutParams.MATCH_PARENT; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        selectedChoice = (ISelectedChoice) activity;

        // ... other stuff you want to do in your onStart() method
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_multi_select, container);
        activity = getActivity();
        multiSelectList = new ArrayList<>();
        multiSelectFinalList = new ArrayList<>();
        multiSelectRecyclerView = view.findViewById(R.id.rv_multiselect);
        multiSelectSearchView = view.findViewById(R.id.sv_multiselect_search);
        txtNoData = view.findViewById(R.id.txt_no_data_found);
        for (int i = 0; i < 10; i++) {
            if (i % 5 == 0) {
                multiSelectModel = new MultiSelectModel("SomeData" + i, false, true);
            } else {
                multiSelectModel = new MultiSelectModel("SomeData" + i);
            }
            multiSelectList.add(multiSelectModel);
        }
        multiSelectList.add(new MultiSelectModel("Numbers", false, true));
        multiSelectList.add(new MultiSelectModel("one"));
        multiSelectList.add(new MultiSelectModel("two"));
        multiSelectList.add(new MultiSelectModel("three"));
        multiSelectList.add(new MultiSelectModel("four"));
        multiSelectList.add(new MultiSelectModel("five"));
        multiSelectList.add(new MultiSelectModel("six"));
        multiSelectList.add(new MultiSelectModel("Alphabets", false, true));
        multiSelectList.add(new MultiSelectModel("A"));
        multiSelectList.add(new MultiSelectModel("BC"));
        multiSelectList.add(new MultiSelectModel("DEF"));
        multiSelectList.add(new MultiSelectModel("GHIJ"));
        multiSelectList.add(new MultiSelectModel("KLMNO"));
        multiSelectList.add(new MultiSelectModel("PQRSTU"));
        multiSelectList.add(new MultiSelectModel("VWXYZAB"));
        multiSelectList.add(multiSelectModel);
        multiSelectFinalList.addAll(multiSelectList);
        multiSelectRecyclerView.setLayoutManager(
                new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        multiSelectAdapter =
                new MultiSelectAdapter(activity.getApplicationContext(), multiSelectList, this);
        multiSelectRecyclerView.setAdapter(multiSelectAdapter);
        multiSelectRecyclerView.addItemDecoration(new MultiSelectSectionDecorator(multiSelectRecyclerView, multiSelectAdapter));
        multiSelectSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    multiSelectList.clear();
                    multiSelectList.addAll(filteredResult(query));
                    multiSelectAdapter.notifyDataSetChanged();
                    if (multiSelectList.size() == 0) {
                        txtNoData.setVisibility(View.VISIBLE);
                        multiSelectRecyclerView.setVisibility(View.GONE);
                    } else {
                        txtNoData.setVisibility(View.GONE);
                        multiSelectRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        multiSelectSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                multiSelectList.clear();
                multiSelectList.addAll(multiSelectFinalList);
                multiSelectAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return view;
    }

    public ArrayList<MultiSelectModel> filteredResult(String searchQuery) {
        if (searchQuery.equals("")) {
            return multiSelectFinalList;
        } else {
            String currentSectionName = "";
            boolean isNewSection = false;
            ArrayList<MultiSelectModel> filteredList = new ArrayList<>();
            for (MultiSelectModel data : multiSelectFinalList) {
                if (data.isSection) {
                    isNewSection = true;
                    currentSectionName = data.dataString;
                } else {
                    if (data.dataString.toLowerCase().contains(searchQuery.toLowerCase())) {
                        if (isNewSection) {
                            filteredList.add(new MultiSelectModel(currentSectionName, false, true));
                            filteredList.add(new MultiSelectModel(data.dataString, false, false));
                            isNewSection = false;
                        } else {
                            filteredList.add(new MultiSelectModel(data.dataString, false, false));
                        }
                    }
                }
            }
            return filteredList;
        }
    }

    @Override
    public void selectedChoice(MultiSelectModel multiSelectedData, int position) {
        Toast.makeText(activity, multiSelectedData.dataString, Toast.LENGTH_SHORT).show();
        selectedChoice.selectedChoice(multiSelectedData, position);
        currentSelectionLimit += 1;
        if (currentSelectionLimit == dataSelectionLimit) {
            this.dismiss();
        }
    }
}
