package rasz.app.My_Car;

import android.database.Cursor;

public class SamochodySimpleCarInfo {

	private int idsam;
	private String nazwa;
	private String marka;
	private String model;
	private int rok;
	private Long przebieg;
	private Double pojSilnika;
	private String paliwo;
	private String symbolSilnika;
	private String vin;
	private int aktywny;
	private int przejechane;
	private long dateInMms;
	private double cena;
	private Long przebiegStart;

	public SamochodySimpleCarInfo() {

	}

	public SamochodySimpleCarInfo(int idsam, String nazwa, String marka, 
			String model, int rok, Long przebieg, Double pojSilnika, String paliwo,
			String symbolSilnika, String vin, int aktywny, int przejechane, long dateInMms, Long przebiegStart, 
			double cena) {

		this.idsam = idsam;
		this.nazwa = nazwa;
		this.marka = marka;
		this.model = model;
		this.rok = rok;
		this.przebieg = przebieg;
		this.pojSilnika = pojSilnika;
		this.paliwo = paliwo;
		this.symbolSilnika = symbolSilnika;
		this.vin = vin;
		this.aktywny = aktywny;
		this.przejechane = przejechane;
		this.dateInMms = dateInMms;
		this.przebiegStart = przebiegStart;
		this.cena = cena;						
		
	}

	public SamochodySimpleCarInfo(Cursor kursor) {
		this();

		this.idsam = kursor.getInt(0);
		this.nazwa = kursor.getString(1);
		this.marka = kursor.getString(2);
		this.model = kursor.getString(3);
		this.rok = kursor.getInt(4);
		this.przebieg = kursor.getLong(5);
		this.pojSilnika = kursor.getDouble(6);
		this.paliwo = kursor.getString(7);
		this.symbolSilnika = kursor.getString(8);
		this.vin = kursor.getString(9);
		this.aktywny = kursor.getInt(10);
		this.przejechane = kursor.getInt(11);
		this.dateInMms = kursor.getLong(12);
		this.przebiegStart = kursor.getLong(13);
		this.cena = kursor.getDouble(14);
	}

	public void setAktywny(int aktywny) {
		this.aktywny = aktywny;
	}

	public int getIdsam() {
		return idsam;
	}

	public String getNazwa() {
		return nazwa;
	}

	public String getMarka() {
		return marka;
	}

	public String getModel() {
		return model;
	}

	public int getRok() {
		return rok;
	}

	public Long getPrzebieg() {
		return przebieg;
	}

	public Double getPoj_silnika() {
		return pojSilnika;
	}

	public String getPaliwo() {
		return paliwo;
	}

	public String getSymbol_silnika() {
		return symbolSilnika;
	}

	public String getVin() {
		return vin;
	}

	public int getAktywny() {
		return aktywny;
	}

	public int getPrzejechane() {
		return przejechane;
	}

	public void setPrzebieg(Long przebieg) {
		this.przebieg = przebieg;
	}
	
	public long getDateInMms() {
		return dateInMms;
	}
	
	public long getPrzebiegStart() {
		return przebiegStart;
	}
 
	public double getCena() {
		return cena;
	}	
}
