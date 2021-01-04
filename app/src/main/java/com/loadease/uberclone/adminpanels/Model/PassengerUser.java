package com.loadease.uberclone.adminpanels.Model;

public class PassengerUser {
    private String email,password,name,phone, avatarUrl, rates,industry,
            typeofRide,labourdetails,insurancedetails,DOB,GENDDER,id,AverageRating;

    public PassengerUser(String email, String password, String name, String phone, String avatarUrl, String rates, String industry, String typeofRide, String labourdetails, String insurancedetails, String DOB, String GENDDER, String id, String averageRating) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rates = rates;
        this.industry = industry;
        this.typeofRide = typeofRide;
        this.labourdetails = labourdetails;
        this.insurancedetails = insurancedetails;
        this.DOB = DOB;
        this.GENDDER = GENDDER;
        this.id = id;
        AverageRating = averageRating;
    }

    public String getAverageRating() {
        return AverageRating;
    }

    public void setAverageRating(String averageRating) {
        AverageRating = averageRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsurancedetails() {
        return insurancedetails;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGENDDER() {
        return GENDDER;
    }

    public void setGENDDER(String GENDDER) {
        this.GENDDER = GENDDER;
    }

    public void setInsurancedetails(String insurancedetails) {
        this.insurancedetails = insurancedetails;
    }

    public String getLabourdetails() {
        return labourdetails;
    }

    public void setLabourdetails(String labourdetails) {
        this.labourdetails = labourdetails;
    }

    public String getTypeofRide() {
        return typeofRide;
    }

    public void setTypeofRide(String typeofRide) {
        this.typeofRide = typeofRide;
    }

    public PassengerUser(){

    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public PassengerUser(String email, String password, String name, String phone, String avatarUrl, String rates, String industry) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rates = rates;
        this.industry=industry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }
}
