package rasz.app.My_Car;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

import rasz.app.My_Car.repository.FillupsRepository;

public class TankowaniaList extends Activity {

	private FillupsRepository entry = new FillupsRepository();
	static DecimalFormat decim = new DecimalFormat("#.##");
	private double przejechane = DataContainer.getPrzejechane();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_tankowan_test);

		DataContainer.getDataFromDB(this, "SELECT t.* " + "FROM tankowania as t, samochody as s " + "WHERE t.idsam = s.idsam AND s.aktywny = 1 "
				+ "ORDER BY t.datawmms", "refueling");
		fillSummaryHeader();
		listViewStuff();

	}

	protected void onRestart() {
		super.onRestart();
		DataContainer.getDataFromDB(this, "SELECT t.* " + "FROM tankowania as t, samochody as s " + "WHERE t.idsam = s.idsam AND s.aktywny = 1 "
				+ "ORDER BY t.datawmms", "refueling");
		przejechane = DataContainer.getPrzejechane();
		fillSummaryHeader();
		listViewStuff();
	}

	public void listViewStuff() {
		ListView list = (ListView) findViewById(R.id.ListView01);
		list.setClickable(true);

		TankowaniaTankAdapter adapter = new TankowaniaTankAdapter(this, DataContainer.listOfRefuels);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
				entry = DataContainer.listOfRefuels.get(position);
				Intent td = new Intent(getApplicationContext(), TankowaniaDetails.class);
				td.putExtra("position", position);
				startActivity(td);
			}
		});

		list.setAdapter(adapter);
		registerForContextMenu(list);
	}

	public void fillSummaryHeader() {
		DataContainer.consumptionMap = DataContainer.consumptionCalc(DataContainer.listOfRefuels);

		TextView AverageFuelConsumption = (TextView) findViewById(R.id.testspalaniaa);
		if (!DataContainer.listOfRefuels.isEmpty()) {
			AverageFuelConsumption.setText(decim.format(DataContainer.consumptionMap.get("totalLiters") * 100 / przejechane));
		}
		TextView TotalFuelCost = (TextView) findViewById(R.id.CalkowiteKosztaPaliwa);
		TotalFuelCost.setText(decim.format(DataContainer.consumptionMap.get("totalTankCost")));

		TextView SummaryKm = (TextView) findViewById(R.id.Przejechane_Header);
		SummaryKm.setText(decim.format(przejechane));
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
		entry = DataContainer.listOfRefuels.get(idListForExtraMenu);

		DataContainer.database = new DatabaseDaneDB(this);
		try {
			SQLiteDatabase bd = DataContainer.database.getWritableDatabase();
			// menuItemIndex (id rekordu listy z dlugiego klika)
			// menuItemIndex = 1 delete;
			// menuItemIndex = 0 edit;
			// menuItemIndex = 2 add;
			if (menuItemIndex == 1) {
				bd.delete("tankowania", "idtankowania" + "=" + entry.getId(), null);
				DataContainer.listOfRefuels.remove(idListForExtraMenu);
				Long lastTankOdometer = entry.getMileAge();
				String actualCarOdometer = DataContainer.getDataFromDB(this,
						"SELECT przebieg FROM samochody WHERE idsam = " + entry.getIdsamochoduZtankowania(), "single");
				if ((lastTankOdometer) == Long.parseLong(actualCarOdometer)) {
					double przebiegUpdt;

					Double lastServOdometer = Double.parseDouble(DataContainer.getDataFromDB(this,
							"SELECT przebieg FROM naprawy WHERE idsam = " + entry.getIdsamochoduZtankowania() + " ORDER BY przebieg DESC", "single"));

					Double lastMaintOdometer = Double.parseDouble(DataContainer.getDataFromDB(this,
							"SELECT przebieg FROM maintance WHERE idsam = " + entry.getIdsamochoduZtankowania() + " ORDER BY przebieg DESC", "single"));

					if (lastServOdometer >= lastMaintOdometer) {
						przebiegUpdt = lastServOdometer;
					} else {
						przebiegUpdt = lastMaintOdometer;
					}
					ContentValues wartosci = new ContentValues();
					wartosci.put("przebieg", przebiegUpdt);
					bd.update("samochody", wartosci, "idsam" + "= " + entry.getIdsamochoduZtankowania(), null);
				}
				fillSummaryHeader();
				listViewStuff();
			} else if (menuItemIndex == 0) {
				Intent k = new Intent(this, TankowaniaEdit.class);
				k.putExtra("list_position", idListForExtraMenu);
				k.putExtra("idsam", entry.getIdsamochoduZtankowania());
				startActivity(k);
			} else if (menuItemIndex == 2) {
				Intent l = new Intent(this, TankowaniaDodaj.class);
				startActivity(l);
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