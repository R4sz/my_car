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
    private Long startMileage;

    public CarsRepository() {

    }

    public CarsRepository(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.carName = cursor.getString(1);
        this.brand = cursor.getString(2);
        this.model = cursor.getString(3);
        this.produceYear = cursor.getInt(4);
        this.mileAge = cursor.getLong(5);
        this.engineCap = cursor.getDouble(6);
        this.fuel = cursor.getString(7);
        this.engineSymbol = cursor.getString(8);
        this.vin = cursor.getString(9);
        this.aktywny = cursor.getInt(10);
        this.ownMileage = cursor.getInt(11);
        this.timestamp = cursor.getLong(12);
        this.startMileage = cursor.getLong(13);
        this.cost = cursor.getDouble(14);
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

    public Double getEngineCap() {
        return engineCap;
    }

    public String getFuel() {
        return fuel;
    }

    public String getEngineSymbol() {
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

    public Long getStartMileage() {
        return startMileage;
    }
}


