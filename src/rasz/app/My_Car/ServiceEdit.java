package rasz.app.My_Car;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rasz.app.My_Car.repository.CarsRepository;
import rasz.app.My_Car.repository.ServicesRepository;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import static rasz.app.My_Car.DataBase_stale.*;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

public class ServiceEdit extends Activity implements OnClickListener, OnItemSelectedListener {
	private EditText service_przebieg;
	private EditText service_koszt_calosc;

	private int idsam;
	static String wybor_samochodu;

	static ArrayList<String> serwisowano = new ArrayList<String>();
	private String serwisowanoStr= "";
	protected CharSequence[] _options = { "silnik", "zawieszenie", "karoseria", "hamulce", "opony", "inne" };
	protected boolean[] _selections = new boolean[_options.length];

	private Button btnChangeDate;
	private Button btnChangeTime;
	private int year;
	private int month;
	private int day;

	private int mhour;
	private int mminute;
	private long dateInMillis;

	static final int TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 0;
	static final int SERVICE_DIALOG_ID = 2;

	private String dateServ;
	private String timeServ;

	private EditText serviceMiejsce;
	private EditText serviceNotatki;

	CarsRepository entry = new CarsRepository();
	ServicesRepository servEntry = new ServicesRepository();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service);

		getServData();
		initUiElements();
		spinnerSam();
		setCurrentDateOnView();
		addListenerOnButton();
		setCurrentTimeOnView();
		addListenerOnButtonTime();

	}

	public void onClick(View v) {
		switch (v.getId()) {		
		case R.id.serwisowano_button:
			showDialog(2);
		}
	}

	public void getServData() {
		Intent in = getIntent();
		servEntry = DataContainer.listOfServs.get(in.getIntExtra("positionServ", 0));
	}

	// TIME PICKER
	public void setCurrentTimeOnView() {
		btnChangeTime = (Button) findViewById(R.id.service_button_czas);
		btnChangeTime.setText(servEntry.getTime());
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

		btnChangeDate.setText(servEntry.getDate());
	}

	public void addListenerOnButton() {
		btnChangeDate = (Button) findViewById(R.id.service_button_data);
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
			String mt = String.format("%02d", month + 1);
			String dy = String.format("%02d", day);

			dateServ = dy + "-" + mt + "-" + yr;

			btnChangeDate.setText(dateServ);

			Date DateDateTank = null;
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			try {
				DateDateTank = (Date) format.parse(dateServ);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dateInMillis = DateDateTank.getTime();
		}
	};

	// MULTI CHECKBOX DIALOG MENU
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mhour, mminute, false);
		case SERVICE_DIALOG_ID:
			String serwisowanoArray[] = new String[_selections.length];
			serwisowanoArray = servEntry.getServiced().split(",");

			for (int i = 0; i < serwisowanoArray.length; i++) {
				for (int j = 0; j < _options.length; j++) {
					if (_options[j].equals(serwisowanoArray[i]))
						_selections[j] = true;
				}
			}

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
		serwisowano.clear();
		for (int i = 0; i < _options.length; i++) {
			Log.i("ME", _options[i] + " selected: " + _selections[i]);
			if (_selections[i]) {
				serwisowano.add(_options[i].toString());
				serwisowanoStr += _options[i].toString() + ",";
			}
		}
	}

	// SPINNER
	public void spinnerSam() {
		Spinner sam = (Spinner) findViewById(R.id.service_samochod);
		sam.setOnItemSelectedListener(this);

		DataContainer.getCarNamesForSpinner();

		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DataContainer.samochodyNazwy);
		aa.setDropDownViewResource(

		android.R.layout.simple_spinner_dropdown_item);
		sam.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Spinner spnir = (Spinner) parent;
		if (spnir.getId() == R.id.service_samochod) {
			wybor_samochodu = (parent.getItemAtPosition(position)).toString();
			idsam = DataContainer.samochodyId.get(position);
			entry = DataContainer.listOfCars.get(position);
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		Spinner spnir = (Spinner) parent;

		if (spnir.getId() == R.id.service_samochod)
			wybor_samochodu = "samochód nieokreœlony";
	}

	private void initUiElements() {		
		TextView label = (TextView) findViewById(R.id.service_label);
		label.setText("Edytuj Serwis...");
		
		View przyciskSerwisowano = findViewById(R.id.serwisowano_button);
		przyciskSerwisowano.setOnClickListener(this);

		service_przebieg = (EditText) findViewById(R.id.service_przebieg);
		service_koszt_calosc = (EditText) findViewById(R.id.service_koszt_serwisowania);
		serviceMiejsce = (EditText) findViewById(R.id.service_place);
		serviceNotatki = (EditText) findViewById(R.id.service_notatki);
		service_przebieg.setText(Double.toString(servEntry.getMileage()));
		service_koszt_calosc.setText(Double.toString(servEntry.getCost()));
		serviceMiejsce.setText(servEntry.getPlace());
		serviceNotatki.setText(servEntry.getNotes());
	}


	public void saveNewServ(View v) {
		String przebieg = service_przebieg.getText().toString();

		String kosztCalosc = service_koszt_calosc.getText().toString();
		String miejsce = serviceMiejsce.getText().toString();
		String notatki = serviceNotatki.getText().toString();
		String idsamString = Integer.toString(idsam);

		DataContainer.database = new DatabaseDaneDB(this);
		try {
			updServToDB(idsamString, przebieg, kosztCalosc, serwisowanoStr, dateServ, timeServ, miejsce, notatki, dateInMillis);
		} finally {
			DataContainer.database.close();
		}
	}

	private void updServToDB(String idsamString, String przebieg, String kosztCalosci, String serwisowano, String date, String time, String miejsce,
			String notatki, Long dateInMillis) {
		double przebiegUpdt = 0.0;

		SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		wartosci.put(KEY_ID_SAMOCHODU, idsamString);
		wartosci.put(KEY_PRZEBIEG, przebieg);
		wartosci.put(KEY_KOSZT_SERWISU, kosztCalosci);
		wartosci.put(KEY_SERWISOWANO, serwisowano);
		wartosci.put(KEY_MIEJSCE, miejsce);
		wartosci.put(KEY_DATA, date);
		wartosci.put(KEY_TIME, time);
		wartosci.put(KEY_NOTATKI, notatki);
		wartosci.put(KEY_DATEMILLIS, dateInMillis);
		bd.update(DB_NAPRAWY_TABLE, wartosci, "idnaprawy = " + servEntry.getIdserv(), null);

		if (DataContainer.getCarMileage(Integer.parseInt(idsamString)) < Double.parseDouble(przebieg)) {
			przebiegUpdt = Double.parseDouble(przebieg);
		}

		else if (DataContainer.getCarMileage(Integer.parseInt(idsamString)) == servEntry.getMileage()) {

			Double lastTankOdometer = Double.parseDouble(DataContainer.getDataFromDB(this, "SELECT przebieg FROM tankowania WHERE idsam = " + idsam
					+ " ORDER BY przebieg DESC", "single"));

			Double lastMaintOdometer = Double.parseDouble(DataContainer.getDataFromDB(this, "SELECT przebieg FROM maintance WHERE idsam = " + idsam
					+ " ORDER BY przebieg DESC", "single"));

			if ((Math.max(lastTankOdometer,lastMaintOdometer)) > Double.parseDouble(przebieg)) {			
				przebiegUpdt = (Math.max(lastTankOdometer,lastMaintOdometer));
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
		Toast.makeText(getApplicationContext(), "Dane zosta³y zaktualizowane poprawnie", Toast.LENGTH_LONG).show();

	}
	public void backButton(View v) {
		finish();
	}
}