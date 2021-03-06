package rasz.app.My_Car;

import java.util.List;

import rasz.app.My_Car.repository.CarsRepository;
import rasz.app.My_Car.repository.ExpensesRepository;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WydatkiMaintAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	private static ExpensesRepository maintEntry = new ExpensesRepository();

	private List<ExpensesRepository> listOfMaints;

	public WydatkiMaintAdapter(Context context, List<ExpensesRepository> listOfMaints) {
		this.context = context;
		this.listOfMaints = listOfMaints;
	}

	public int getCount() {
		return listOfMaints.size();
	}

	public Object getItem(int position) {
		return listOfMaints.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		maintEntry = listOfMaints.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.maintance_list_content, null);
		}

		TextView mileage = (TextView) convertView.findViewById(R.id.maintance_list_mileage);
		mileage.setText(Double.toString(maintEntry.getMileAge()));

		TextView maintanced = (TextView) convertView.findViewById(R.id.maint_list_maintanced);
		maintanced.setText(maintEntry.getMaintanced());

		TextView CarName = (TextView) convertView.findViewById(R.id.maintance_list_car_name);
		CarName.setText(getCarName(maintEntry));

		TextView cost = (TextView) convertView.findViewById(R.id.maintance_list_cost);
		cost.setText(Double.toString(maintEntry.getCost()));

		TextView date = (TextView) convertView.findViewById(R.id.MaintDate);
		date.setText(maintEntry.getDate());

		return convertView;
	}

	public static String getCarName(ExpensesRepository entryy) {
		String j = null;
		for (CarsRepository t : DataContainer.listOfCars) {
			if (t.getId() == entryy.getIdsam()) {
				j = t.getCarName();
			}
		}
		return j;
	}

	public void onClick(View view) {
		ExpensesRepository maintEntry = (ExpensesRepository) view.getTag();
		listOfMaints.remove(maintEntry);
		notifyDataSetChanged();
	}
}