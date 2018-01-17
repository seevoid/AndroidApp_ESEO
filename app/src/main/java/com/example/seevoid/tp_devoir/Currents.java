package com.example.seevoid.tp_devoir;

/**
 * Created by seevoid on 14/01/18.
 */

public class Currents {

    public static final Currents INSTANCE = new Currents();

    public static Currents getInstance() {
        return INSTANCE;
    }

    private Currents() {}

    private int currentIndexVehicle;

    public int getCurrentIndexVehicle() {
        return this.currentIndexVehicle;
    }

    public void setCurrentIndexVehicle(int currentIndex) {
        this.currentIndexVehicle = currentIndex;
    }

    private int currentIndexPeople;

    public int getCurrentIndexPeople() {
        return this.currentIndexPeople;
    }

    public void setCurrentIndexPeople(int currentIndex) {
        this.currentIndexPeople = currentIndex;
    }


    private int currentIndexPlanet;

    public int getCurrentIndexPlanet() {
        return this.currentIndexPlanet;
    }

    public void setCurrentIndexPlanet(int currentIndex) {
        this.currentIndexPlanet = currentIndex;
    }

    private int currentIndexStarship;

    public int getCurrentIndexStarship() {
        return this.currentIndexStarship;
    }

    public void setCurrentIndexStarship(int currentIndex) {
        this.currentIndexStarship = currentIndex;
    }
}
