package rasz.app.My_Car;

//import android.provider.BaseColumns;

public interface DataBase_stale {

	// TABELA SAMOCHODY
	public static final String DB_SAMOCHODY_TABLE = "samochody";

	public static final String KEY_ID = "idsam";
	// public static final String ID_OPTIONS =
	// "INTEGER PRIMARY KEY AUTOINCREMENT";
	// public static final int ID_COLUMN = 0;

	public static final String KEY_NAZWA = "nazwa";
	public static final String NAZWA_OPTIONS = "TEXT NOT NULL";;
	public static final int NAZWA_COLUMN = 1;

	public static final String KEY_MARKA = "marka";
	public static final String MARKA_OPTIONS = "TEXT";;
	public static final int MARKA_COLUMN = 2;

	public static final String KEY_MODEL = "model";
	public static final String MODEL_OPTIONS = "TEXT";;
	public static final int MODEL_COLUMN = 3;

	public static final String KEY_ROK_PRODUKCJI = "rok_produkcji";
	public static final String ROK_PRODUKCJI_OPTIONS = "INTEGER DEFAULT 1900";;
	public static final int ROK_PRODUKCJI_COLUMN = 4;

	public static final String KEY_PRZEBIEG = "przebieg";
	public static final String PRZEBIEG_OPTIONS = "DOUBLE";
	public static final int PRZEBIEG_COLUMN = 5;

	public static final String KEY_POJEMNOSC_SILNIKA = "pojemnosc_silnika";
	public static final String POJEMNOSC_SILNIKA_OPTIONS = "DOUBLE";;
	public static final int POJEMNOSC_SILNIKA_COLUMN = 6;

	public static final String KEY_PALIWO = "paliwo";
	public static final String PALIWO_OPTIONS = "TEXT";;
	public static final int PALIWO_COLUMN = 7;

	public static final String KEY_SYMBOL_SILNIKA = "symbol_silnika";
	public static final int SYMBOL_SILNIKA_COLUMN = 8;

	public static final String KEY_VIN = "vin";
	public static final int VIN_COLUMN = 9;

	public static final String KEY_ACTIVE = "aktywny";
	public static final String ACTIVE_OPTIONS = "INTEGER DEFAULT 0";
	public static final int ACTIVE_COLUMN = 10;

	public static final String KEY_PUCHARSE_TIME = "czas_zakupu";
	public static final String PUCHARSE_TIME_OPTIONS = "LONG";
	public static final int PUCHARSE_TIME_COLUMN = 11;

	public static final String KEY_PRZEBIEG_START = "przebieg_start";
	public static final String PRZEBIEG_START_OPTIONS = "DOUBLE";
	public static final int PRZEBIEG_START_COLUMN = 12;

	public static final String KEY_PUCHARSE_PRICE = "cena_zakupu";
	public static final String PUCHARSE_PRICE_OPTIONS = "DOUBLE";
	public static final int PUCHARSE_PRICE_COLUMN = 13;

	// TABELA TANKOWANIA
	public static final String DB_TANKOWANIA_TABLE = "tankowania";

	public static final String KEY_ID_TANKOWANIA = "idtankowania";
	public static final String ID_TANKOWANIA_OPTIONS = "INTEGER";

	public static final String KEY_ID_SAMOCHODU = "idsam";
	public static final String ID_SAMOCHODU_OPTIONS = "INTEGER";

	// public static final String KEY_PRZEBIEG = "przebieg";
	// public static final String PRZEBIEG_OPTIONS = "LONG DEFAULT 0";;

	public static final String KEY_ILOSC_WLANYCH_LITROW = "ilosc_wlanych_litrow";
	public static final String ILOSC_WLANYCH_LITROW_OPTIONS = "DOUBLE";

	public static final String KEY_CENA_ZA_LITR = "cena_za_litr";
	public static final String CENA_ZA_LITR_OPTIONS = "DOUBLE";

	public static final String KEY_CENA_TANKOWANIA = "cena_tankowania";
	public static final String CENA_TANKOWANIA = "DOUBLE";

	public static final String KEY_DATA = "data";
	public static final String DATA_OPTIONS = "TEXT";

	public static final String KEY_DATEMILLIS = "datawmms";
	public static final String DATEMILLIS_OPTIONS = "LONG";

	public static final String KEY_TIME = "czas";
	public static final String TIME_OPTIONS = "TEXT";

	public static final String KEY_RODZAJ_PALIWA = "rodzaj_paliwa";
	public static final String RODZAJ_PALIWA_OPTIONS = "TEXT";

	public static final String KEY_STACJA = "stacja";
	public static final String STACJA_OPTIONS = "TEXT";

	public static final String KEY_NOTATKI = "notatki";
	public static final String NOTATKI_OPTIONS = "TEXT";

	public static final String KEY_PRZEJECHANE = "przejechane";
	public static final String PRZEJECHANE_OPTIONS = "DOUBLE";

	public static final String KEY_SPALANIE = "spalanie";
	public static final String SPALANIE_OPTIONS = "DOUBLE";

	// TABELA NAPRAWY
	public static final String DB_NAPRAWY_TABLE = "naprawy";

	public static final String KEY_ID_NAPRAWY = "idnaprawy";
	public static final String ID_NAPRAWY_OPTIONS = "INTEGER";

	// public static final String KEY_ID_SAMOCHODU = "idsam";
	// public static final String ID_SAMOCHODU_OPTIONS = "INTEGER";

	// public static final String KEY_PRZEBIEG = "przebieg";
	// public static final String PRZEBIEG_OPTIONS = "LONG DEFAULT 0";;

	public static final String KEY_KOSZT_SERWISU = "koszt";
	public static final String KOSZT_SERWISU_OPTIONS = "DOUBLE";

	public static final String KEY_SERWISOWANO = "serwisowano";
	public static final String SERWISOWANO_OPTIONS = "TEXT";

	// public static final String KEY_DATA = "data";
	// public static final String DATA_OPTIONS = "TEXT";

	// public static final String KEY_TIME = "czas";
	// public static final String TIME_OPTIONS = "TEXT";

	// public static final String KEY_DATEMILLIS = "datawmms";
	// public static final String DATEMILLIS_OPTIONS = "LONG";

	public static final String KEY_MIEJSCE = "miejsce";
	public static final String MIEJSCE_OPTIONS = "TEXT";

	// public static final String KEY_NOTATKI = "notatki";
	// public static final String NOTATKI_OPTIONS = "TEXT";

	// TABELE MAINTANCE
	public static final String DB_MAINTANCE_TABLE = "maintance";

	public static final String KEY_ID_MAINTANCE = "idmaintance";
	public static final String ID_MAINTANCE_OPTIONS = "INTEGER";

	// public static final String KEY_ID_SAMOCHODU = "idsam";
	// public static final String ID_SAMOCHODU_OPTIONS = "INTEGER";

	// public static final String KEY_PRZEBIEG = "przebieg";
	// public static final String PRZEBIEG_OPTIONS = "LONG DEFAULT 0";;

	public static final String KEY_KOSZT_MAINTANCE = "koszt";
	public static final String KOSZT_MAINTANCE_OPTIONS = "DOUBLE";

	public static final String KEY_MAINTANCED = "maintanced";
	public static final String MAINTANCED_OPTIONS = "TEXT";

	// public static final String KEY_DATA = "data";
	// public static final String DATA_OPTIONS = "TEXT";

	// public static final String KEY_TIME = "czas";
	// public static final String TIME_OPTIONS = "TEXT";

	// public static final String KEY_DATEMILLIS = "datawmms";
	// public static final String DATEMILLIS_OPTIONS = "LONG";

	// public static final String KEY_MIEJSCE = "miejsce";
	// public static final String MIEJSCE_OPTIONS = "TEXT";

	// public static final String KEY_NOTATKI = "notatki";
	// public static final String NOTATKI_OPTIONS = "TEXT";

}