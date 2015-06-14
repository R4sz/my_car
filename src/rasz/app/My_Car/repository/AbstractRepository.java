package rasz.app.My_Car.repository;
import android.database.Cursor;

public abstract class AbstractRepository {

	private int id;
	private double mileAge;
	private double cost;
	private long timestamp;
	private String place;
	private String notes;

    public AbstractRepository (Cursor cursor) {
        this.id = kursor.getInt(0);



    }

	public int getId() {
		return id;
	}

	public double getMileAge() {
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
