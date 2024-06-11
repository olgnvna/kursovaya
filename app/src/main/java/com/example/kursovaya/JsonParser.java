package com.example.kursovaya;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private Context context;

    public JsonParser(Context context) {
        this.context = context;
    }

    public List<Tour> parseToursFromFile() {
        List<Tour> tourList = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput("tours.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String price = jsonObject.getString("price");
                String description = jsonObject.getString("description");
                String image = jsonObject.getString("image");
                tourList.add(new Tour(name, price, description, image));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return tourList;
    }

    public void deleteTour(int position) {
        try {
            FileInputStream fis = context.openFileInput("tours.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            JSONArray jsonArray = new JSONArray(sb.toString());
            jsonArray.remove(position);

            FileOutputStream fos = context.openFileOutput("tours.json", Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void addTour(Tour tour) {
        try {
            FileInputStream fis = context.openFileInput("tours.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            JSONArray jsonArray = new JSONArray(sb.toString());
            JSONObject newTour = new JSONObject();
            newTour.put("name", tour.getName());
            newTour.put("price", tour.getPrice());
            newTour.put("description", tour.getDescription());
            newTour.put("image", tour.getImage());
            jsonArray.put(newTour);

            FileOutputStream fos = context.openFileOutput("tours.json", Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateTour(int position, Tour tour) {
        try {
            FileInputStream fis = context.openFileInput("tours.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            JSONArray jsonArray = new JSONArray(sb.toString());
            JSONObject updatedTour = new JSONObject();
            updatedTour.put("name", tour.getName());
            updatedTour.put("price", tour.getPrice());
            updatedTour.put("description", tour.getDescription());
            updatedTour.put("image", tour.getImage());

            jsonArray.put(position, updatedTour);

            FileOutputStream fos = context.openFileOutput("tours.json", Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}

