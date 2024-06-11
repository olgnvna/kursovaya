package com.example.kursovaya;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ItemFragment extends Fragment {
    private TourAdapter adapter;
    private List<Tour> tourList;
    private JsonParser jsonParser;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        ListView listView = view.findViewById(R.id.listview);

        jsonParser = new JsonParser(requireContext());
        tourList = jsonParser.parseToursFromFile();
        adapter = new TourAdapter(requireContext(), tourList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> showDeleteConfirmationDialog(position));

        Button addButton = view.findViewById(R.id.add_tour);
        addButton.setOnClickListener(v -> showAddTourDialog());

        listView.setOnItemLongClickListener((parent, view2, position, id) -> {
            showEditTourDialog(position);
            return true;
        });

        return view;

    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Подтверждение удаления")
                .setMessage("Вы уверены, что хотите удалить этот тур?")
                .setPositiveButton("Удалить", (dialog, which) -> deleteTour(position))
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteTour(int position) {
        jsonParser.deleteTour(position);
        tourList.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void showAddTourDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Добавить новый тур");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_tour, null);
        builder.setView(dialogView);

        EditText countryEditText = dialogView.findViewById(R.id.et_country);
        EditText descriptionEditText = dialogView.findViewById(R.id.et_description);
        EditText costEditText = dialogView.findViewById(R.id.et_cost);
        EditText photoEditText = dialogView.findViewById(R.id.et_photo);

        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String country = countryEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String cost = costEditText.getText().toString();
            String photo = photoEditText.getText().toString();

            if (!country.isEmpty() && !description.isEmpty() && !cost.isEmpty() && !photo.isEmpty()) {
                Tour tour = new Tour(country, cost, description, photo);
                addTour(tour);
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addTour(Tour tour) {
        String formattedPrice = tour.getPrice();
        Tour newTour = new Tour(tour.getName(), formattedPrice, tour.getDescription(), tour.getImage());
        jsonParser.addTour(newTour);
        tourList.add(newTour);
        adapter.notifyDataSetChanged();
    }

    private void showEditTourDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Изменить информацию о туре");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_tour, null);
        builder.setView(dialogView);

        EditText countryEditText = dialogView.findViewById(R.id.et_country);
        EditText descriptionEditText = dialogView.findViewById(R.id.et_description);
        EditText costEditText = dialogView.findViewById(R.id.et_cost);
        EditText photoEditText = dialogView.findViewById(R.id.et_photo);

        Tour tour = tourList.get(position);
        countryEditText.setText(tour.getName());
        descriptionEditText.setText(tour.getDescription());
        costEditText.setText(tour.getPrice());
        photoEditText.setText(tour.getImage());

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String country = countryEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String cost = costEditText.getText().toString();
            String photo = photoEditText.getText().toString();

            if (!country.isEmpty() && !description.isEmpty() && !cost.isEmpty() && !photo.isEmpty()) {
                updateTour(position, country, description, cost, photo);
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    private void updateTour(int position, String country, String description, String cost, String photo) {
        Tour tour = tourList.get(position);
        tour.setName(country);
        tour.setDescription(description);
        tour.setPrice(cost);
        tour.setImage(photo);

        jsonParser.updateTour(position, tour);

        tourList.set(position, tour);
        adapter.notifyDataSetChanged();
    }

}

