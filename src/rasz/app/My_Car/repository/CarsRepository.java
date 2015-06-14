package rasz.app.My_Car.repository;

import android.database.Cursor;

public class CarsRepository extends AbstractRepository {

    private String carName;
    private String brand;
    private String model;
    private int produceYear;
    private Double engineCap;
    private String fuel;
    private String engineSymbol;
    private String vin;
    private int aktywny;
    private int ownMileage;
    private long dateInMms;
    private double price;
    private Long startMileage;

    public CarsRepository() {

    }

    public CarsRepository(Cursor kursor) {
        this();

        this.carName = kursor.getString(1);
        this.brand = kursor.getString(2);
        this.model = kursor.getString(3);
        this.produceYear = kursor.getInt(4);
        this. = kursor.getLong(5);
        this.engineCap = kursor.getDouble(6);
        this.fuel = kursor.getString(7);
        this.engineSymbol = kursor.getString(8);
        this.vin = kursor.getString(9);
        this.aktywny = kursor.getInt(10);
        this.ownMileage = kursor.getInt(11);
        this.dateInMms = kursor.getLong(12);
        this.startMileage = kursor.getLong(13);
        this.price = kursor.getDouble(14);
    }

    public void setAktywny(int aktywny) {
        this.aktywny = aktywny;
    }

    public String getCarName() {
        return carName;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getProduceYear() {
        return produceYear;
    }

    public Long getPrzebieg() {
        return przebieg;
    }

    public Double getPoj_silnika() {
        return engineCap;
    }

    public String getFuel() {
        return fuel;
    }

    public String getSymbol_silnika() {
        return engineSymbol;
    }

    public String getVin() {
        return vin;
    }

    public int getAktywny() {
        return aktywny;
    }

    public int getOwnMileage() {
        return ownMileage;
    }

    public void setPrzebieg(Long przebieg) {
        this.przebieg = przebieg;
    }

    public long getDateInMms() {
        return dateInMms;
    }

    public long getStartMileage() {
        return startMileage;
    }

    public double getPrice() {
        return price;
    }
}
