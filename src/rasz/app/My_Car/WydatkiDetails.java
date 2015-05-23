package rasz.app.My_Car;

import rasz.app.My_Car.repository.ExpensesRepository;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WydatkiDetails extends Activity {

	private int position = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maint_details);
		Initelements();
	}

	public void onRestart(Bundle savedInstanceState) {
		super.onRestart();
		DataContainer.getDataFromDB(this, "SELECT m.* " + "FROM maintance as m, samochody as s " + "WHERE m.idsam = s.idsam " + "AND s.aktywny = 1 "
				+ "ORDER BY m.datawmms", "maintance");
		Initelements();
	}

	private void Initelements() {
		Intent maintListIntent = getIntent();
		position = maintListIntent.getIntExtra("pos", 0);
		ExpensesRepository MaintEntry = DataContainer.listOfMaints.get(position);

		TextView CarName = (TextView) findViewById(R.id.maintance_car_name);
		CarName.setText(WydatkiMaintAdapter.getCarName(MaintEntry));

		TextView mileage = (TextView) findViewById(R.id.maintance_car_odometer);
		mileage.setText(Double.toString(MaintEntry.getMileage()));

		TextView serviced = (TextView) findViewById(R.id.maintance_wydatek);
		serviced.setText(MaintEntry.getMaintanced());

		TextView date = (TextView) findViewById(R.id.maintance_date);
		date.setText(MaintEntry.getDate());

		TextView time = (TextView) findViewById(R.id.maintance_time);
		time.setText(MaintEntry.getTime());

		TextView place = (TextView) findViewById(R.id.maintance_place);
		place.setText(MaintEntry.getPlace());

		TextView notes = (TextView) findViewById(R.id.maintance_notes);
		notes.setText(MaintEntry.getNotes());
	}

	public void editButton(View v) {
		Intent i = new Intent(this, WydatkiEdit.class);
		i.putExtra("positionMaint", position);
		startActivity(i);
	}

	public void backButton(View v) {
		finish();
	}
}