package rasz.app.My_Car.repository;

import android.database.Cursor;

public class ExpensesRepository extends AbstractRepository {

    private int idsam;
    private String maintanced;
    private String date;
    private String time;

    public ExpensesRepository() {
    }

    public ExpensesRepository(Cursor cursor) {

        this.id = cursor.getInt(0);
        this.idsam = cursor.getInt(1);
        this.mileAge = cursor.getLong(2);
        this.cost = cursor.getDouble(3);
        this.maintanced = cursor.getString(4);
        this.date = cursor.getString(5);
        this.time = cursor.getString(6);
        this.place = cursor.getString(7);
        this.notes = cursor.getString(8);
        this.timestamp = cursor.getLong(9);
    }


    public int getIdsam() {
        return idsam;
    }

    public String getMaintanced() {
        return maintanced;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}