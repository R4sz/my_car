package rasz.app.My_Car;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static rasz.app.My_Car.DataBase_stale.*;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import rasz.app.My_Car.repository.FillupsRepository;

public class TankowaniaEdit extends Activity implements OnItemSelectedListener {
	private EditText tankowaniaPrzebieg;
	private EditText tankowaniaLitry;
	private EditText tankowaniaCenaLitra;
	private EditText tankowaniaKosztCalosc;
	private int idsam;
	static String wyborSamochodu;
	static String wyborPaliwa;
	String[] paliwa = { "PB95", "PB98", "PB95 EXTRA", "PB98 EXTRA", "PB100", "ON", "ON EXTRA", "LPG", "CNG" };
	private String paliwo;
	private Button btnChangeDate;
	private Button btnChangeTime;
	private int year;
	private int month;
	private int day;

	private int mhour;
	private int mminute;

	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 0;

	private String dateTank;
	private String timeTank;
	private long dateInMillis = 0;

	private EditText tankowaniaStacja;
	private EditText tankowaniaNotatki;
	private double przebiegStary;
	private double spalanie;
	private int idListForExtraMenu;
	private int idsamztnk;
	private String carName;
	private String paliwoForSpinner;
	private int idTank;
	private double przejechane;
	private double przebiegNowy;

	TankowaniaSimpleTankInfo tankEntry = new TankowaniaSimpleTankInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tankowania);
		initUiElements();

		DataContainer.database.close();
		spinnerSam();
		spinnerPal();
		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentTameOnView();
		addListenerOnButtonTime();

	}

	// TIME PICKER
	public void setCurrentTameOnView() {
		btnChangeTime = (Button) findViewById(R.id.tankowania_button_czas);
		btnChangeTime.setText(timeTank);
	}

	public void addListenerOnButtonTime() {
		btnChangeTime = (Button) findViewById(R.id.tankowania_button_czas);
		btnChangeTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mhour = hourOfDay;
			mminute = minute;

			String hr = String.format("%02d", mhour);
			String mt = String.format("%02d", mminute);
			String sec = "00";

			timeTank = hr + ":" + mt + ":" + sec;
			btnChangeTime.setText(timeTank);
		}
	};

	// DATA PICKER
	public void setCurrentDateOnView() {
		btnChangeDate = (Button) findViewById(R.id.tankowania_button_data);

		btnChangeDate.setText(dateTank);
	}

	public void addListenerOnButton() {
		btnChangeDate = (Button) findViewById(R.id.tankowania_button_data);
		btnChangeDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			year = 2012;
			month = 0;
			day = 1;
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mhour, mminute, false);
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
			String mt = String.format("%02d", month + 1);
			String dy = String.format("%02d", day);

			dateTank = dy + "-" + mt + "-" + yr;

			Date dateDateTank = null;
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dateDateTank = (Date) format.parse(dateTank);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dateInMillis = dateDateTank.getTime();

			btnChangeDate.setText(dateTank);

			Toast.makeText(getApplicationContext(), dateTank, Toast.LENGTH_LONG).show();
		}
	};

	// SPINNER
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void spinnerSam() {
		Spinner sam = (Spinner) findViewById(R.id.tankowania_samochod);
		sam.setOnItemSelectedListener(this);

		Intent getIdsam = getIntent();
		idsam = getIdsam.getIntExtra("idsam", 0);

		carName = DataContainer.getDataFromDB(this, "SELECT nazwa FROM samochody WHERE idsam = " + idsam, "single");

		DataContainer.getCarNamesForSpinner();
		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, DataContainer.samochodyNazwy);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sam.setAdapter(aa);

		ArrayAdapter<String> myAdap = (ArrayAdapter) sam.getAdapter(); 
		
		int spinnerPosition = myAdap.getPosition(carName);
		// set the default according to value
		sam.setSelection(spinnerPosition);
	}

	public Cursor getCarNameForSpinner() {
		// and get car odometer for update db
		SQLiteDatabase bd = DataContainer.database.getReadableDatabase();
		Cursor kursor = bd.rawQuery("Select nazwa, przebieg FROM samochody WHERE idsam = " + idsamztnk, null);
		startManagingCursor(kursor);

		return kursor;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void spinnerPal() {
		Spinner pal = (Spinner) findViewById(R.id.tankowania_paliwo);
		pal.setOnItemSelectedListener(this);
		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paliwa);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pal.setAdapter(aa);

		ArrayAdapter<String> myAdap = (ArrayAdapter) pal.getAdapter();

		int spinnerPosition = myAdap.getPosition(paliwoForSpinner);
		// set the default according to value
		pal.setSelection(spinnerPosition);

	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.tankowania_samochod) {
			wyborSamochodu = (parent.getItemAtPosition(position)).toString();
			idsam = DataContainer.samochodyId.get(position);

		}

		if (spnir.getId() == R.id.tankowania_paliwo)
			wyborPaliwa = (parent.getItemAtPosition(position)).toString();
		paliwo = paliwa[position];
	}

	public void onNothingSelected(AdapterView<?> parent) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.tankowania_paliwo)
			wyborPaliwa = "paliwo nieokreslone";
		if (spnir.getId() == R.id.tankowania_samochod)
			wyborSamochodu = "samoch�d nieokre�lony";
	}

	private void initUiElements() {
		TextView label = (TextView) findViewById(R.id.tankowania_label);
		label.setText("Edytuj Tankowanie...");

		Intent intent = getIntent();
		idListForExtraMenu = intent.getIntExtra("list_position", 0);
		FillupsRepository tankEntry = DataContainer.listOfRefuels.get(idListForExtraMenu);

		idsamztnk = tankEntry.getIdsamochoduZtankowania();
		paliwoForSpinner = tankEntry.getRodzajPaliwa();
		timeTank = tankEntry.getTime();
		dateTank = tankEntry.getDate();
		idTank = tankEntry.getIdTank();

		tankowaniaPrzebieg = (EditText) findViewById(R.id.tankowania_przebieg);
		tankowaniaPrzebieg.setText(Double.toString(tankEntry.getPrzebieg()));

		tankowaniaLitry = (EditText) findViewById(R.id.tankowania_ilosc_wlanych_litrow);
		tankowaniaLitry.setText(Double.toString(tankEntry.getLitry()));

		tankowaniaCenaLitra = (EditText) findViewById(R.id.tankowania_cena_za_litr);
		tankowaniaCenaLitra.setText(Double.toString(tankEntry.getCenaLitra()));

		tankowaniaKosztCalosc = (EditText) findViewById(R.id.tankowania_koszt_tankowania);
		tankowaniaKosztCalosc.setText(Double.toString(tankEntry.getWartosc()));

		tankowaniaStacja = (EditText) findViewById(R.id.tankowania_stacja);
		tankowaniaStacja.setText(tankEntry.getStacja());

		tankowaniaNotatki = (EditText) findViewById(R.id.tankowania_notatki);
		tankowaniaNotatki.setText(tankEntry.getNotki());
	}

	public void saveNewTank(View v) {
		String przebieg = tankowaniaPrzebieg.getText().toString();
		String litry = tankowaniaLitry.getText().toString();
		String cenaLitra = tankowaniaCenaLitra.getText().toString();
		String kosztCalosc = tankowaniaKosztCalosc.getText().toString();
		String stacja = tankowaniaStacja.getText().toString();
		String notatki = tankowaniaNotatki.getText().toString();
		String idsamString = Integer.toString(idsam);

		SQLiteDatabase db = DataContainer.database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT idtankowania, przebieg FROM tankowania WHERE idsam = " + idsam + " ORDER BY datawmms ASC", null);
		while (c.moveToNext()) {
			if (c.getCount() == 1) {
				przebiegStary = c.getDouble(1);
			} else if (c.getInt(0) == idTank) {
				c.moveToPrevious();
				przebiegStary = c.getDouble(1);
				break;
			}
		}

		if (c.getCount() == 1) {
			przebiegNowy = Double.parseDouble(przebieg);
			przejechane = przebiegNowy - przebiegStary;
			spalanie = Double.parseDouble(litry) * 100 / przejechane;

			String text = Double.toString(spalanie);
			Toast.makeText(getApplicationContext(), text + "/100km", Toast.LENGTH_LONG).show();
		}

		if (przebieg.equals("")) {
			tankowaniaPrzebieg.setError("To pole jest wymagane");
		}
		if (litry.equals("")) {
			tankowaniaLitry.setError("To pole jest wymagane");
		}
		if (cenaLitra.equals("")) {
			tankowaniaCenaLitra.setError("To pole jest wymagane");
		} else {
			DataContainer.database = new DatabaseDaneDB(this);
			try {

				addCarToDB(idsamString, przebieg, litry, cenaLitra, kosztCalosc, paliwo, stacja, notatki, dateTank, timeTank);
			} finally {
				DataContainer.database.close();
			}
		}
	}

	private void addCarToDB(String idsamString, String przebieg, String litry, String cenaLitra, String kosztCalosc, String paliwo, String stacja,
			String notatki, String date, String time) {

		Double przebiegUpdt = 0.0;

		SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		wartosci.put(KEY_ID_SAMOCHODU, idsamString);
		wartosci.put(KEY_PRZEBIEG, przebieg);
		wartosci.put(KEY_ILOSC_WLANYCH_LITROW, litry);
		wartosci.put(KEY_CENA_ZA_LITR, cenaLitra);
		wartosci.put(KEY_CENA_TANKOWANIA, kosztCalosc);
		wartosci.put(KEY_RODZAJ_PALIWA, paliwo);
		wartosci.put(KEY_STACJA, stacja);
		wartosci.put(KEY_NOTATKI, notatki);
		wartosci.put(KEY_DATA, date);
		wartosci.put(KEY_TIME, time);
		wartosci.put(KEY_SPALANIE, spalanie);
		wartosci.put(KEY_PRZEJECHANE, przejechane);
		wartosci.put(KEY_DATEMILLIS, dateInMillis);
		bd.update(DB_TANKOWANIA_TABLE, wartosci, "idtankowania" + "= " + idTank, null);

		if (DataContainer.getCarMileage(Integer.parseInt(idsamString)) < Double.parseDouble(przebieg)) {
			przebiegUpdt = Double.parseDouble(przebieg);
		}

		else if (DataContainer.getCarMileage(Integer.parseInt(idsamString)) == tankEntry.getPrzebieg()) {

			Double lastMaintOdometer = Double.parseDouble(DataContainer.getDataFromDB(this, "SELECT przebieg FROM maintance WHERE idsam = " + idsam
					+ " ORDER BY przebieg DESC", "single"));

			Double lastServOdometer = Double.parseDouble(DataContainer.getDataFromDB(this, "SELECT przebieg FROM naprawy WHERE idsam = " + idsam
					+ " ORDER BY przebieg DESC", "single"));

			if ((Math.max(lastMaintOdometer, lastServOdometer)) > Double.parseDouble(przebieg)) {
				przebiegUpdt = (Math.max(lastMaintOdometer, lastServOdometer));
			} else {
				przebiegUpdt = Double.parseDouble(przebieg);
			}
		}
		if (przebiegUpdt != 0.0) {
			ContentValues wartosci1 = new ContentValues();
			wartosci1.put(KEY_PRZEBIEG, przebiegUpdt);
			bd.update(DB_SAMOCHODY_TABLE, wartosci1, "idsam = " + idsam, null);
		}

		finish();
		Toast.makeText(getApplicationContext(), "Tankowanie o przebiegu  " + przebieg + " zosta�o zapisane poprawnie", Toast.LENGTH_LONG).show();
	}

	public void backButton(View v) {
		finish();
	}
}