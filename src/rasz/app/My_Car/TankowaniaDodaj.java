package rasz.app.My_Car;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import static rasz.app.My_Car.DataBase_stale.*;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import rasz.app.My_Car.repository.CarsRepository;

public class TankowaniaDodaj extends Activity implements OnItemSelectedListener {
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
	private int sec;

	private long dateInMillis = 0;
	
	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 0;

	private String dateTank;
	private String timeTank;
	private EditText tankowaniaStacja;
	private EditText tankowaniaNotatki;
	private double przebiegStary;
	private double spalanie;
	private double przejechane;

	static DecimalFormat decim = new DecimalFormat("#.##");

	CarsRepository entry = new CarsRepository();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tankowania);
		initUiElements();

		spinnerSam();
		spinnerPal();
		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentTimeOnView();
		addListenerOnButtonTime();
		initChange();
	}

	public void initChange() {
		tankowaniaCenaLitra.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String cenaL = tankowaniaCenaLitra.getText().toString();
				String iloscL = tankowaniaLitry.getText().toString();
				String przecinek = ",";

				if ((cenaL.length() > 0) && cenaL.charAt(cenaL.length() - 1) != przecinek.charAt(przecinek.length() - 1) && (iloscL.length() > 0)) {

					double wartosc = Double.parseDouble(cenaL) * Double.parseDouble(iloscL);
					tankowaniaKosztCalosc.setText(decim.format(wartosc));
				}
			}
		});
	}

	// TIME PICKER
	public void setCurrentTimeOnView() {
		btnChangeTime = (Button) findViewById(R.id.tankowania_button_czas);
		final Calendar c = Calendar.getInstance();
		mhour = c.get(Calendar.HOUR_OF_DAY);
		mminute = c.get(Calendar.MINUTE);
		sec = c.get(Calendar.SECOND);

		String hr = String.format("%02d", mhour);
		String mt = String.format("%02d", mminute);
		String sc = String.format("%02d", sec);

		timeTank = hr + ":" + mt + ":" + sc;
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
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		String yr = Integer.toString(year);
		String mt = String.format("%02d", month + 1);
		String dy = String.format("%02d", day);

		dateTank = dy + "." + mt + "." + yr;
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
			int month2 = month + 1;
			String mt = String.format("%02d", month2);
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
		}
	};

	// SPINNER
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void spinnerSam() {
		Spinner sam = (Spinner) findViewById(R.id.tankowania_samochod);
		sam.setOnItemSelectedListener(this);

		DataContainer.getCarNamesForSpinner();

		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, DataContainer.samochodyNazwy);
		aa.setDropDownViewResource(

		android.R.layout.simple_spinner_dropdown_item);
		sam.setAdapter(aa);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void spinnerPal() {
		Spinner pal = (Spinner) findViewById(R.id.tankowania_paliwo);
		pal.setOnItemSelectedListener(this);
		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paliwa);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pal.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.tankowania_samochod) {
			wyborSamochodu = (parent.getItemAtPosition(position)).toString();
			idsam = DataContainer.samochodyId.get(position);
			entry = DataContainer.listOfCars.get(position);
			tankowaniaPrzebieg.setText(Long.toString(entry.getPrzebieg()));
		}

		if (spnir.getId() == R.id.tankowania_paliwo)
			wyborPaliwa = (parent.getItemAtPosition(position)).toString();
		paliwo = paliwa[position];
	}

	public void onNothingSelected(AdapterView<?> parent) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.tankowania_paliwo)
			wyborPaliwa = "paliwoNieokreslone";
		if (spnir.getId() == R.id.tankowania_samochod)
			wyborSamochodu = "samoch�dNieokre�lony";
	}

	private void initUiElements() {

		tankowaniaPrzebieg = (EditText) findViewById(R.id.tankowania_przebieg);
		tankowaniaLitry = (EditText) findViewById(R.id.tankowania_ilosc_wlanych_litrow);
		tankowaniaCenaLitra = (EditText) findViewById(R.id.tankowania_cena_za_litr);
		tankowaniaKosztCalosc = (EditText) findViewById(R.id.tankowania_koszt_tankowania);
		tankowaniaStacja = (EditText) findViewById(R.id.tankowania_stacja);
		tankowaniaNotatki = (EditText) findViewById(R.id.tankowania_notatki);
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
			} else if (c.isLast()) {
				przebiegStary = c.getDouble(1);
				break;
			}
		}

		if (c.getCount() > 0) {
			double przebiegNowy = Double.parseDouble(przebieg);

			przejechane = przebiegNowy - przebiegStary;
			spalanie = Double.parseDouble(litry) * 100 / przejechane;

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
				addCarToDB(idsamString, przebieg, litry, cenaLitra, kosztCalosc, paliwo, stacja, notatki, dateTank, timeTank, dateInMillis);
			} finally {
				DataContainer.database.close();
			}
		}
	}

	private void addCarToDB(String idsamString, String przebieg, String litry, String cenaLitra, String kosztCalosc, String paliwo, String stacja,
			String notatki, String date, String time, Long dateInMillis) {

		SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		ContentValues wartosci1 = new ContentValues();
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
		bd.insertOrThrow(DB_TANKOWANIA_TABLE, null, wartosci);
		int idsamint = Integer.parseInt(idsamString);

		Double przebiegDouble = Double.parseDouble(przebieg);

		if (przebiegStary < przebiegDouble) {
			wartosci1.put(KEY_PRZEBIEG, przebieg);
			wartosci1.put(KEY_PRZEJECHANE, (entry.getPrzebieg() - przebiegDouble));
			bd.update(DB_SAMOCHODY_TABLE, wartosci1, idsamint + "=" + "idsam", null);
		}
		finish();
		Toast.makeText(getApplicationContext(), "Tankowanie o przebiegu  " + przebieg + " zosta�o dodane", Toast.LENGTH_LONG).show();

		Toast.makeText(getApplicationContext(), "Spalanie od ost. tankowania: " + decim.format(spalanie) + "/100km", Toast.LENGTH_LONG).show();
	}

	public void backButton(View v) {
		finish();
	}
}