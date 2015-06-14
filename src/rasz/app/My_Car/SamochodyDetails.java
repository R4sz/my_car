package rasz.app.My_Car;

import static rasz.app.My_Car.DataBase_stale.DB_SAMOCHODY_TABLE;
import static rasz.app.My_Car.DataBase_stale.KEY_ACTIVE;

import java.sql.Date;
import java.text.SimpleDateFormat;

import rasz.app.My_Car.repository.CarsRepository;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SamochodyDetails extends Activity implements OnClickListener {

	private static int aktywny;
	private static int idZTablicy;
	private static int idZlistySam;

	public static CarsRepository entry = new CarsRepository();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.samochody_details);
		initTables();
		pokazZdarzenia();

		ToggleButton syncSwitch = (ToggleButton) findViewById(R.id.sam_aktywny);
		syncSwitch.setChecked(aktywny != 0);

		syncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setActive((isChecked) ? 1 : 0);
			}
		});
	}

	protected void onRestart() {
		super.onRestart();
		initTables();
		pokazZdarzenia();
	}

	void setActive(int active) {
		DataContainer.database = new DatabaseDaneDB(this);
		SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		wartosci.put(KEY_ACTIVE, active);
		bd.update(DB_SAMOCHODY_TABLE, wartosci, idZTablicy + "=" + "idsam", null);
		DataContainer.database.close();
		entry.setAktywny(active);
	}

	void setActive() {
		setActive(1);
	}

	void setDeactive() {
		setActive(0);
	}

	void initTables() {
		Intent samochody_lista = getIntent();
		idZlistySam = samochody_lista.getIntExtra("position", 0);
		entry = DataContainer.listOfCars.get(idZlistySam);
		idZTablicy = entry.getId();
		aktywny = entry.getAktywny();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.car_edit_button:
			Intent i = new Intent(this, SamochodyEdit.class);
			i.putExtra("position", idZlistySam);
			startActivity(i);
			break;
		}
	}

	public void pokazZdarzenia() {

		TextView carDetailsName = (TextView) findViewById(R.id.car_details_name);
		carDetailsName.setText(entry.getCarName());

		TextView carMark = (TextView) findViewById(R.id.car_mark);
		carMark.setText(entry.getBrand());

		TextView carModel = (TextView) findViewById(R.id.car_model);
		carModel.setText(entry.getModel());

		TextView rokProdukcji = (TextView) findViewById(R.id.rok_produkcji);
		rokProdukcji.setText(Integer.toString(entry.getProduceYear()));

		TextView carDetailsPrzebieg = (TextView) findViewById(R.id.car_details_przebieg);
		carDetailsPrzebieg.setText(Long.toString(entry.getPrzebieg()));

		TextView carDetailsPojemnoscSilnika = (TextView) findViewById(R.id.car_details_pojemnosc_silnika);
		carDetailsPojemnoscSilnika.setText(Double.toString(entry.getPoj_silnika()));

		TextView carDetailsPaliwo = (TextView) findViewById(R.id.car_details_paliwo);
		carDetailsPaliwo.setText(entry.getFuel());

		TextView carDetailsSymbolSilnika = (TextView) findViewById(R.id.car_details_symbol_silnika);
		carDetailsSymbolSilnika.setText(entry.getSymbol_silnika());

		TextView carDetailsVin = (TextView) findViewById(R.id.car_details_vin);
		carDetailsVin.setText(entry.getVin());

		TextView price = (TextView) findViewById(R.id.car_details_pucharse_price);
		price.setText(Double.toString(entry.getPrice()));

		Date pucharseDate = new Date(entry.getDateInMms());
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		TextView date = (TextView) findViewById(R.id.car_details_pucharse_date);
		date.setText(df.format(pucharseDate));

		View przyciskEdytujSamochod = findViewById(R.id.car_edit_button);
		przyciskEdytujSamochod.setOnClickListener(this);
	}

	public void backButton(View v) {
		finish();
	}
}