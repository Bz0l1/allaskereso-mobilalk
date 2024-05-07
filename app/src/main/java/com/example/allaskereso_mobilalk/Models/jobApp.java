package com.example.allaskereso_mobilalk.Models;

public class jobApp {
    private final String jobID, seekerID;

    public jobApp(String jobId, String seekerId) {
        this.jobID = jobId;
        this.seekerID = seekerId;
    }

    public String getJobID() {
        return jobID;
    }

    public String getSeekerID() {
        return seekerID;
    }
}
