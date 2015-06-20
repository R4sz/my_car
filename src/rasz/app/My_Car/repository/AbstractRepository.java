package rasz.app.My_Car.repository;

import android.database.Cursor;

public abstract class AbstractRepository {

    int id;
    Long mileAge;
    double cost;
    long timestamp;
    String place;
    String notes;

    public int getId() {
        return id;
    }

    public Long getMileAge() {
        return mileAge;
    }

    public double getCost() {
        return cost;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPlace() {
        return place;
    }

    public String getNotes() {
        return notes;
    }

}
