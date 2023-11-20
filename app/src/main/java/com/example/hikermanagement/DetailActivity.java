package com.example.hikermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView nameTV, locationTV, lengthTV, difficultyTV, isParkingTV, descriptionTV, dateTV;
    Button buttonAddObservation;
    Button editDetail;
    String name, location, length, description, difficulty, date, hikeId;
    boolean parkingAvailable;
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    TextView emptyText;
    ImageView emptyImage;
//    ArrayList<String> obsId, obsText, obsTime, obsComment;

    List<ObservationModel> observations;
    ObservationAdapter observationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameTV = findViewById(R.id.textName);
        locationTV = findViewById(R.id.textLocation);
        lengthTV = findViewById(R.id.textLength);
        difficultyTV = findViewById(R.id.textDifficulty);
        isParkingTV = findViewById(R.id.textParking);
        descriptionTV = findViewById(R.id.textDescription);
        dateTV = findViewById(R.id.textDate);
        buttonAddObservation = findViewById(R.id.buttonAddObservation);
        editDetail = findViewById(R.id.editButtonDetail);
        emptyImage = findViewById(R.id.emptyImageObservation);
        emptyText = findViewById(R.id.emptyTextObservation);
        recyclerView = findViewById(R.id.listObservation);
        getSetDataIntent();

        buttonAddObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, AddObservation.class);
                intent.putExtra("hikeId", hikeId);
                startActivity(intent);
            }
        });
        editDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(hikeId));
                intent.putExtra("name", String.valueOf(name));
                intent.putExtra("location", String.valueOf(location));
                intent.putExtra("date", String.valueOf(date));
                intent.putExtra("parkingAvailable", String.valueOf(parkingAvailable));
                intent.putExtra("length", String.valueOf(length));
                intent.putExtra("difficulty", String.valueOf(difficulty));
                intent.putExtra("description", String.valueOf(description));
                startActivity(intent);
            }
        });
        databaseHelper = new DatabaseHelper(DetailActivity.this);
        observations = new ArrayList<ObservationModel>();
        storeDataObservation();
        observationAdapter = new ObservationAdapter(DetailActivity.this, observations, date, hikeId);
        recyclerView.setAdapter(observationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
    }

    void getSetDataIntent() {
        Intent intent = getIntent();
        hikeId = intent.getStringExtra("hikeId");
        name = intent.getStringExtra("name");
        location = intent.getStringExtra("location");
        length = intent.getStringExtra("length");
        date = intent.getStringExtra("date");
        difficulty = intent.getStringExtra("difficulty");
        parkingAvailable = intent.getBooleanExtra("parkingAvailable", false);
        description = intent.getStringExtra("description");

        nameTV.setText(name);
        locationTV.setText(location);
        lengthTV.setText(length);
        descriptionTV.setText(description);
        difficultyTV.setText(difficulty);
        dateTV.setText(date);
        if(parkingAvailable){
            isParkingTV.setText(String.valueOf(true));
        } else {
            isParkingTV.setText(String.valueOf(false));
        }
    }

    void storeDataObservation() {
        Cursor cursor = databaseHelper.readAllObservation(Integer.parseInt(hikeId));
        if(cursor.getCount() == 0){
            emptyImage.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                String obsId=cursor.getString(0);
                String obsText=cursor.getString(2);
                String obsTime=cursor.getString(3);
                String obsComment=cursor.getString(4);
                observations.add(new ObservationModel(obsId , obsText, obsTime, obsComment));
            }
            emptyImage.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
        }
    }

}