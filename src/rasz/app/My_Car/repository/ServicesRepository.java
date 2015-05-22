package rasz.app.My_Car.repository;

import android.database.Cursor;

public class ServicesRepository {

	private int idserv;
	private int idsam;
	private Double mileage;
	private Double cost;
	private String serviced;
	private String date;
	private String time;
	private String place;
	private String notes;
	private Long datawmms;

	public ServicesRepository() {
	}

	public ServicesRepository(int idserv, int idsam, Double mileage, Double cost, String serviced, String date, String time, String place, String notes, Long datawmms) {
		super();

		this.idserv = idserv;
		this.idsam = idsam;
		this.mileage = mileage;
		this.cost = cost;
		this.serviced = serviced;
		this.date = date;
		this.time = time;
		this.place = place;
		this.notes = notes;
		this.datawmms = datawmms;		
		
	}

	public ServicesRepository(Cursor cc) {
		
		this.idserv = cc.getInt(0);
		this.idsam = cc.getInt(1);
		this.mileage = cc.getDouble(2);
		this.cost = cc.getDouble(3);
		this.serviced = cc.getString(4);
		this.date = cc.getString(5);
		this.time = cc.getString(6);
		this.place = cc.getString(7);
		this.notes = cc.getString(8);
		this.datawmms = cc.getLong(9);	
	}
	
	
	public int getIdserv() {
		return idserv;
	}

	public int getIdsam() {
		return idsam;
	}

	public Double getMileage() {
		return mileage;
	}

	public Double getCost() {
		return cost;
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

	public Long getDatawmms() {
		return datawmms;
	}
}