package rasz.app.My_Car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {

	protected static SparseArray<Class<? extends Activity>> buttonConfig = new SparseArray<Class<? extends Activity>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		buttonConfig.append(R.id.przycisk_samochody, SamochodyLista.class);
		buttonConfig.append(R.id.przycisk_tankowanie, TankowaniaDodaj.class);
		buttonConfig.append(R.id.przycisk_naprawy_koszta, ServiceDodaj.class);
		buttonConfig.append(R.id.przycisk_lista_tankowan, TankowaniaList.class);
		buttonConfig.append(R.id.przycisk_wydatki, WydatkiDodaj.class);
		buttonConfig.append(R.id.przycisk_serwisy_lista, ServiceList.class);
		buttonConfig.append(R.id.przycisk_maintance_list, WydatkiList.class);
		buttonConfig.append(R.id.przycisk_statystyki, Statystyki.class);
		
		initButtons();
		DataContainer.getDataFromDB(this, "SELECT * FROM Samochody", "cars");
	}

	protected void onRestart() {
		super.onRestart();
		DataContainer.getDataFromDB(this, "SELECT * FROM Samochody", "cars");
	}
	
	public void nullCarTable() {
		Intent samochodyDodaj = new Intent(this, SamochodyDodaj.class);
		startActivity(samochodyDodaj);
		Toast.makeText(getApplicationContext(), "Najpierw Dodaj Samochód...", Toast.LENGTH_LONG).show();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.przycisk_wyjscie) {
			moveTaskToBack(true);
			return;
		}
		if (DataContainer.listOfCars.isEmpty()) {
			nullCarTable();
			return;
		}
		Intent i = new Intent(this, buttonConfig.get(v.getId()));
		startActivity(i);
	}

	void initButtons() {
		int key = 0;
		View view = null;
		for (int i = 0; i < buttonConfig.size(); i++) {
			key = buttonConfig.keyAt(i);
			view = findViewById(key);
			view.setOnClickListener(this);
		}

		view = findViewById(R.id.przycisk_wyjscie);
		view.setOnClickListener((OnClickListener) this);
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem element) {
		switch (element.getItemId()) {
		case R.id.add_car_menu:
			Intent m = new Intent(this, SamochodyDodaj.class);
			startActivity(m);
			return true;
		}
		return false;
	}

	public void carAddButton(View v) {
		Intent m = new Intent(this, SamochodyDodaj.class);
		startActivity(m);		
	}
	//Committt
	public void backButton(View v) {
		finish();
	}
}