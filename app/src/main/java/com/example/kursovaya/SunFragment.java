package com.example.kursovaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kursovaya.databinding.FragmentSunBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SunFragment extends Fragment {

    FragmentSunBinding binding;

    private static final String SHARED_PREFS_BOOKINGS = "booking_preferences";
    private static final String BOOKING_KEY = "booking_info";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSunBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        displayBookings();

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

        });
        Button clearButton = binding.clearButton;
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBookings();
            }
        });

        return view;
    }

    private void clearBookings() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS_BOOKINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(BOOKING_KEY);
        editor.apply();
        displayBookings();
    }

    private void displayBookings() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS_BOOKINGS, Context.MODE_PRIVATE);
        String bookingInfo = sharedPreferences.getString(BOOKING_KEY, "");
        if (!bookingInfo.isEmpty()) {
            TextView bookingTextView = binding.bookingDate;
            bookingTextView.setText(bookingInfo);
        }
    }
}

