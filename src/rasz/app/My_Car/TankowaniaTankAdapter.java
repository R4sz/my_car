package rasz.app.My_Car;

import java.util.List;

import rasz.app.My_Car.repository.CarsRepository;
import rasz.app.My_Car.repository.FillupsRepository;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TankowaniaTankAdapter extends BaseAdapter implements OnClickListener {
	private Context context;

	private List<FillupsRepository> listOfRefueling;

	public TankowaniaTankAdapter(Context context, List<FillupsRepository> listOfRefueling) {
		this.context = context;
		this.listOfRefueling = listOfRefueling;
	}

	public int getCount() {
		return listOfRefueling.size();
	}

	public Object getItem(int position) {
		return listOfRefueling.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		FillupsRepository entry = listOfRefueling.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.lista_tankowan_test_content, null);
		}

		TextView Name = (TextView) convertView.findViewById(R.id.tank_list_car_name);
		Name.setText(getCarName(entry));

		TextView tvContact = (TextView) convertView.findViewById(R.id.tank_list_litry);
		tvContact.setText(Double.toString(entry.getLitry()));

		TextView tvPhone = (TextView) convertView.findViewById(R.id.tvMobile);
		tvPhone.setText(Double.toString(entry.getWartosc()));

		TextView tvMail = (TextView) convertView.findViewById(R.id.tvMail);
		tvMail.setText(Double.toString(entry.getPrzebieg()));

		TextView Date = (TextView) convertView.findViewById(R.id.TankDate);
		Date.setText(entry.getDate());

		return convertView;
	}

	public String getCarName(FillupsRepository entryy) {
		String j = null;
		for (CarsRepository t : DataContainer.listOfCars) {
			if (t.getIdsam() == entryy.getIdsamochoduZtankowania()) {
				j = t.getNazwa();
			}
		}
		return j;
	}

	public void onClick(View view) {
		FillupsRepository entry = (FillupsRepository) view.getTag();
		listOfRefueling.remove(entry);
		notifyDataSetChanged();
	}
}