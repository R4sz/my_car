package rasz.app.My_Car;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static rasz.app.My_Car.DataBase_stale.*;

public class SamochodyEdit extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {

	private int idCar;
	private String paliwo;

	private EditText samochody_nazwa;
	private EditText samochody_marka;
	private EditText samochody_model;
	private EditText samochody_rok_produkcji;
	private EditText samochody_aktualny_przebieg;
	private EditText samochody_pojemnosc_silnika;
	private EditText samochody_symbol_silnika;
	private EditText samochody_vin;
	private EditText samochodyPrice;
	static String wybor_paliwa;
	String[] paliwa = { "benzyna", "benzyna + LPG", "benzyna + CNG", "diesel" };

	private Button samochody_pucharse_time;

	public static CarsRepository entry = new CarsRepository();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.samochody);
		initTables();
		initUiElements();

		spinner();
	}

	void initTables() {
		Intent SamochodyLista = getIntent();
		int idZlistySam = SamochodyLista.getIntExtra("position", 0);
		entry = DataContainer.listOfCars.get(idZlistySam);
	}

	// SPINNER
	public void spinner() {
		Spinner spin = (Spinner) findViewById(R.id.paliwo);
		spin.setOnItemSelectedListener(this);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paliwa);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);

		// set spinner actual value
		String myString = paliwo;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter<String> myAdap = (ArrayAdapter) spin.getAdapter(); 																		
																		
		int spinnerPosition = myAdap.getPosition(myString);
		// set the default according to value
		spin.setSelection(spinnerPosition);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		wybor_paliwa = (parent.getItemAtPosition(position)).toString();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		wybor_paliwa = "paliwo nieokreslone";
	}
	public void initUiElements() {
		TextView label = (TextView) findViewById(R.id.samochody_label);
		label.setText("Edytuj Samoch�d...");

		idCar = entry.getId();
		paliwo = entry.getFuel();

		samochody_nazwa = (EditText) findViewById(R.id.samochody_nazwa);
		samochody_nazwa.setText(entry.getCarName());

		samochody_marka = (EditText) findViewById(R.id.samochody_marka);
		samochody_marka.setText(entry.getBrand());

		samochody_model = (EditText) findViewById(R.id.samochody_model);
		samochody_model.setText(entry.getModel());

		samochody_rok_produkcji = (EditText) findViewById(R.id.samochody_rok_produkcji);
		String rok = Integer.toString(entry.getProduceYear());
		samochody_rok_produkcji.setText(rok);

		samochody_aktualny_przebieg = (EditText) findViewById(R.id.samochody_aktualny_przebieg);
		String przeb = Long.toString(entry.getMileAge());
		samochody_aktualny_przebieg.setText(przeb);

		samochody_pojemnosc_silnika = (EditText) findViewById(R.id.samochody_pojemnosc_silnika);
		String poj = Double.toString(entry.getEngineCap());
		samochody_pojemnosc_silnika.setText(poj);

		samochody_symbol_silnika = (EditText) findViewById(R.id.samochody_symbol_silnika);
		samochody_symbol_silnika.setText(entry.getEngineSymbol());

		samochody_vin = (EditText) findViewById(R.id.samochody_vin);
		samochody_vin.setText(entry.getVin());

		samochodyPrice = (EditText) findViewById(R.id.samochody_price);
		samochodyPrice.setText(Double.toString(entry.getCost()));

		Date pucharseDate = new Date(entry.getTimestamp());
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		samochody_pucharse_time = (Button) findViewById(R.id.samochody_pucharse_time);
		samochody_pucharse_time.setText(df.format(pucharseDate));

	}

	public void saveNewCar(View v) {
		String nazwa = samochody_nazwa.getText().toString();
		String marka = samochody_marka.getText().toString();
		String model = samochody_model.getText().toString();
		String rok_produkcji = samochody_rok_produkcji.getText().toString();
		String aktualny_przebieg = samochody_aktualny_przebieg.getText().toString();
		String pojemnosc_silnika = samochody_pojemnosc_silnika.getText().toString();
		String symbol = samochody_symbol_silnika.getText().toString();
		String vin = samochody_vin.getText().toString();
		String price = samochodyPrice.getText().toString();

		// warunek obowiazkowych pol
		if (nazwa.equals("")) {
			samochody_nazwa.setError("To pole jest wymagane");
		}
		if (aktualny_przebieg.equals("")) {
			samochody_aktualny_przebieg.setError("To pole jest wymagane");
		}
		if (price.equals("")) {
			samochodyPrice.setError("To pole jest wymagane");
		} else {
			DataContainer.database = new DatabaseDaneDB(this);
			try {
				updCarData(nazwa, marka, model, rok_produkcji, aktualny_przebieg, wybor_paliwa, pojemnosc_silnika, symbol, vin, price);
				DataContainer.getDataFromDB(this, "SELECT * FROM Samochody", "cars");
				pokazZdarzenia();
			} finally {
				DataContainer.database.close();
			}
		}
	}
	
	private void updCarData(String nazwa, String marka, String model, String rok, String przebieg, String paliwo, String pojSilnika, String symbol, String vin,
			String price) {

		SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		wartosci.put(KEY_NAZWA, nazwa);
		wartosci.put(KEY_MARKA, marka);
		wartosci.put(KEY_MODEL, model);
		wartosci.put(KEY_ROK_PRODUKCJI, rok);
		wartosci.put(KEY_PRZEBIEG, przebieg);
		wartosci.put(KEY_PALIWO, paliwo);
		wartosci.put(KEY_POJEMNOSC_SILNIKA, pojSilnika);
		wartosci.put(KEY_SYMBOL_SILNIKA, symbol);
		wartosci.put(KEY_VIN, vin);
		wartosci.put(KEY_PUCHARSE_PRICE, price);
		bd.update(DB_SAMOCHODY_TABLE, wartosci, idCar + "=" + "idsam", null);
	}

	// wyswietlenie informacji
	private void pokazZdarzenia() {
		Toast.makeText(getApplicationContext(), "Dane zosta�y zaktualizowane poprawnie", Toast.LENGTH_LONG).show();
		finish();
	}

	public void onClick(View arg0) {		
	}

	public void backButton(View v) {
		finish();
	}
}