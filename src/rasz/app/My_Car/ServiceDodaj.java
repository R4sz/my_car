package rasz.app.My_Car;

import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rasz.app.My_Car.repository.CarsRepository;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

public class ServiceDodaj extends Activity implements OnClickListener, OnItemSelectedListener {
	private EditText servicePrzebieg;
	private EditText serviceKosztCalosc;

	private int idsam;
	static String wyborSamochodu;

	private String serwisowanoStr = "";
	protected CharSequence[] _options = { "silnik", "zawieszenie", "karoseria", "hamulce", "opony", "inne" };
	protected boolean[] _selections = new boolean[_options.length];

	private Button btnChangeDate;
	private Button btnChangeTime;
	private Button servicedButton;
	private int year;
	private int month;
	private int day;

	private int mhour;
	private int mminute;
	private int sec;
	private long dateInMillis = 0;

	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 0;
	static final int SERVICE_DIALOG_ID = 2;

	private String dateServ;
	private String timeServ;

	private EditText serviceMiejsce;
	private EditText serviceNotatki;
	private String przebieg;
	
	CarsRepository entry = new CarsRepository();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service);

		initUiElements();
		spinnerSam();
		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentTameOnView();
		addListenerOnButtonTime();
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.serwisowano_button:
			showDialog(2);
		}
	}

	// TIME PICKER
	public void setCurrentTameOnView() {
		btnChangeTime = (Button) findViewById(R.id.service_button_czas);
		final Calendar c = Calendar.getInstance();
		mhour = c.get(Calendar.HOUR_OF_DAY);
		mminute = c.get(Calendar.MINUTE);
		sec = c.get(Calendar.SECOND);

		String hr = String.format("%02d", mhour);
		String mt = String.format("%02d", mminute);
		String sc = String.format("%02d", sec);

		timeServ = hr + ":" + mt + ":" + sc;
		btnChangeTime.setText(timeServ);
	}

	public void addListenerOnButtonTime() {
		btnChangeTime = (Button) findViewById(R.id.service_button_czas);
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

			timeServ = hr + ":" + mt + ":" + sec;
			btnChangeTime.setText(timeServ);
		}
	};

	// DATA PICKER
	public void setCurrentDateOnView() {
		btnChangeDate = (Button) findViewById(R.id.service_button_data);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		String yr = Integer.toString(year);
		String mt = String.format("%02d", month + 1);
		String dy = String.format("%02d", day);

		dateServ = dy + "." + mt + "." + yr;
		btnChangeDate.setText(dateServ);

	}

	public void addListenerOnButton() {
		btnChangeDate = (Button) findViewById(R.id.service_button_data);
		btnChangeDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	// MULTI CHECKBOX DIALOG MENU
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mhour, mminute, false);
		case SERVICE_DIALOG_ID:
			_selections[_selections.length - 1] = true;

			return new AlertDialog.Builder(this).setTitle("Services").setMultiChoiceItems(_options, _selections, new DialogSelectionClickHandler())
					.setPositiveButton("OK", new DialogButtonClickHandler()).create();
		}
		return null;
	}

	public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
		public void onClick(DialogInterface dialog, int clicked, boolean selected) {
			Log.i("ME", _options[clicked] + " selected: " + selected);
		}
	}

	public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int clicked) {
			switch (clicked) {
			case DialogInterface.BUTTON_POSITIVE:
				printSelectedServices();
				break;
			}
		}
	}

	protected void printSelectedServices() {
		serwisowanoStr = "";
		for (int i = 0; i < _options.length; i++) {
			Log.i("ME", _options[i] + " selected: " + _selections[i]);
			if (_selections[i]) {
				serwisowanoStr += _options[i].toString() + ",";				
			}
		}
		if(serwisowanoStr.length()>2) {
			servicedButton.setError(null);
		}
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

			dateServ = dy + "-" + mt + "-" + yr;
			btnChangeDate.setText(dateServ);

			Date dateDateTank = null;
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dateDateTank = (Date) format.parse(dateServ);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dateInMillis = dateDateTank.getTime();
		}
	};

	// SPINNER
	public void spinnerSam() {
		Spinner sam = (Spinner) findViewById(R.id.service_samochod);
		sam.setOnItemSelectedListener(this);

		DataContainer.getCarNamesForSpinner();

		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataContainer.samochodyNazwy);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sam.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.service_samochod) {
			wyborSamochodu = (parent.getItemAtPosition(position)).toString();
			idsam = DataContainer.samochodyId.get(position);
			entry = DataContainer.listOfCars.get(position);
			servicePrzebieg.setText(Long.toString(entry.getPrzebieg()));
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		Spinner spnir = (Spinner) parent;

		if (spnir.getId() == R.id.service_samochod)
			wyborSamochodu = "samochód nieokreœlony";
	}

	private void initUiElements() {
		View przyciskSerwisowano = findViewById(R.id.serwisowano_button);
		przyciskSerwisowano.setOnClickListener(this);

		servicePrzebieg = (EditText) findViewById(R.id.service_przebieg);
		serviceKosztCalosc = (EditText) findViewById(R.id.service_koszt_serwisowania);
		serviceMiejsce = (EditText) findViewById(R.id.service_place);
		serviceNotatki = (EditText) findViewById(R.id.service_notatki);
		servicedButton = (Button) findViewById(R.id.serwisowano_button);
	}

	private double getCarMileage(int idsam) {
		SQLiteDatabase db = DataContainer.database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT przebieg FROM samochody WHERE idsam = " + idsam, null);
		while (c.moveToNext()) {
			double przebieg = c.getDouble(0);
			return przebieg;
		}
		return 0.0;
	}

	public void saveNewServ(View v) {
		przebieg = servicePrzebieg.getText().toString();
		String kosztCalosc = serviceKosztCalosc.getText().toString();
		String miejsce = serviceMiejsce.getText().toString();
		String notatki = serviceNotatki.getText().toString();
		String idsamString = Integer.toString(idsam);

		if (serwisowanoStr.length()<2) {
			servicedButton.setError("Wymagany wybór kategorii");
		} else {
			DataContainer.database = new DatabaseDaneDB(this);
			try {
				addServToDB(idsamString, przebieg, kosztCalosc, serwisowanoStr, dateServ, timeServ, miejsce, notatki, dateInMillis);
			} finally {
				DataContainer.database.close();
			}
		}
	}

	private void addServToDB(String idsamString, String Przebieg, String KosztCalosci, String serwisowano, String Date, String Time, String Miejsce,
			String Notatki, Long DateInMillis) {

		SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		ContentValues wartosci1 = new ContentValues();
		wartosci.put(KEY_ID_SAMOCHODU, idsamString);
		wartosci.put(KEY_PRZEBIEG, Przebieg);
		wartosci.put(KEY_KOSZT_SERWISU, KosztCalosci);
		wartosci.put(KEY_SERWISOWANO, serwisowano);
		wartosci.put(KEY_MIEJSCE, Miejsce);
		wartosci.put(KEY_DATA, Date);
		wartosci.put(KEY_TIME, Time);
		wartosci.put(KEY_NOTATKI, Notatki);
		wartosci.put(KEY_DATEMILLIS, DateInMillis);
		bd.insertOrThrow(DB_NAPRAWY_TABLE, null, wartosci);

		if (getCarMileage(Integer.parseInt(idsamString)) < Double.parseDouble(Przebieg)) {
			wartosci1.put(KEY_PRZEBIEG, Przebieg);
			bd.update(DB_SAMOCHODY_TABLE, wartosci1, "idsam = " + Integer.parseInt(idsamString), null);
		}

		Toast.makeText(getApplicationContext(), "Naprawa o przebiegu  " + przebieg + " zosta³a dodana", Toast.LENGTH_LONG).show();
		finish();
	}

	public void backButton(View v) {
		finish();
	}
}