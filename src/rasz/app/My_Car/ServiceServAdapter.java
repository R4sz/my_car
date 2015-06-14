package rasz.app.My_Car;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rasz.app.My_Car.repository.CarsRepository;
import rasz.app.My_Car.repository.ServicesRepository;

public class ServiceServAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	private static ServicesRepository servEntry = new ServicesRepository();

	private List<ServicesRepository> listOfServs;

	public ServiceServAdapter(Context context, List<ServicesRepository> listOfServs) {
		this.context = context;
		this.listOfServs = listOfServs;
	}

	public int getCount() {
		return listOfServs.size();
	}

	public Object getItem(int position) {
		return listOfServs.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		servEntry = listOfServs.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.service_list_content, null);
		}

		TextView mileage = (TextView) convertView.findViewById(R.id.service_list_mileage);
		mileage.setText(Double.toString(servEntry.getMileage()));

		TextView CarName = (TextView) convertView.findViewById(R.id.serv_list_car_name);
		CarName.setText(GetCarName(servEntry));

		TextView cost = (TextView) convertView.findViewById(R.id.service_list_cost);
		cost.setText(Double.toString(servEntry.getCost()));

		TextView date = (TextView) convertView.findViewById(R.id.ServDate);
		date.setText(servEntry.getDate());

		return convertView;
	}

	public static String GetCarName(ServicesRepository entryy) {
		String j = null;
		for (CarsRepository t : DataContainer.listOfCars) {
			if (t.getIdsam() == entryy.getIdsam()) {
				j = t.getCarName();
				return j;
			}
		}
		return j;
	}

	public void onClick(View view) {
		ServiceSimpleServInfo servEntry = (ServiceSimpleServInfo) view.getTag();
		listOfServs.remove(servEntry);
		notifyDataSetChanged();
	}
}