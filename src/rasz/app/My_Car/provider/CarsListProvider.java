package rasz.app.My_Car.provider;

public class CarsListProvider {

	private static CarsListProvider instance = null;

	private CarsListProvider() {
	
	}

	public CarsListProvider getInstance() {
		if (instance == null) {
			instance = new CarsListProvider();
		}
		return instance;
	}
	
	
	
}
