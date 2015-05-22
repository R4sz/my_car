package rasz.app.My_Car.repository;

import android.database.Cursor;

public class FillupsRepository {

	private int idTank;
	private int idSamochoduZtankowania;
	private double przebieg;
	private double litry;
	private double cenaLitra;
	private double wartosc;
	private String date;
	private String time;
	private String rodzajPaliwa;
	private String stacja;
	private String notki;
	private double przejechane;
	private double spalanie;
	private long dateMillis;

	public FillupsRepository() {
	}

	public FillupsRepository(int idTank, int idSamochoduZtankowania, double przebieg, double litry, double cenaLitra, double wartosc, String date,
			String time, String rodzajPaliwa, String stacja, String notki, double przejechane, double spalanie, long dateMillis) {

		this.idTank = idTank;
		this.idSamochoduZtankowania = idSamochoduZtankowania;
		this.przebieg = przebieg;
		this.litry = litry;
		this.cenaLitra = cenaLitra;
		this.wartosc = wartosc;
		this.date = date;
		this.time = time;
		this.rodzajPaliwa = rodzajPaliwa;
		this.stacja = stacja;
		this.notki = notki;
		this.przejechane = przejechane;
		this.spalanie = spalanie;
		this.dateMillis = dateMillis;

	}

	public FillupsRepository(Cursor cc) {
		this.idTank = cc.getInt(0);
		this.idSamochoduZtankowania = cc.getInt(1);
		this.przebieg = cc.getDouble(2);
		this.litry = cc.getDouble(3);
		this.cenaLitra = cc.getDouble(4);
		this.wartosc = cc.getDouble(5);
		this.date = cc.getString(6);
		this.time = cc.getString(7);
		this.rodzajPaliwa = cc.getString(8);
		this.stacja = cc.getString(9);
		this.notki = cc.getString(10);
		this.przejechane = cc.getDouble(11);
		this.spalanie = cc.getDouble(12);
		this.dateMillis = cc.getLong(13);

	}

	public int getIdTank() {
		return idTank;
	}

	public int getIdsamochoduZtankowania() {
		return idSamochoduZtankowania;
	}

	public double getPrzebieg() {
		return przebieg;
	}

	public double getLitry() {
		return litry;
	}

	public double getCenaLitra() {
		return cenaLitra;
	}

	public double getWartosc() {
		return wartosc;
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

	public String getNotki() {
		return notki;
	}

	public double getPrzejechane() {
		return przejechane;
	}

	public double getSpalanie() {
		return spalanie;
	}

	public long getDatemillis() {
		return dateMillis;
	}
}