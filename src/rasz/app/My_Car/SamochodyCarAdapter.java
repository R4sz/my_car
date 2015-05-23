package rasz.app.My_Car;

import java.util.List;

import rasz.app.My_Car.repository.CarsRepository;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SamochodyCarAdapter extends BaseAdapter {
	private Context context;
	private CarsRepository entry = new CarsRepository();
	private List<CarsRepository> listOfCars;

	public SamochodyCarAdapter(Context context, List<CarsRepository> listOfCars) {
		this.context = context;
		this.listOfCars = listOfCars;
	}

	public int getCount() {
		return listOfCars.size();
	}

	public Object getItem(int position) {
		return listOfCars.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		entry = listOfCars.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.samochody_lista_test, null);
		}

		TextView nazwaSam = (TextView) convertView.findViewById(R.id.lista_nazwa_samochodu);
		nazwaSam.setText(entry.getNazwa());

		TextView markaSam = (TextView) convertView.findViewById(R.id.lista_marka_samochodu);
		markaSam.setText(entry.getMarka());

		TextView modelSam = (TextView) convertView.findViewById(R.id.lista_model_samochodu);
		modelSam.setText(entry.getModel());

		TextView Przebieg = (TextView) convertView.findViewById(R.id.lista_przebieg_samochodu);
		Przebieg.setText(Long.toString(entry.getPrzebieg()));

		return convertView;
	}
}