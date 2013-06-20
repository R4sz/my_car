package rasz.app.My_Car;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import static rasz.app.My_Car.DataBase_stale.*;

public class SamochodyDodaj extends Activity implements OnItemSelectedListener {
	private EditText samochodyNazwa;
	private EditText samochodyMarka;
	private EditText samochodyModel;
	private EditText samochodyRokProdukcji;
	private EditText samochodyAktualnyPrzebieg;
	private EditText samochodyPojemnoscSilnika;
	private EditText samochodySymbolSilnika;
	private EditText samochodyVin;
	private EditText samochodyPrice;
	static String wyborPaliwa;
	String[] paliwa = { "benzyna", "benzyna + LPG", "benzyna + CNG", "diesel" };

	private Button btnChangeDate;;
	private int year;
	private int month;
	private int day;
	private String pucharseDate;
	private long dateInMillis = 0;

	static final int DATE_DIALOG_ID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.samochody);

		initUiElements();
		initSpinner();
		setCurrentDateOnView();
	}

	public void setCurrentDateOnView() {
		btnChangeDate = (Button) findViewById(R.id.samochody_pucharse_time);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		String yr = Integer.toString(year);
		String mt = String.format("%02d", month + 1);
		String dy = String.format("%02d", day);

		pucharseDate = dy + "-" + mt + "-" + yr;
		btnChangeDate.setText(pucharseDate);

		getActualDate();
	}

	public void setDate(View v) {

		showDialog(DATE_DIALOG_ID);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			String yr = Integer.toString(year);
			int month2 = month + 1;
			String mt = String.format("%02d", month2);
			String dy = String.format("%02d", day);

			pucharseDate = dy + "-" + mt + "-" + yr;

			getActualDate();
		}
	};

	private void getActualDate() {
		Date dateDatePucharse = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			dateDatePucharse = (Date) format.parse(pucharseDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dateInMillis = dateDatePucharse.getTime();
		btnChangeDate.setText(pucharseDate);
	}

	// SPINNER
	@SuppressWarnings("rawtypes")
	public void initSpinner() {
		Spinner spin = (Spinner) findViewById(R.id.paliwo);
		spin.setOnItemSelectedListener(this);
		@SuppressWarnings("unchecked")
		ArrayAdapter<?> aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paliwa);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		wyborPaliwa = (parent.getItemAtPosition(position)).toString();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		wyborPaliwa = "paliwo nieokreslone";
	}

	private void initUiElements() {
		samochodyNazwa = (EditText) findViewById(R.id.samochody_nazwa);
		samochodyMarka = (EditText) findViewById(R.id.samochody_marka);
		samochodyModel = (EditText) findViewById(R.id.samochody_model);
		samochodyRokProdukcji = (EditText) findViewById(R.id.samochody_rok_produkcji);
		samochodyAktualnyPrzebieg = (EditText) findViewById(R.id.samochody_aktualny_przebieg);
		samochodyPojemnoscSilnika = (EditText) findViewById(R.id.samochody_pojemnosc_silnika);
		samochodySymbolSilnika = (EditText) findViewById(R.id.samochody_symbol_silnika);
		samochodyVin = (EditText) findViewById(R.id.samochody_vin);
		samochodyPrice = (EditText) findViewById(R.id.samochody_price);
	}

	public void saveNewCar(View v) {
		String nazwa = samochodyNazwa.getText().toString();
		String marka = samochodyMarka.getText().toString();
		String model = samochodyModel.getText().toString();
		String rok = samochodyRokProdukcji.getText().toString();
		String aktualnyPrzebieg = samochodyAktualnyPrzebieg.getText().toString();
		String pojemnoscSilnika = samochodyPojemnoscSilnika.getText().toString();
		String symbol = samochodySymbolSilnika.getText().toString();
		String vin = samochodyVin.getText().toString();
		String price = samochodyPrice.getText().toString();

		if (nazwa.equals("")) {
			samochodyNazwa.setError("To pole jest wymagane");
		}

		if (aktualnyPrzebieg.equals("")) {
			samochodyAktualnyPrzebieg.setError("To pole jest wymagane");
		}

		if (price.equals("")) {
			samochodyPrice.setError("To pole jest wymagane");
		}

		else {
			DataContainer.database = new DatabaseDaneDB(this);
			try {
				addCarToDB(nazwa, marka, model, rok, aktualnyPrzebieg, wyborPaliwa, pojemnoscSilnika, symbol, vin, dateInMillis, price);
			} finally {
				DataContainer.database.close();
			}
		}
	}

	private void addCarToDB(String nazwa, String marka, String model, String rok, String przebieg, String paliwo, String pojSilnika, String symbol, String vin,
			Long dateInMillis, String price) {

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
		wartosci.put(KEY_ACTIVE, "1");
		wartosci.put(KEY_PUCHARSE_TIME, dateInMillis);
		wartosci.put(KEY_PRZEBIEG_START, przebieg);
		wartosci.put(KEY_PUCHARSE_PRICE, price);
		bd.insertOrThrow(DB_SAMOCHODY_TABLE, null, wartosci);

		Toast.makeText(getApplicationContext(), "Samochód " + nazwa + " o przebiegu " + przebieg + " zosta³ dodany", Toast.LENGTH_LONG).show();
		finish();
	}

	public void backButton(View v) {
		finish();
	}
}