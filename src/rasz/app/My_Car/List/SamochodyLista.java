package rasz.app.My_Car.List;

import rasz.app.My_Car.DataContainer;
import rasz.app.My_Car.DatabaseDaneDB;
import rasz.app.My_Car.R;
import rasz.app.My_Car.SamochodyCarAdapter;
import rasz.app.My_Car.SamochodyDetails;
import rasz.app.My_Car.SamochodyDodaj;
import rasz.app.My_Car.SamochodyEdit;
import rasz.app.My_Car.SamochodySimpleCarInfo;
import rasz.app.My_Car.R.array;
import rasz.app.My_Car.R.id;
import rasz.app.My_Car.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SamochodyLista extends Activity {

	public static SamochodySimpleCarInfo entry = new SamochodySimpleCarInfo();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.samochody_lista);
		initListView();
	}

	protected void onRestart() {
		super.onRestart();
		DataContainer.getDataFromDB(this, "SELECT * FROM Samochody", "cars");
		initListView();	
	}
	
	private void initListView() {
		
		TextView active = (TextView) findViewById(R.id.active_cars);
		String aktywne = "Aktywne Samochody:";
		for (int i = 0; i < DataContainer.listOfCars.size(); i++) {

			entry = DataContainer.listOfCars.get(i);
			if (entry.getAktywny() == 1) {
				aktywne += " " + entry.getNazwa();
			}
		} 

		if (!aktywne.equals("Aktywne Samochody:")) {		
			active.setText(aktywne);
		} else {
			active.setText("Brak aktywnych samochodów");
		}

		ListView list = (ListView) findViewById(R.id.ListView01);
		list.setClickable(true);

		SamochodyCarAdapter adapter = new SamochodyCarAdapter(this, DataContainer.listOfCars);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
				Intent td = new Intent(getApplicationContext(), SamochodyDetails.class);
				td.putExtra("position", position);
				startActivity(td);
			}
		});
		list.setAdapter(adapter);
		registerForContextMenu(list);
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.ListView01) {
			menu.setHeaderTitle("Opcje...");
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();

		int idListForExtraMenu = info.position;
		entry = DataContainer.listOfCars.get(idListForExtraMenu);
		int idCar = entry.getIdsam();

		DataContainer.database = new DatabaseDaneDB(this);
		SQLiteDatabase bd = DataContainer.database.getReadableDatabase();
		// menuItemIndex (id rekordu listy z dlugiego klika)
		// menuItemIndex = 2 add;
		// menuItemIndex = 1 delete;
		// menuItemIndex = 0 edit;		
		if (menuItemIndex == 1) {
			bd.delete("samochody", "idsam" + "=" + idCar, null);
			DataContainer.database.close();
			DataContainer.listOfCars.remove(idListForExtraMenu);
			initListView();
		} else if (menuItemIndex == 0) {
			Intent k = new Intent(this, SamochodyEdit.class);
			k.putExtra("position", idListForExtraMenu);
			startActivity(k);
		} else if (menuItemIndex == 2) {
			Intent l = new Intent(this, SamochodyDodaj.class);
			startActivity(l);
		}
		return true;
	}

	public void backButton(View v) {
		finish();
	}
}