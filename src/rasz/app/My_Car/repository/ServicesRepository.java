package rasz.app.My_Car.repository;

import android.database.Cursor;

public class ServicesRepository extends AbstractRepository {

    private int idsam;
    private String serviced;
    private String date;
    private String time;

    public ServicesRepository() {
    }

    public ServicesRepository(Cursor cc) {

        this.id = cc.getInt(0);
        this.idsam = cc.getInt(1);
        this.mileAge = cc.getLong(2);
        this.cost = cc.getDouble(3);
        this.serviced = cc.getString(4);
        this.date = cc.getString(5);
        this.time = cc.getString(6);
        this.place = cc.getString(7);
        this.notes = cc.getString(8);
        this.timestamp = cc.getLong(9);
    }

    public int getIdsam() {
        return idsam;
    }

    public String getServiced() {
        return serviced;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public String getNotes() {
        return notes;
    }

}