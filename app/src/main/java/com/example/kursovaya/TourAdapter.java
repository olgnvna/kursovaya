package com.example.kursovaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class TourAdapter extends ArrayAdapter<Tour> {

    public TourAdapter(@NonNull Context context, @NonNull List<Tour> tours) {
        super(context, 0, tours);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tour tour = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ShapeableImageView imageView = convertView.findViewById(R.id.listImage);
        TextView nameTextView = convertView.findViewById(R.id.listName);
        TextView priceTextView = convertView.findViewById(R.id.listPrice);

        imageView.setImageResource(getImageResource(tour.getImage()));
        nameTextView.setText(tour.getName());
        priceTextView.setText(tour.getPrice() + " руб.");

        return convertView;
    }

    private int getImageResource(String imageName) {
        return getContext().getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
    }
}
