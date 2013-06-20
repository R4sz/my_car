package rasz.app.My_Car;

import static rasz.app.My_Car.DataBase_stale.*;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

	public class DatabaseDaneDB extends SQLiteOpenHelper {		
		
		private static final String NAZWA_BAZY_DANYCH = "baza_nowa1234.db";
		private static final int WERSJA_BAZY_DANYCH = 2;
		
		public DatabaseDaneDB(Context kontekst) {
			super(kontekst, NAZWA_BAZY_DANYCH, null, WERSJA_BAZY_DANYCH);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DB_SAMOCHODY_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ KEY_NAZWA + " TEXT NOT NULL, " +
					 KEY_MARKA + " " + MARKA_OPTIONS + ", " + 
		             KEY_MODEL + " " + MODEL_OPTIONS + ", " + 
		             KEY_ROK_PRODUKCJI + " " + ROK_PRODUKCJI_OPTIONS + ", " +
		             KEY_PRZEBIEG + " " + PRZEBIEG_OPTIONS + ", " +
		             KEY_POJEMNOSC_SILNIKA +  ", " + 
		             KEY_PALIWO + " " + PALIWO_OPTIONS + ", " +
		             KEY_SYMBOL_SILNIKA + ", " + 
		             KEY_VIN  + ", " + 
		             KEY_ACTIVE + " " + ACTIVE_OPTIONS + ", " + 
		             KEY_PRZEJECHANE + " " + PRZEJECHANE_OPTIONS + ", " + 
		             KEY_PUCHARSE_TIME + " " + PUCHARSE_TIME_OPTIONS + ", " + 
		             KEY_PRZEBIEG_START + " " + PRZEBIEG_START_OPTIONS + ", " + 
		             KEY_PUCHARSE_PRICE + " " + PUCHARSE_PRICE_OPTIONS + ");");  				
			
			db.execSQL("CREATE TABLE " + DB_TANKOWANIA_TABLE + "( " + KEY_ID_TANKOWANIA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				  + KEY_ID_SAMOCHODU + " " + ID_SAMOCHODU_OPTIONS + ", " + 
					KEY_PRZEBIEG + " " + PRZEBIEG_OPTIONS + ", " + 
					KEY_ILOSC_WLANYCH_LITROW + " " + ILOSC_WLANYCH_LITROW_OPTIONS + ", " + 
					KEY_CENA_ZA_LITR + " " + CENA_ZA_LITR_OPTIONS + ", " + 
					KEY_CENA_TANKOWANIA + " " + CENA_TANKOWANIA + ", " + 	
					KEY_DATA + " " + DATA_OPTIONS + ", " +
					KEY_TIME + " " + TIME_OPTIONS + ", " + 
					KEY_RODZAJ_PALIWA + " " + RODZAJ_PALIWA_OPTIONS + ", " + 
					KEY_STACJA + " " + STACJA_OPTIONS + ", " + 
					KEY_NOTATKI + " " + NOTATKI_OPTIONS + ", " + 
					KEY_PRZEJECHANE + " " + PRZEJECHANE_OPTIONS + ", " + 
					KEY_SPALANIE + " " + SPALANIE_OPTIONS  + ", " +
					KEY_DATEMILLIS + " " + DATEMILLIS_OPTIONS + ");");   
		   
			db.execSQL("CREATE TABLE " + DB_NAPRAWY_TABLE + "( " + KEY_ID_NAPRAWY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				  + KEY_ID_SAMOCHODU + " " + ID_SAMOCHODU_OPTIONS + ", " + 	
				    KEY_PRZEBIEG + " " + PRZEBIEG_OPTIONS + ", " + 
				    KEY_KOSZT_SERWISU + " " + KOSZT_SERWISU_OPTIONS + ", " + 
				    KEY_SERWISOWANO + " " + SERWISOWANO_OPTIONS + ", " + 
				    KEY_DATA + " " + DATA_OPTIONS + ", " +
					KEY_TIME + " " + TIME_OPTIONS + ", " + 
					KEY_MIEJSCE + " " + MIEJSCE_OPTIONS + ", " +
					KEY_NOTATKI + " " + NOTATKI_OPTIONS + ", " + 
					KEY_DATEMILLIS + " " + DATEMILLIS_OPTIONS + ");");			   
	
			db.execSQL("CREATE TABLE " + DB_MAINTANCE_TABLE + "( " + KEY_ID_MAINTANCE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				  + KEY_ID_SAMOCHODU + " " + ID_SAMOCHODU_OPTIONS + ", " + 
					KEY_PRZEBIEG + " " + PRZEBIEG_OPTIONS + ", " +  
					KEY_KOSZT_MAINTANCE + " " + KOSZT_MAINTANCE_OPTIONS + ", " +
					KEY_MAINTANCED + " " + MAINTANCED_OPTIONS + ", " +
					KEY_DATA + " " + DATA_OPTIONS + ", " +
					KEY_TIME + " " + TIME_OPTIONS + ", " + 
					KEY_NOTATKI + " " + NOTATKI_OPTIONS + ", " + 
					KEY_MIEJSCE + " " + MIEJSCE_OPTIONS + ", " +
					KEY_DATEMILLIS + " " + DATEMILLIS_OPTIONS + ");");   												
		}								

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			 db.execSQL("DROP TABLE IF EXISTS " + NAZWA_BAZY_DANYCH);
			onCreate(db);		
		}	
}