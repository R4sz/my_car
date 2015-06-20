package rasz.app.My_Car.repository;

import android.database.Cursor;

public class FillupsRepository extends AbstractRepository{

	private int idSamochoduZtankowania;
	private double litry;
	private double cenaLitra;
	private String date;
	private String time;
	private String rodzajPaliwa;
	private String stacja;
	private double przejechane;
	private double spalanie;

	public FillupsRepository() {
	}

	public FillupsRepository(Cursor cc) {
		this.id = cc.getInt(0);
		this.idSamochoduZtankowania = cc.getInt(1);
		this.mileAge = cc.getLong(2);
		this.litry = cc.getDouble(3);
		this.cenaLitra = cc.getDouble(4);
		this.cost = cc.getDouble(5);
		this.date = cc.getString(6);
		this.time = cc.getString(7);
		this.rodzajPaliwa = cc.getString(8);
		this.stacja = cc.getString(9);
		this.notes = cc.getString(10);
		this.przejechane = cc.getDouble(11);
		this.spalanie = cc.getDouble(12);
		this.timestamp = cc.getLong(13);

	}

	public int getIdsamochoduZtankowania() {
		return idSamochoduZtankowania;
	}

	public double getLitry() {
		return litry;
	}

	public double getCenaLitra() {
		return cenaLitra;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getRodzajPaliwa() {
		return rodzajPaliwa;
	}

	public String getStacja() {
		return stacja;
	}

	public double getPrzejechane() {
		return przejechane;
	}

	public double getSpalanie() {
		return spalanie;
	}

}