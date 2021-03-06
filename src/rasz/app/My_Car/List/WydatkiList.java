package rasz.app.My_Car.List;

import rasz.app.My_Car.DataBase_stale;
import rasz.app.My_Car.DataContainer;
import rasz.app.My_Car.DatabaseDaneDB;
import rasz.app.My_Car.R;
import rasz.app.My_Car.WydatkiDetails;
import rasz.app.My_Car.WydatkiDodaj;
import rasz.app.My_Car.WydatkiEdit;
import rasz.app.My_Car.WydatkiMaintAdapter;
import rasz.app.My_Car.R.array;
import rasz.app.My_Car.R.id;
import rasz.app.My_Car.R.layout;
import rasz.app.My_Car.repository.ExpensesRepository;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class WydatkiList extends Activity {

	private ExpensesRepository entry = new ExpensesRepository();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintance_list);

		DataContainer.getDataFromDB(this, "SELECT m.* " + "FROM maintance as m, samochody as s " + "WHERE m.idsam = s.idsam " + "AND s.aktywny = 1 "
				+ "ORDER BY m.datawmms", "maintance");
		listViewStuff();
	}

	protected void onRestart() {
		super.onRestart();
		DataContainer.getDataFromDB(this, "SELECT m.* " + "FROM maintance as m, samochody as s " + "WHERE m.idsam = s.idsam " + "AND s.aktywny = 1 "
				+ "ORDER BY m.datawmms", "maintance");
		listViewStuff();
	}

	private void listViewStuff() {
		ListView list = (ListView) findViewById(R.id.ListView011);
		list.setClickable(true);

		WydatkiMaintAdapter adapter = new WydatkiMaintAdapter(this, DataContainer.listOfMaints);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
				Intent details = new Intent(getApplicationContext(), WydatkiDetails.class);
				details.putExtra("pos", position);
				startActivity(details);
			}
		});

		list.setAdapter(adapter);
		registerForContextMenu(list);
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.ListView011) {
			menu.setHeaderTitle("Opcje...");
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	public boolean onContextItemSelected(MenuItem Item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) Item.getMenuInfo();
		int menuItemIndex = Item.getItemId();

		entry = DataContainer.listOfMaints.get(info.position);
		int positionMaint = info.position;

		DataContainer.database = new DatabaseDaneDB(this);
		try {
			SQLiteDatabase db = DataContainer.database.getReadableDatabase();
			// menuItemIndex (id rekordu listy z dlugiego klika)
			// menuItemIndex = 1 delete;
			// menuItemIndex = 0 edit;
			// menuItemIndex = 2 add;

			if (menuItemIndex == 1) {
				db.delete("maintance", "idmaintance = " + entry.getId(), null);
				DataContainer.listOfMaints.remove(positionMaint);

				String lastMaintOdometer = Double.toString(entry.getMileAge());
				Double actualCarOdometer = Double.parseDouble(DataContainer.getDataFromDB(this,
						"SELECT przebieg FROM samochody WHERE idsam = " + entry.getIdsam(), "single"));
				if (Double.parseDouble(lastMaintOdometer) == actualCarOdometer) {
					double przebiegUpdt;

					Double lastTankOdometer = Double.parseDouble(DataContainer.getDataFromDB(this,
							"SELECT przebieg FROM tankowania WHERE idsam = " + entry.getIdsam() + " ORDER BY przebieg DESC", "single"));

					Double lastServOdometer = Double.parseDouble(DataContainer.getDataFromDB(this,
							"SELECT przebieg FROM naprawy WHERE idsam = " + entry.getIdsam() + " ORDER BY przebieg DESC", "single"));

					if (lastTankOdometer >= lastServOdometer) {
						przebiegUpdt = lastTankOdometer;
					} else {
						przebiegUpdt = lastServOdometer;
					}
					ContentValues wartosci = new ContentValues();
					wartosci.put(DataBase_stale.KEY_PRZEBIEG, przebiegUpdt);
					db.update(DataBase_stale.DB_SAMOCHODY_TABLE, wartosci, "idsam" + "= " + entry.getIdsam(), null);
				}
				listViewStuff();
			} else if (menuItemIndex == 2) {
				Intent add = new Intent(this, WydatkiDodaj.class);
				startActivity(add);
			} else if (menuItemIndex == 0) {
				Intent edit = new Intent(getApplicationContext(), WydatkiEdit.class);
				edit.putExtra("positionMaint", positionMaint);
				startActivity(edit);
			}
		} finally {
			DataContainer.database.close();
		}

		return true;
	}

	public void backButton(View v) {
		finish();
	}
}