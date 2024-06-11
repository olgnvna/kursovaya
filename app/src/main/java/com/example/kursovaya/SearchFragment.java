package com.example.kursovaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private ListView listView;
    private List<Tour> tourList;
    private TourAdapter adapter;
    private JsonParser jsonParser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        listView = view.findViewById(R.id.listview);
        tourList = new ArrayList<>();
        jsonParser = new JsonParser(requireContext());
        tourList.addAll(jsonParser.parseToursFromFile());
        adapter = new TourAdapter(requireContext(), tourList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Tour selectedTour = tourList.get(position);
            Intent intent = new Intent(requireContext(), DetailedActivity.class);
            intent.putExtra("country_name", selectedTour.getName());
            startActivity(intent);
        });

        return view;
    }
}



