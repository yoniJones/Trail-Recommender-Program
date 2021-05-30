/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_tools;

/**
 *
 * @author yonij
 */
public class Trail {
    protected String name;
    protected String areaName;
    protected String state;
    protected double length;
    protected int difficulty;
    protected String features;
    protected String activities;

    public Trail(String name, String areaName, String state, double length, int difficulty, String features, String activities) {
        this.name = name;
        this.areaName = areaName;
        this.state = state;
        this.length = length;
        this.difficulty = difficulty;
        this.features = features;
        this.activities = activities;
    }

    
    // getters and setters 

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    
    
    public String getName() {
        return name;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getState() {
        return state;
    }

    public double getLength() {
        return length;
    }

    public String getFeatures() {
        return features;
    }

    public String getActivities() {
        return activities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }
    
    
}
