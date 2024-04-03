package uqac.dim.travelmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "travel_manager.db";
    private static final int DATABASE_VERSION = 1;

    // Define table and column names
    private static final String TABLE_VOYAGE = "voyage";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_DATE_DEPART = "date_depart";
    private static final String COLUMN_DATE_FIN = "date_fin";

    // SQL query to create voyage table
    private static final String SQL_CREATE_TABLE_VOYAGE = "CREATE TABLE " + TABLE_VOYAGE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOM + " TEXT, " +
            COLUMN_DATE_DEPART + " TEXT, " +
            COLUMN_DATE_FIN + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_VOYAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOYAGE);
        onCreate(db);
    }

    // Insert new voyage
    public long insertVoyage(String nom, String dateDepart, String dateFin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, nom);
        values.put(COLUMN_DATE_DEPART, dateDepart);
        values.put(COLUMN_DATE_FIN, dateFin);
        return db.insert(TABLE_VOYAGE, null, values);
    }

    // Get all voyages
    public Cursor getAllVoyages() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_VOYAGE, null, null, null, null, null, null);
    }

    // Other CRUD operations (update, delete) can be added similarly
}


