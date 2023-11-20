package com.example.hikermanagement;

public class HikeModel {
    String id;
    String name;
    String location;
    String length;
    String date;
    String description;
    String difficulty;
    String isParking;

    public HikeModel(String id, String name, String location, String length, String date,String description, String difficulty, String isParking) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.length = length;
        this.date = date;
        this.description = description;
        this.difficulty = difficulty;
        this.isParking = isParking;
    }
}
