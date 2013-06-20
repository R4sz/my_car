package rasz.app.My_Car;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Statystyki extends Activity {
	
	public SamochodySimpleCarInfo samEntry = new SamochodySimpleCarInfo();
	public TankowaniaSimpleTankInfo tankEntry;
	public WydatkiSimpleMaintInfo maintEntry;
	public ServiceSimpleServInfo servEntry;
	
	private double totalMaintCost = 0;
	private double totalServCost = 0;
	private double generalCost = 0;		
	private double przejechane = DataContainer.getPrzejechane();
	
	private int year;
	private int month;
	private int day;
	
	private String actualDate;

	private long actualdateInMillis = 0;

	DecimalFormat df = new DecimalFormat("0.0#");
	DecimalFormat dff = new DecimalFormat("0");
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statystyki);

		getData();
		if(!DataContainer.listOfRefuels.isEmpty())
		initElements();
	}	
	
	private void getData() {	
		DataContainer.getDataFromDB(this, "SELECT t.* " + "FROM tankowania as t, samochody as s " + "WHERE t.idsam = s.idsam AND s.aktywny = 1 "
				+ "ORDER BY t.datawmms", "refueling");
		
		DataContainer.getDataFromDB(this, "SELECT * FROM Samochody WHERE aktywny = 1", "cars");
		
		DataContainer.getDataFromDB(this, "SELECT n.* " + "FROM naprawy as n, samochody as s " + "WHERE n.idsam = s.idsam " + "AND s.aktywny = 1 "
				+ "ORDER BY datawmms", "service");

		DataContainer.getDataFromDB(this, "SELECT m.* " + "FROM maintance as m, samochody as s " + "WHERE m.idsam = s.idsam " + "AND s.aktywny = 1 "
				+ "ORDER BY m.datawmms", "maintance");

		DataContainer.consumptionMap = DataContainer.consumptionCalc(DataContainer.listOfRefuels);
	}
	
	private void initElements() {		
		for (int i = 0; i < DataContainer.listOfMaints.size(); i++) {
			maintEntry = DataContainer.listOfMaints.get(i);
			totalMaintCost += maintEntry.getCost();
		}
		for (int i = 0; i < DataContainer.listOfServs.size(); i++) {
			servEntry = DataContainer.listOfServs.get(i);
			totalServCost += servEntry.getCost();		
		}

		generalCost = DataContainer.consumptionMap.get("totalTankCost") + totalMaintCost + totalServCost;

		TextView general_cost = (TextView) findViewById(R.id.general_cost);
		general_cost.setText(df.format(generalCost));


		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		String yr = Integer.toString(year);
		String mt = String.format("%02d", month + 1);
		String dy = String.format("%02d", day);

		actualDate = dy + "-" + mt + "-" + yr;

		Date dateDateTank = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			dateDateTank = (Date) format.parse(actualDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		actualdateInMillis = dateDateTank.getTime();

		Long carPucharseDate = Long.valueOf(DataContainer.getDataFromDB(this, "SELECT czas_zakupu from samochody " + "WHERE aktywny = 1 ORDER BY czas_zakupu",
				"single"));

		Long carLifeTimeDays = (actualdateInMillis - carPucharseDate)/86400000;

		String str = df.format(generalCost / (double) carLifeTimeDays);

		TextView cost_per_day = (TextView) findViewById(R.id.cost_per_day);
		cost_per_day.setText(str);
		
		TextView cost_per_km = (TextView) findViewById(R.id.cost_per_km);
		cost_per_km.setText((df.format(generalCost / przejechane)));

		Double summaryCarsPrice = 0.0;
		for (int i = 0 ; i < DataContainer.listOfCars.size() ; i++ ) {
			samEntry = DataContainer.listOfCars.get(i);
			summaryCarsPrice += samEntry.getCena();			
		}
				
		TextView pucharse_cost = (TextView) findViewById(R.id.pucharse_cost);
		pucharse_cost.setText(Double.toString(summaryCarsPrice));

		TextView from_pucharse_cost = (TextView) findViewById(R.id.from_pucharse_cost);
		from_pucharse_cost.setText(df.format(summaryCarsPrice + generalCost));
	
		TextView from_pucharse_cost_per_day = (TextView) findViewById(R.id.from_pucharse_cost_per_day);
		from_pucharse_cost_per_day.setText(df.format((summaryCarsPrice + generalCost) / carLifeTimeDays));	
		
		TextView from_pucharse_cost_per_km = (TextView) findViewById(R.id.from_pucharse_cost_per_km);
		from_pucharse_cost_per_km.setText(df.format((summaryCarsPrice + generalCost) / przejechane));
		
		TextView distance = (TextView) findViewById(R.id.distance);
		distance.setText(Double.toString(przejechane));
		
		TextView total_time = (TextView) findViewById(R.id.total_time);
		total_time.setText(dff.format(carLifeTimeDays));						
		
		TextView average_consumption = (TextView) findViewById(R.id.average_consumption);
		average_consumption.setText(df.format((DataContainer.consumptionMap.get("totalLiters")*100) / przejechane));		
		
		tankEntry = DataContainer.listOfRefuels.get(DataContainer.listOfRefuels.size()-1);
		TextView last_average_consumption = (TextView) findViewById(R.id.last_average_consumption);
		last_average_consumption.setText(df.format((tankEntry.getSpalanie())));

		TextView min_average_consumption = (TextView) findViewById(R.id.min_average_consumption);
		min_average_consumption.setText(df.format(DataContainer.consumptionMap.get("minSpalanie")));
		
		TextView max_average_consumption = (TextView) findViewById(R.id.max_average_consumption);
		max_average_consumption.setText(df.format(DataContainer.consumptionMap.get("maxSpalanie")));
		
		TextView average_zl_per_liter = (TextView) findViewById(R.id.average_zl_per_liter);
		average_zl_per_liter.setText(df.format(DataContainer.consumptionMap.get("cenaLitra") / DataContainer.listOfRefuels.size()));
				
		TextView last_zl_per_liter = (TextView) findViewById(R.id.last_zl_per_liter);
		last_zl_per_liter.setText(df.format(tankEntry.getCenaLitra()));
		
		TextView min_zl_per_liter = (TextView) findViewById(R.id.min_zl_per_liter);
		min_zl_per_liter.setText(df.format(DataContainer.consumptionMap.get("minCena")));
		
		TextView max_zl_per_liter = (TextView) findViewById(R.id.max_zl_per_liter);
		max_zl_per_liter.setText(df.format(DataContainer.consumptionMap.get("maxCena")));				
				
		TextView avg_zl_per_km = (TextView) findViewById(R.id.avg_zl_per_km);
		avg_zl_per_km.setText(df.format(DataContainer.consumptionMap.get("totalTankCost")/przejechane));
		
		TextView avg_zl_per_day = (TextView) findViewById(R.id.avg_zl_per_day);
		avg_zl_per_day.setText(df.format(DataContainer.consumptionMap.get("totalTankCost")/carLifeTimeDays));
		
		TextView avg_zl_per_fill = (TextView) findViewById(R.id.avg_zl_per_fill);
		avg_zl_per_fill.setText(df.format(DataContainer.consumptionMap.get("totalTankCost")/DataContainer.listOfRefuels.size()));
		
		TextView avg_km_per_zl = (TextView) findViewById(R.id.avg_km_per_zl);
		avg_km_per_zl.setText(df.format(przejechane/DataContainer.consumptionMap.get("totalTankCost")));
		
		TextView avg_Liter_per_fill = (TextView) findViewById(R.id.avg_Liter_per_fill);
		avg_Liter_per_fill.setText(df.format(DataContainer.consumptionMap.get("totalLiters")/DataContainer.listOfRefuels.size()));
		
		TextView avg_day_per_fill = (TextView) findViewById(R.id.avg_Day_per_fill);
		avg_day_per_fill.setText(df.format(carLifeTimeDays/DataContainer.listOfRefuels.size()));
		
		TextView avg_km_per_fill = (TextView) findViewById(R.id.avg_Km_per_fill);
		avg_km_per_fill.setText(df.format(przejechane/DataContainer.listOfRefuels.size()));
		
		TextView total_liters = (TextView) findViewById(R.id.total_liters);
		total_liters.setText(df.format(DataContainer.consumptionMap.get("totalLiters")));
		
		TextView total_zl = (TextView) findViewById(R.id.total_zl);
		total_zl.setText(df.format(DataContainer.consumptionMap.get("totalTankCost")));
		
	}	
	public void backButton(View v) {
		finish();

	}
}