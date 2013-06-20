package rasz.app.My_Car;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
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
import android.widget.Toast;
import static rasz.app.My_Car.DataBase_stale.*;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

public class WydatkiDodaj extends Activity implements OnItemSelectedListener {
	private EditText maintancePrzebieg;
	private EditText maintanceKoszt;

	private int idsam;
	static String wyborSamochodu;
	static ArrayList<String> samochodyNazwy = new ArrayList<String>();
	static ArrayList<Integer> samochodyId = new ArrayList<Integer>();
	String maintance[] = { "Myjnia", "Ubezpiecznienie", "Przegl¹d", "Parking", "Podatek", "Eksploatacja", "Inne" };
	private String maintanceChoose;

	private Button btnChangeDate;
	private Button btnChangeTime;
	private int year;
	private int month;
	private int day;

	private int mhour;
	private int mminute;
	private int sec;
	private Long dateInMillis;

	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 0;

	private String dateMaint;
	private String timeMaint;

	private EditText maintanceMiejsce;
	private EditText maintanceNotatki;

	SamochodySimpleCarInfo entry = new SamochodySimpleCarInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintance);

		initUiElements();
		spinnerSam();
		spinnerMaintance();
		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentTimeOnView();
		addListenerOnButtonTime();
	}

	// TIME PICKER
	public void setCurrentTimeOnView() {
		btnChangeTime = (Button) findViewById(R.id.maintance_button_czas);
		final Calendar c = Calendar.getInstance();
		mhour = c.get(Calendar.HOUR_OF_DAY);
		mminute = c.get(Calendar.MINUTE);
		sec = c.get(Calendar.SECOND);

		String hr = String.format("%02d", mhour);
		String mt = String.format("%02d", mminute);
		String sc = String.format("%02d", sec);

		timeMaint = hr + ":" + mt + ":" + sc;
		btnChangeTime.setText(timeMaint);
	}

	public void addListenerOnButtonTime() {
		btnChangeTime = (Button) findViewById(R.id.maintance_button_czas);
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

			timeMaint = hr + ":" + mt + ":" + sec;
			btnChangeTime.setText(timeMaint);
		}
	};

	// DATA PICKER
	public void setCurrentDateOnView() {
		btnChangeDate = (Button) findViewById(R.id.maintance_button_data);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		String yr = Integer.toString(year);
		int month2 = month + 1;
		String mt = String.format("%02d", month2);
		String dy = String.format("%02d", day);

		dateMaint = dy + "-" + mt + "-" + yr;
		btnChangeDate.setText(dateMaint);
	}

	public void addListenerOnButton() {
		btnChangeDate = (Button) findViewById(R.id.maintance_button_data);
		btnChangeDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
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

			dateMaint = dy + "-" + mt + "-" + yr;
			btnChangeDate.setText(dateMaint);

			Date dateDateMaint = null;
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dateDateMaint = (Date) format.parse(dateMaint);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dateInMillis = dateDateMaint.getTime();
		}
	};

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

	// SPINNER
	public void spinnerSam() {
		Spinner sam = (Spinner) findViewById(R.id.maintance_samochod);
		sam.setOnItemSelectedListener(this);
		samochodyNazwy.clear();
		samochodyId.clear();

		for (int i = 0; i < DataContainer.listOfCars.size(); i++) {
			entry = DataContainer.listOfCars.get(i);
			samochodyNazwy.add(entry.getNazwa());
			samochodyId.add(entry.getIdsam());
		}

		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, samochodyNazwy);
		aa.setDropDownViewResource(

		android.R.layout.simple_spinner_dropdown_item);
		sam.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.maintance_samochod) {
			wyborSamochodu = (parent.getItemAtPosition(position)).toString();
			idsam = samochodyId.get(position);
			entry = DataContainer.listOfCars.get(position);
			maintancePrzebieg.setText(Long.toString(entry.getPrzebieg()));
		}
		if (spnir.getId() == R.id.maintance_maintanced) {
			maintanceChoose = (parent.getItemAtPosition(position)).toString();
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.maintance_samochod)
			wyborSamochodu = "samochód nieokreœlony";
		if (spnir.getId() == R.id.maintance_maintanced) {
			maintanceChoose = "wydatek nieokreœlony";
		}

	}

	// SPINNER MAINTANCE
	public void spinnerMaintance() {
		Spinner spin = (Spinner) findViewById(R.id.maintance_maintanced);
		spin.setOnItemSelectedListener(this);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, maintance);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);
	}

	private void initUiElements() {

		maintancePrzebieg = (EditText) findViewById(R.id.maintance_przebieg);
		maintanceKoszt = (EditText) findViewById(R.id.maintance_koszt);
		maintanceMiejsce = (EditText) findViewById(R.id.maintance_place);
		maintanceNotatki = (EditText) findViewById(R.id.maintance_notatki);
	}

	private double getCarMileAge(int idsam) {
		SQLiteDatabase db = DataContainer.database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT przebieg FROM samochody WHERE idsam = " + idsam, null);
		startManagingCursor(c);
		while (c.moveToNext()) {
			Double przebieg = c.getDouble(0);
			return przebieg;
		}
		return 0.0;
	}

	public void saveNewMaint(View v) {
		String przebieg = maintancePrzebieg.getText().toString();
		String kosztCalosc = maintanceKoszt.getText().toString();
		String miejsce = maintanceMiejsce.getText().toString();
		String notatki = maintanceNotatki.getText().toString();
		String idsamString = Integer.toString(idsam);

		DataContainer.database = new DatabaseDaneDB(this);
		try {
			addMaintToDB(idsamString, przebieg, kosztCalosc, maintanceChoose, dateMaint, timeMaint, miejsce, notatki, dateInMillis);
		} finally {
			DataContainer.database.close();
		}
	}

	private void addMaintToDB(String idsamString, String przebieg, String kosztCalosci, String maintanced, String date, String time, String miejsce,
			String notatki, Long dateInMillis) {

		SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		ContentValues wartosci1 = new ContentValues();
		wartosci.put(KEY_ID_SAMOCHODU, idsamString);
		wartosci.put(KEY_PRZEBIEG, przebieg);
		wartosci.put(KEY_KOSZT_MAINTANCE, kosztCalosci);
		wartosci.put(KEY_MAINTANCED, maintanced);
		wartosci.put(KEY_MIEJSCE, miejsce);
		wartosci.put(KEY_DATA, date);
		wartosci.put(KEY_TIME, time);
		wartosci.put(KEY_NOTATKI, notatki);
		wartosci.put(KEY_DATEMILLIS, dateInMillis);
		bd.insertOrThrow(DB_MAINTANCE_TABLE, null, wartosci);

		if (getCarMileAge(Integer.parseInt(idsamString)) < Double.parseDouble(przebieg)) {
			wartosci1.put(KEY_PRZEBIEG, przebieg);
			bd.update(DB_SAMOCHODY_TABLE, wartosci1, "idsam = " + Integer.parseInt(idsamString), null);
		}
		finish();
		Toast.makeText(getApplicationContext(), "Wydatek  " + maintanceChoose + " zosta³ dodany", Toast.LENGTH_LONG).show();

	}

	public void backButton(View v) {
		finish();
	}
}