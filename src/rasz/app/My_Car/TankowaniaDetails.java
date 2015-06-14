package rasz.app.My_Car;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import rasz.app.My_Car.repository.CarsRepository;
import rasz.app.My_Car.repository.FillupsRepository;

public class TankowaniaDetails extends Activity {

	public FillupsRepository entry;
	public int position;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tankowania_details);
		initTextFields();
	}

	String GetCarName() {
		String j = null;
		Intent in = getIntent();
		int position = in.getIntExtra("position", 0);
		FillupsRepository entry = DataContainer.listOfRefuels.get(position);
		for (CarsRepository t : DataContainer.listOfCars) {
			if (t.getId() == entry.getIdsamochoduZtankowania()) {
				j = t.getCarName();
				return j;
			}
		}
		return j;
	}

	void initTextFields() {
		Intent in = getIntent();
		position = in.getIntExtra("position", 0);
		entry = DataContainer.listOfRefuels.get(position);
        FillupsRepository previousEntry = null;
        FillupsRepository nextEntry = null;
		if (position != 0) {
			previousEntry = DataContainer.listOfRefuels.get(position - 1);
		}
		if ((DataContainer.listOfRefuels.size() - 1) != position) {
			nextEntry = DataContainer.listOfRefuels.get(position + 1);
		}

		TextView name = (TextView) findViewById(R.id.car_name);
		name.setText(GetCarName());

		TextView odoMeter = (TextView) findViewById(R.id.car_odometer);
		odoMeter.setText(Double.toString(entry.getPrzebieg()));

		TextView liters = (TextView) findViewById(R.id.litry);
		liters.setText(Double.toString(entry.getLitry()));

		TextView costPerLiter = (TextView) findViewById(R.id.cost_per_liter);
		costPerLiter.setText(Double.toString(entry.getCenaLitra()));

		TextView cost = (TextView) findViewById(R.id.total_tank_cost);
		cost.setText(Double.toString(entry.getWartosc()));

		TextView przejechane = (TextView) findViewById(R.id.przejechane);
		przejechane.setText(Double.toString(entry.getPrzejechane()));
		
		Double spalanie = 0.0;
		Double previousTankOdometer = 0.0;
		SQLiteDatabase db = DataContainer.database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT idtankowania, przebieg FROM tankowania WHERE idsam = " + entry.getIdsamochoduZtankowania(), null);
		while(c.moveToNext()) {
			if(c.getInt(0) == entry.getIdTank()) {
				if(c.getCount() > 1 && position != 0) {
				c.moveToPrevious(); 
				}
				previousTankOdometer = c.getDouble(1);
				break;
			}				
		}
		
		if(previousTankOdometer != 0.0) {	
		spalanie =  entry.getLitry() * 100 / (entry.getPrzebieg() - previousTankOdometer); 
		}
		else spalanie = 0.0;
		
		if(position != 0) {
		TextView spalanieField = (TextView) findViewById(R.id.wydajnosc_paliwa);
		spalanieField.setText(roundTwoDecimals(spalanie)); 
		}

		TextView paliwo = (TextView) findViewById(R.id.rodzaj_paliwa);
		paliwo.setText(entry.getRodzajPaliwa());

		TextView stacja = (TextView) findViewById(R.id.stacja);
		stacja.setText(entry.getStacja());

		TextView notki = (TextView) findViewById(R.id.notatki);
		notki.setText(entry.getNotki());

		TextView PreviousTankDate = (TextView) findViewById(R.id.poprzednie_tankowanie);
		String PoprzednieTankowanie = "b/d";
		if (position != 0) {
			PoprzednieTankowanie = previousEntry.getDate(); 
		}
		
		PreviousTankDate.setText(PoprzednieTankowanie);

		TextView NextTankDate = (TextView) findViewById(R.id.nastepne_tankowanie);
		String NastepneTankowanie = "b/d";
		if ((DataContainer.listOfRefuels.size() - 1) != position) {
			NastepneTankowanie = nextEntry.getDate();
		}
		NextTankDate.setText(NastepneTankowanie);
	}

	String roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return twoDForm.format(d);
	}
	public void backButton (View v) {
	   finish();
	}
	
	public void editButton (View v) {
		Intent k = new Intent(this, TankowaniaEdit.class);
		k.putExtra("list_position", position);
		k.putExtra("idsam", entry.getIdsamochoduZtankowania());
		startActivity(k);
	}
}