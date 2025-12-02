package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "savedTrips")
public class SavedTrip {

    @Id
    private String id;

    private String userId;
    private String title;
    private String planHtml;
    private String createdAt; // simple string date/time

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPlanHtml() { return planHtml; }
    public void setPlanHtml(String planHtml) { this.planHtml = planHtml; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
