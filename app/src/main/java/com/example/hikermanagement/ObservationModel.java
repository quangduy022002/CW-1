package com.example.hikermanagement;

public class ObservationModel {
    String obsId;
    String obsText;
    String obsTime;
    String obsComment;

    public ObservationModel(String obsId, String obsText, String obsTime, String obsComment) {
        this.obsId = obsId;
        this.obsText = obsText;
        this.obsTime = obsTime;
        this.obsComment = obsComment;
    }
}
