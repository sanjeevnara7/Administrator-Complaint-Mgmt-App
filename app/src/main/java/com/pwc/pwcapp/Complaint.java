package com.pwc.pwcapp;

public class Complaint {

    String complaintId, userId, description, status, category, timestamp ,postedUserKey;
    Boolean isResolved;

    public Complaint() {
    }

    public Complaint(String complaintId, String userId, String description, String status, String category, String timestamp, Boolean isResolved, String postedUserKey) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.description = description;
        this.status = status;
        this.category = category;
        this.timestamp = timestamp;
        this.isResolved = isResolved;
        this.postedUserKey = postedUserKey;
    }

    public String getPostedUserKey() {
        return postedUserKey;
    }

    public void setPostedUserKey(String postedUserKey) {
        this.postedUserKey = postedUserKey;
    }

    public Boolean getResolved() {
        return isResolved;
    }

    public void setResolved(Boolean resolved) {
        isResolved = resolved;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
