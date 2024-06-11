package com.example.kursovaya;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursovaya.databinding.ActivityDetailedBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;
    EditText startDateEditText;
    EditText endDateEditText;
    Button bookButton;
    private long startDate = 0;
    private long endDate = 0;
    private static final String SHARED_PREFS_BOOKINGS = "booking_preferences";
    private static final String BOOKING_KEY = "booking_info";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            String countryName = intent.getStringExtra("country_name");
            if (countryName != null) {
                JsonParser jsonParser = new JsonParser(this);
                List<Tour> tourList = jsonParser.parseToursFromFile();

                for (Tour tour : tourList) {
                    if (tour.getName().equals(countryName)) {
                        binding.detailName.setText(tour.getName());
                        binding.detailPrice.setText(tour.getPrice() + " руб.");
                        binding.detailDesc.setText(tour.getDescription());
                        int imageResource = getResources().getIdentifier(tour.getImage(), "drawable", getPackageName());
                        binding.detailImage.setImageResource(imageResource);
                        break;
                    }
                }
            }
        }

        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        bookButton = findViewById(R.id.bookButton);

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });


        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate == 0 || endDate == 0) {
                    Toast.makeText(DetailedActivity.this, "Пожалуйста, выберите даты начала и окончания", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailedActivity.this);
                builder.setTitle("Бронирование тура")
                        .setMessage("Ваш тур забронирован на следующие даты: " + getFormattedDateRange())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveBookingInfo();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void saveBookingInfo() {
        List<String> bookings = getBookings();
        String bookingInfo = "Даты бронирования: " + getFormattedDate(startDate) + " - " + getFormattedDate(endDate);
        bookings.add(bookingInfo);
        saveBookings(bookings);
        setBookingReminder(startDate - (24 * 60 * 60 * 1000));
    }


    private List<String> getBookings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_BOOKINGS, Context.MODE_PRIVATE);
        String bookingsString = sharedPreferences.getString(BOOKING_KEY, "");
        return new ArrayList<>(Arrays.asList(bookingsString.split("\n")));
    }

    private void saveBookings(List<String> bookings) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_BOOKINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKING_KEY, TextUtils.join("\n", bookings));
        editor.apply();
    }

    private void showDatePickerDialog(final boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailedActivity.this, R.style.CustomAlertDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        long selectedDateInMillis = selectedDate.getTimeInMillis();

                        if (isStartDate) {
                            startDate = selectedDateInMillis;
                            startDateEditText.setText(getFormattedDate(selectedDateInMillis));
                            if (endDate != 0 && startDate > endDate) {
                                endDate = 0;
                                endDateEditText.setText("");
                            }
                        } else {
                            if (selectedDateInMillis >= startDate) {
                                endDate = selectedDateInMillis;
                                endDateEditText.setText(getFormattedDate(selectedDateInMillis));
                            } else {
                                Toast.makeText(DetailedActivity.this, "Дата окончания не может быть раньше даты начала", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private String getFormattedDate(long dateInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(dateInMillis);
    }
    private String getFormattedDateRange() {
        if (startDate == 0 || endDate == 0) {
            return "";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return dateFormat.format(startDate) + " - " + dateFormat.format(endDate);
        }
    }
    private void setBookingReminder(long bookingDateInMillis) {
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long reminderTimeInMillis = bookingDateInMillis - oneDayInMillis;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BookingReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent);
    }
}
