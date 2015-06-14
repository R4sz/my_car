package rasz.app.My_Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rasz.app.My_Car.repository.CarsRepository;
import rasz.app.My_Car.repository.ServicesRepository;
import rasz.app.My_Car.repository.FillupsRepository;
import rasz.app.My_Car.repository.ExpensesRepository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataContainer {

	public static List<CarsRepository> listOfCars = new ArrayList<CarsRepository>();
	public static List<FillupsRepository> listOfRefuels = new ArrayList<FillupsRepository>();
	public static List<ServicesRepository> listOfServs = new ArrayList<ServicesRepository>();
	public static List<ExpensesRepository> listOfMaints = new ArrayList<ExpensesRepository>();

	public static ArrayList<String> samochodyNazwy = new ArrayList<String>();
	public static ArrayList<Integer> samochodyId = new ArrayList<Integer>();
	public static Map<String, Double> consumptionMap = new HashMap<String, Double>();
	public static ArrayList<Integer> activeCarsID = new ArrayList<Integer>();

	public static DatabaseDaneDB database;

	public static String getDataFromDB(Context context, String query, String table) {
		database = new DatabaseDaneDB(context);
		try {
			SQLiteDatabase bd = database.getReadableDatabase();
			Cursor kursor = bd.rawQuery(query, null);

			if (table.equals("cars")) {
				listOfCars.clear();
				while (kursor.moveToNext()) {
					listOfCars.add(new CarsRepository(kursor));
				}
			}

			if (table.equals("service")) {
				listOfServs.clear();
				while (kursor.moveToNext()) {
					listOfServs.add(new ServicesRepository(kursor));
				}
			}

			if (table.equals("refueling")) {
				listOfRefuels.clear();
				while (kursor.moveToNext()) {
					listOfRefuels.add(new FillupsRepository(kursor));
				}
			}

			if (table.equals("maintance")) {
				listOfMaints.clear();
				while (kursor.moveToNext()) {
					listOfMaints.add(new ExpensesRepository(kursor));
				}
			}
			if (table.equals("single")) {
				while (kursor.moveToNext()) {
					String result = kursor.getString(0);
					return result;
				}
			}
		} finally {
			database.close();
		}
		return "fail";
	}

	// metody z ktorych korzystaja klasy
	public static void getCarNamesForSpinner() {
		samochodyNazwy.clear();
		samochodyId.clear();

		for (int i = 0; i < listOfCars.size(); i++) {
			samochodyNazwy.add(listOfCars.get(i).getCarName());
			samochodyId.add(listOfCars.get(i).getId());
		}
	}

	public static double getCarMileage(int idsam) {
		SQLiteDatabase db = DataContainer.database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT przebieg FROM samochody WHERE idsam = " + idsam, null);
		while (c.moveToNext()) {
			double przebieg = c.getDouble(0);
			return przebieg;
		}
		return 0.0;
	}

	public static Map<String, Double> consumptionCalc(List<FillupsRepository> listOfRef) {

		FillupsRepository tankEntry;
		double totalTankCost = 0;
		double totalLiters = 0;
		double minSpalanie = 999;
		double maxSpalanie = 0;
		double cenaLitra = 0;
		double minCena = 999;
		double maxCena = 0;

		for (int i = 0; i < listOfRef.size(); i++) {
			tankEntry = listOfRef.get(i);
			totalTankCost += tankEntry.getWartosc();
			totalLiters += tankEntry.getLitry();

			// spalanie
			if (tankEntry.getSpalanie() < minSpalanie && tankEntry.getSpalanie() > 0) {
				minSpalanie = tankEntry.getSpalanie();
			}
			if (tankEntry.getSpalanie() > maxSpalanie) {
				maxSpalanie = tankEntry.getSpalanie();
			}

			// cena
			cenaLitra += tankEntry.getCenaLitra();

			if (tankEntry.getCenaLitra() < minCena && tankEntry.getCenaLitra() > 0) {
				minCena = tankEntry.getCenaLitra();
			}
			if (tankEntry.getCenaLitra() > maxCena) {
				maxCena = tankEntry.getCenaLitra();
			}
		}

		consumptionMap.put("totalTankCost", totalTankCost);
		consumptionMap.put("totalLiters", totalLiters);
		consumptionMap.put("minSpalanie", minSpalanie);
		consumptionMap.put("maxSpalanie", maxSpalanie);
		consumptionMap.put("cenaLitra", cenaLitra);
		consumptionMap.put("minCena", minCena);
		consumptionMap.put("maxCena", maxCena);

		return consumptionMap;
	}

	public static double getPrzejechane() {

		SQLiteDatabase db = DataContainer.database.getReadableDatabase();
		Cursor cc = db.rawQuery("SELECT idsam FROM samochody WHERE aktywny = 1", null);
		activeCarsID.clear();
		while (cc.moveToNext()) {
			activeCarsID.add(cc.getInt(0));
		}

		double przebiegStart = 0;
		double przejechane = 0;
		for (int i = 0; i < activeCarsID.size(); i++) {
			double przebiegActual = DataContainer.getCarMileage(activeCarsID.get(i));
			cc = db.rawQuery("SELECT przebieg_start from samochody WHERE idsam = " + activeCarsID.get(i), null);
			activeCarsID.clear();
			while (cc.moveToNext()) {
				przebiegStart = cc.getDouble(0);
			}

			przejechane += przebiegActual - przebiegStart;
		}

		return przejechane;
	}
}