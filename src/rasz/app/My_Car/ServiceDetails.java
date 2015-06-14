package rasz.app.My_Car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rasz.app.My_Car.repository.ServicesRepository;

public class ServiceDetails extends Activity {
	
	private int position = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serv_details);

		Initelements();
	}

	protected void onRestart() {
		super.onRestart();
		DataContainer.getDataFromDB(this, "SELECT * from naprawy", "service");
		Initelements();
	}	
	
	private void Initelements() {
		Intent ServlistIntent = getIntent();
		position = ServlistIntent.getIntExtra("pos", 0);
        ServicesRepository servEntry = DataContainer.listOfServs.get(position);

		TextView CarName = (TextView) findViewById(R.id.service_car_name);
		CarName.setText(ServiceServAdapter.GetCarName(servEntry));

		TextView mileage = (TextView) findViewById(R.id.service_Przebieg1);
		mileage.setText(Double.toString(servEntry.getMileage()));

		TextView cost = (TextView) findViewById(R.id.service_costt);
		cost.setText(Double.toString(servEntry.getCost()));
		
		TextView serviced = (TextView) findViewById(R.id.service_serwisowano);
		serviced.setText(servEntry.getServiced().replace(",", "\n"));

		String servArray[] = servEntry.getServiced().split(",");
		int CalcServicedHeight = 15 + (servArray.length * 22);

		if (servArray.length > 1) {
			TextView servicedTitle = (TextView) findViewById(R.id.service_serwisowano_title);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) servicedTitle.getLayoutParams();
			params.height = CalcServicedHeight;
			servicedTitle.setLayoutParams(params);
		}

		TextView date = (TextView) findViewById(R.id.service_date);
		date.setText(servEntry.getDate());

		TextView time = (TextView) findViewById(R.id.service_time);
		time.setText(servEntry.getTime());

		TextView place = (TextView) findViewById(R.id.service_place);
		place.setText(servEntry.getPlace());

		TextView notes = (TextView) findViewById(R.id.service_notes);
		notes.setText(servEntry.getNotes());
	}
	
	public void editButton (View v) {
		Intent i = new Intent(this, ServiceEdit.class);
		i.putExtra("positionServ", position);
		startActivity(i);
	}	
	
	public void backButton (View v) {
	   finish();
	}
}