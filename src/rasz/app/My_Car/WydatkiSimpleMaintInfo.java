package rasz.app.My_Car;

import android.database.Cursor;

public class WydatkiSimpleMaintInfo {

	private int idmaint;
	private int idsam;
	private Double mileage;
	private Double cost;
	private String maintanced;
	private String date;
	private String time;
	private String place;
	private String notes;
	private Long dateWmms;

	public WydatkiSimpleMaintInfo() {
	}

	public WydatkiSimpleMaintInfo(int idmaint, int idsam, Double mileage, Double cost, String maintanced, String date, String time, String place, String notes,
			Long dateWmms) {

		this.idmaint = idmaint;
		this.idsam = idsam;
		this.mileage = mileage;
		this.cost = cost;
		this.maintanced = maintanced;
		this.date = date;
		this.time = time;
		this.place = place;
		this.notes = notes;
		this.dateWmms = dateWmms;
	}

	public WydatkiSimpleMaintInfo(Cursor cc) {
		
		this.idmaint = cc.getInt(0);
		this.idsam = cc.getInt(1);
		this.mileage = cc.getDouble(2);
		this.cost = cc.getDouble(3);
		this.maintanced = cc.getString(4);
		this.date = cc.getString(5);
		this.time = cc.getString(6);
		this.place = cc.getString(7);
		this.notes = cc.getString(8);
		this.dateWmms = cc.getLong(9);
	}
	
	public int getIdmaint() {
		return idmaint;
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

	public String getMaintanced() {
		return maintanced;
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

	public Long getDatewmms() {
		return dateWmms;
	}
}