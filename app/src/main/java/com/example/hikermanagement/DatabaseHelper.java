package com.example.hikermanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "HikerManagement.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_HIKES_MANAGEMENT = "hikes_management";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String LOCATION_HIKE = "location_hike";
    private static final String DATE_HIKE = "date_hike";
    private static final String ISPARKING = "is_parking";
    private static final String LENGTH_HIKE = "length_hike";
    private static final String DIFFICULTY_HIKE= "difficulty_hike";
    private static final String DESCRIPTION_HIKE = "description_hike";

    private static final String TABLE_OBSERVATIONS_OF_HIKE = "observations_of_hike";
    private static final String ID_OBSERVATION = "id_observation";
    private static final String ID_HIKE = "hike_id";
    private static final String TEXT_OBSERVATION = "text_observation";
    private static final String TIME_OBSERVATION = "time_observation";
    private static final String COMMENTS = "comments";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String hike_query =
                "CREATE TABLE " + TABLE_HIKES_MANAGEMENT +
                        " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TITLE + " TEXT, " +
                        LOCATION_HIKE + " TEXT, " +
                        DATE_HIKE + " TEXT, " +
                        ISPARKING + " TEXT, " +
                        LENGTH_HIKE + " TEXT, " +
                        DIFFICULTY_HIKE + " TEXT, " +
                        DESCRIPTION_HIKE + " TEXT) ;";

        String observation_query =
                "CREATE TABLE " + TABLE_OBSERVATIONS_OF_HIKE +
                        " (" + ID_OBSERVATION + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ID_HIKE + " INTEGER, " +
                        TEXT_OBSERVATION + " TEXT, " +
                        TIME_OBSERVATION + " TEXT, " +
                        COMMENTS + " TEXT) ;";

        db.execSQL(hike_query);
        db.execSQL(observation_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES_MANAGEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS_OF_HIKE);
        onCreate(db);
    }

    //hike
    void addHike(String name, String location, String date, String length,
                 String difficulty, String description, String parking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, name);
        cv.put(LOCATION_HIKE, location);
        cv.put(DATE_HIKE, date);
        cv.put(LENGTH_HIKE, length);
        cv.put(DIFFICULTY_HIKE, difficulty);
        cv.put(DESCRIPTION_HIKE, description);
        cv.put(ISPARKING, parking);

        long result = db.insert(TABLE_HIKES_MANAGEMENT, null, cv);

        if(result == -1){
            Toast.makeText(context, "Added Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_HIKES_MANAGEMENT;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateHike(String id, String name, String location, String date, String length,
                 String difficulty, String description, String parking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, name);
        cv.put(LOCATION_HIKE, location);
        cv.put(DATE_HIKE, date);
        cv.put(LENGTH_HIKE, length);
        cv.put(DIFFICULTY_HIKE, difficulty);
        cv.put(DESCRIPTION_HIKE, description);
        cv.put(ISPARKING, parking);

        long result = db.update(TABLE_HIKES_MANAGEMENT, cv, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Updated Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOne(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_HIKES_MANAGEMENT, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Deleted Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Deleted Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HIKES_MANAGEMENT);
    }

    //observation
    void addObservation(int hike_id, String observation_text, String observation_time, String observation_comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ID_HIKE, hike_id);
        cv.put(TEXT_OBSERVATION, observation_text);
        cv.put(TIME_OBSERVATION, observation_time);
        cv.put(COMMENTS, observation_comment);

        long result = db.insert(TABLE_OBSERVATIONS_OF_HIKE, null, cv);

        if(result == -1){
            Toast.makeText(context, "Added Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllObservation(int hikeId) {
        String query = "SELECT * FROM " + TABLE_OBSERVATIONS_OF_HIKE + " WHERE " + ID_HIKE + " = " + hikeId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateObservation(String id,int hike_id, String observation_text, String observation_time, String observation_comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_HIKE, hike_id);
        cv.put(TEXT_OBSERVATION, observation_text);
        cv.put(TIME_OBSERVATION, observation_time);
        cv.put(COMMENTS, observation_comment);

        long result = db.update(TABLE_OBSERVATIONS_OF_HIKE, cv, "observation_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Updated Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneObservation(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_OBSERVATIONS_OF_HIKE, "observation_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Deleted Failed !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Deleted Successfully !", Toast.LENGTH_SHORT).show();
        }
    }
}
