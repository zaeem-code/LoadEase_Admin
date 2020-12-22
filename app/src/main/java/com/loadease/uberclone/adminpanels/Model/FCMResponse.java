package com.loadease.uberclone.adminpanels.Model;


import java.util.List;

public class FCMResponse {
    public long multicast_id;
    public int success;
    public int failture;
    public int canonical_ids;
    public List<FCMResult> results;

    public FCMResponse() {
    }


    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailture() {
        return failture;
    }

    public void setFailture(int failture) {
        this.failture = failture;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<FCMResult> getResults() {
        return results;
    }

    public void setResults(List<FCMResult> results) {
        this.results = results;
    }
}
