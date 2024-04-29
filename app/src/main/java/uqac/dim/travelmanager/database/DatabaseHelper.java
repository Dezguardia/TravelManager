package uqac.dim.travelmanager.database;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uqac.dim.travelmanager.models.Jour;
import uqac.dim.travelmanager.models.Lieu;
import uqac.dim.travelmanager.models.Voyage;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Define table and column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_DATE_DEPART = "date_depart";
    public static final String COLUMN_DATE_FIN = "date_fin";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    // Constantes pour la table Jour
    public static final String TABLE_JOUR = "jour";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ID_VOYAGE = "voyage_id";

    // Constantes pour la table Lieu
    public static final String TABLE_LIEU = "lieu";
    public static final String COLUMN_NOM_LIEU = "lieu";
    public static final String COLUMN_TRANSPORT = "transport";
    public static final String COLUMN_ID_JOUR = "jour_id";



    private static final String DATABASE_NAME = "travel_manager.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_VOYAGE = "voyage";
    // Compteur pour générer des IDs uniques
    private static long idCounter = 0;
    private static final String PREFS_NAME = "VoyagePrefs";
    private static final String PREF_LAST_VOYAGE_ID = "lastVoyageId";
    private Context context;

    // Méthode pour générer un ID unique pour un nouveau voyage
    public long generateUniqueVoyageId() {
        // Récupérer le dernier ID utilisé depuis les préférences partagées
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastVoyageId = prefs.getLong(PREF_LAST_VOYAGE_ID, 0);

        // Incrémenter le dernier ID utilisé
        long newVoyageId = lastVoyageId + 1;

        // Vérifier si un voyage avec cet ID existe déjà
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(newVoyageId)};
        Cursor cursor = db.query(TABLE_VOYAGE, projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            // Un voyage avec cet ID existe déjà, donc incrémenter l'ID jusqu'à ce qu'un ID unique soit trouvé
            while (cursor.moveToNext()) {
                newVoyageId++;
            }
            cursor.close();
        }

        // Mettre à jour le dernier ID utilisé dans les préférences partagées
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREF_LAST_VOYAGE_ID, newVoyageId);
        editor.apply();

        return newVoyageId;
    }
    // SQL query to create voyage table
    private static final String SQL_CREATE_TABLE_VOYAGE = "CREATE TABLE " + TABLE_VOYAGE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOM + " TEXT, " +
            COLUMN_DATE_DEPART + " TEXT, " +
            COLUMN_DATE_FIN + " TEXT, " +
            COLUMN_IMAGE_PATH + " TEXT)";

    // SQL query to create jour table
    private static final String SQL_CREATE_TABLE_JOUR = "CREATE TABLE " + TABLE_JOUR + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_ID_VOYAGE + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_ID_VOYAGE + ") REFERENCES " + TABLE_VOYAGE + "(" + COLUMN_ID + "))";

    // SQL query to create lieu table
    private static final String SQL_CREATE_TABLE_LIEU = "CREATE TABLE " + TABLE_LIEU + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOM_LIEU + " TEXT, " +
            COLUMN_TRANSPORT + " TEXT, " +
            COLUMN_ID_JOUR + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_ID_JOUR + ") REFERENCES " + TABLE_JOUR + "(" + COLUMN_ID + "))";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate method is called");
        db.execSQL(SQL_CREATE_TABLE_VOYAGE);
        // Vérifier si la table Jour existe avant de la créer
        if (!tableExists(db, TABLE_JOUR)) {
            db.execSQL(SQL_CREATE_TABLE_JOUR);
        }
        // Vérifier si la table Lieu existe avant de la créer
        if (!tableExists(db, TABLE_LIEU)) {
            db.execSQL(SQL_CREATE_TABLE_LIEU);
        }
    }

    // Méthode pour vérifier si une table existe dans la base de données
    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOYAGE);
        // Create tables again
        onCreate(db);
    }

    // Insert new voyage
    public long insertVoyage(String nom, String dateDepart, String dateFin, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, nom);
        values.put(COLUMN_DATE_DEPART, dateDepart);
        values.put(COLUMN_DATE_FIN, dateFin);
        values.put(COLUMN_IMAGE_PATH,imagePath);
        return db.insert(TABLE_VOYAGE, null, values);
    }

    public long insertVoyageComplet(Voyage voyage) {
        SQLiteDatabase db = this.getWritableDatabase();
        long voyageId = -1;

        // Vérifier si l'objet Voyage possède déjà un ID
        if (voyageId == -1) {
            // Générer un nouvel ID unique pour le voyage
            voyageId = generateUniqueVoyageId();
            // Méthode pour générer un nouvel ID unique
            // Assigner cet ID unique à l'objet Voyage
            voyage.setId(voyageId);
        }
        // Insérer le voyage principal
        ContentValues voyageValues = new ContentValues();
        voyageValues.put(COLUMN_NOM, voyage.getNomVoyage());
        voyageValues.put(COLUMN_DATE_DEPART, voyage.getDateDepart());
        voyageValues.put(COLUMN_DATE_FIN, voyage.getDateFin());
        voyageValues.put(COLUMN_IMAGE_PATH, voyage.getImagePath());
        voyageId = db.insert(TABLE_VOYAGE, null, voyageValues);



        // Vérifier si l'insertion du voyage principal a réussi
        if (voyageId != -1) {
            // Insérer les jours et les lieux associés
            ArrayList<Jour> jours = voyage.getJours();
            for (Jour jour : jours) {
                // Insérer le jour
                ContentValues jourValues = new ContentValues();
                jourValues.put("voyage_id", voyageId);
                jourValues.put("date", jour.getDate());
                // Insérer le jour dans la table jour
                long jourId = db.insert("jour", null, jourValues);

                // Vérifier si l'insertion du jour a réussi
                if (jourId != -1) {
                    // Insérer les lieux associés au jour
                    ArrayList<Lieu> lieux = jour.getLieux();
                    for (Lieu lieu : lieux) {
                        // Insérer le lieu
                        ContentValues lieuValues = new ContentValues();
                        lieuValues.put("jour_id", jourId);
                        lieuValues.put("lieu", lieu.getLieu());
                        lieuValues.put("transport", lieu.getTransport());
                        // Insérer le lieu dans la table lieu
                        db.insert("lieu", null, lieuValues);
                    }
                } else {
                    // Gérer l'échec de l'insertion du jour
                    Log.e(TAG, "Échec de l'insertion du jour: " + jour.getDate());
                }
            }
        } else {
            // Gérer l'échec de l'insertion du voyage principal
            Log.e(TAG, "Échec de l'insertion du voyage principal: " + voyage.getNomVoyage());
        }

        // Fermer la base de données
        db.close();

        return voyageId;
    }

    public Voyage getExistingVoyage(String nomVoyage, String dateDepart, String dateFin) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Clause WHERE pour rechercher le voyage avec le même nom, la même date de départ et la même date de fin
        String selection = COLUMN_NOM + " = ? AND " + COLUMN_DATE_DEPART + " = ? AND " + COLUMN_DATE_FIN + " = ?";
        String[] selectionArgs = {nomVoyage, dateDepart, dateFin};

        // Effectuer la requête pour récupérer le voyage existant
        Cursor cursor = db.query(TABLE_VOYAGE, null, selection, selectionArgs, null, null, null);

        Voyage existingVoyage = null;
        if (cursor != null && cursor.moveToFirst()) {
            // Un voyage existant avec les mêmes informations a été trouvé
            // Récupérer les données du voyage existant à partir du curseur
            @SuppressLint("Range") String existingNomVoyage = cursor.getString(cursor.getColumnIndex(COLUMN_NOM));
            @SuppressLint("Range") String existingDateDepart = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_DEPART));
            @SuppressLint("Range") String existingDateFin = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_FIN));
            @SuppressLint("Range") String existingImagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));

            // Créer un objet Voyage avec les données récupérées
            existingVoyage = new Voyage(existingNomVoyage, existingDateDepart, existingDateFin, existingImagePath);

            // Fermer le curseur
            cursor.close();
        }

        return existingVoyage;
    }
    // Exemple de méthode pour récupérer l'ID d'un voyage à partir de son nom et de ses dates
    @SuppressLint("Range")
    public long getVoyageId(String nomVoyage, String dateDepart, String dateFin) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Clause WHERE pour rechercher le voyage avec le même nom, la même date de départ et la même date de fin
        String selection = COLUMN_NOM + " = ? AND " + COLUMN_DATE_DEPART + " = ? AND " + COLUMN_DATE_FIN + " = ?";
        String[] selectionArgs = { nomVoyage, dateDepart, dateFin };

        // Effectuer la requête pour récupérer l'ID du voyage
        Cursor cursor = db.query(TABLE_VOYAGE, new String[] { COLUMN_ID }, selection, selectionArgs, null, null, null);

        long voyageId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            // Récupérer l'ID du voyage à partir du curseur
            voyageId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));

            // Fermer le curseur
            cursor.close();
        }

        return voyageId;
    }

    public int updateVoyage(Voyage voyage) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Définir les valeurs à mettre à jour
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, voyage.getNomVoyage());
        values.put(COLUMN_DATE_DEPART, voyage.getDateDepart());
        values.put(COLUMN_DATE_FIN, voyage.getDateFin());
        values.put(COLUMN_IMAGE_PATH, voyage.getImagePath());

        // Clause WHERE pour mettre à jour le voyage avec le même nom, la même date de départ et la même date de fin
        String selection = COLUMN_NOM + " = ? AND " + COLUMN_DATE_DEPART + " = ? AND " + COLUMN_DATE_FIN + " = ?";
        String[] selectionArgs = {voyage.getNomVoyage(), voyage.getDateDepart(), voyage.getDateFin()};

        // Mettre à jour le voyage dans la base de données
        int rowsUpdated = db.update(TABLE_VOYAGE, values, selection, selectionArgs);
        Log.d("Voyage update complet", "Le voyage : " + voyage.toString());
        long id = getVoyageId(voyage.getNomVoyage(), voyage.getDateDepart(), voyage.getDateFin());
        ajouterListeJours(id,voyage.getJours());
        return rowsUpdated;
    }
    public int updateVoyageById(long voyageId, Voyage voyage) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsUpdated = 0;

        // Commencer une transaction
        db.beginTransaction();

        try {
            Log.d("updateVoyageWithJoursEtLieux", "Début de la transaction");
            // Mettre à jour le voyage principal
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOM, voyage.getNomVoyage());
            values.put(COLUMN_DATE_DEPART, voyage.getDateDepart());
            values.put(COLUMN_DATE_FIN, voyage.getDateFin());
            values.put(COLUMN_IMAGE_PATH, voyage.getImagePath());
            Log.d("updateVoyageWithJoursEtLieux", "Voyage à mettre à jour : " + values.toString());

            // Clause WHERE pour mettre à jour le voyage avec l'ID spécifié
            String selection = COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(voyageId)};

            // Mettre à jour le voyage principal
            rowsUpdated = db.update(TABLE_VOYAGE, values, selection, selectionArgs);

            // Si le voyage principal est mis à jour avec succès
            if (rowsUpdated > 0) {
                Log.d("updateVoyageWithJoursEtLieux", "Voyage principal mis à jour avec succès");

                // Supprimer tous les jours associés à ce voyage
                Log.d("updateVoyageWithJoursEtLieux", "Suppression des jours associés au voyage");
                db.delete(TABLE_JOUR, COLUMN_ID_VOYAGE + " = ?", new String[]{String.valueOf(voyageId)});

                // Insérer les nouveaux jours avec les lieux associés
                for (Jour jour : voyage.getJours()) {
                    // Insérer le jour dans la table Jour
                    ContentValues jourValues = new ContentValues();
                    jourValues.put(COLUMN_DATE, jour.getDate());
                    jourValues.put(COLUMN_ID_VOYAGE, voyageId);
                    long jourId = db.insert(TABLE_JOUR, null, jourValues);

                    // Si l'insertion du jour est réussie
                    if (jourId != -1) {
                        Log.d("updateVoyageWithJoursEtLieux", "Jour inséré avec succès : " + jour.toString());

                        // Insérer les lieux associés à ce jour
                        for (Lieu lieu : jour.getLieux()) {
                            ContentValues lieuValues = new ContentValues();
                            lieuValues.put(COLUMN_NOM_LIEU, lieu.getLieu());
                            lieuValues.put(COLUMN_TRANSPORT, lieu.getTransport());
                            lieuValues.put(COLUMN_ID_JOUR, jourId);
                            db.insert(TABLE_LIEU, null, lieuValues);
                            Log.d("updateVoyageWithJoursEtLieux", "Lieu inséré avec succès : " + lieu.toString());

                        }
                    }
                }

                // Valider la transaction
                db.setTransactionSuccessful();
            }
        } finally {
            // Terminer la transaction
            db.endTransaction();
            Log.d("updateVoyageWithJoursEtLieux", "Fin de la transaction");
        }

        return rowsUpdated;
    }

    public int updateVoyageWithJoursEtLieux(Voyage voyage) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsUpdated = 0;

        // Commencer une transaction
        db.beginTransaction();

        try {
            Log.d("updateVoyageWithJoursEtLieux", "Début de la transaction");
            // Mettre à jour le voyage principal
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOM, voyage.getNomVoyage());
            values.put(COLUMN_DATE_DEPART, voyage.getDateDepart());
            values.put(COLUMN_DATE_FIN, voyage.getDateFin());
            values.put(COLUMN_IMAGE_PATH, voyage.getImagePath());
            Log.d("updateVoyageWithJoursEtLieux", "Voyage à mettre à jour : " + values.toString());

            // Clause WHERE pour mettre à jour le voyage avec le même nom, la même date de départ et la même date de fin
            String selection = COLUMN_NOM + " = ? AND " + COLUMN_DATE_DEPART + " = ? AND " + COLUMN_DATE_FIN + " = ?";
            String[] selectionArgs = {voyage.getNomVoyage(), voyage.getDateDepart(), voyage.getDateFin()};

            // Mettre à jour le voyage principal
            rowsUpdated = db.update(TABLE_VOYAGE, values, selection, selectionArgs);

            // Si le voyage principal est mis à jour avec succès
            if (rowsUpdated > 0) {
                Log.d("updateVoyageWithJoursEtLieux", "Voyage principal mis à jour avec succès");

                // Supprimer tous les jours associés à ce voyage
                Log.d("updateVoyageWithJoursEtLieux", "Suppression des jours associés au voyage");
                db.delete(TABLE_JOUR, COLUMN_ID_VOYAGE + " = ?", new String[]{String.valueOf(voyage.getId())});

                // Insérer les nouveaux jours avec les lieux associés
                for (Jour jour : voyage.getJours()) {
                    // Insérer le jour dans la table Jour
                    ContentValues jourValues = new ContentValues();
                    jourValues.put(COLUMN_DATE, jour.getDate());
                    jourValues.put(COLUMN_ID_VOYAGE, voyage.getId());
                    long jourId = db.insert(TABLE_JOUR, null, jourValues);

                    // Si l'insertion du jour est réussie
                    if (jourId != -1) {
                        Log.d("updateVoyageWithJoursEtLieux", "Jour inséré avec succès : " + jour.toString());

                        // Insérer les lieux associés à ce jour
                        for (Lieu lieu : jour.getLieux()) {
                            ContentValues lieuValues = new ContentValues();
                            lieuValues.put(COLUMN_NOM_LIEU, lieu.getLieu());
                            lieuValues.put(COLUMN_TRANSPORT, lieu.getTransport());
                            lieuValues.put(COLUMN_ID_JOUR, jourId);
                            db.insert(TABLE_LIEU, null, lieuValues);
                            Log.d("updateVoyageWithJoursEtLieux", "Lieu inséré avec succès : " + lieu.toString());

                        }
                    }
                }

                // Valider la transaction
                db.setTransactionSuccessful();
            }
        } finally {
            // Terminer la transaction
            db.endTransaction();
            Log.d("updateVoyageWithJoursEtLieux", "Fin de la transaction");
        }

        return rowsUpdated;

    }


    // Get all voyages
    public Cursor getAllVoyages() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_VOYAGE, null, null, null, null, null, null);
    }

    // Other CRUD operations (update, delete) can be added similarly

    // Méthode pour récupérer les jours associés à un voyage
    public List<Jour> getJoursForVoyage(long voyageId) {
        List<Jour> jours = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Clause WHERE pour récupérer les jours du voyage spécifique
        String selection = COLUMN_ID_VOYAGE + " = ?";
        String[] selectionArgs = {String.valueOf(voyageId)};

        // Effectuer la requête pour récupérer les jours
        Cursor cursor = db.query(TABLE_JOUR, null, selection, selectionArgs, null, null, null);

        // Parcourir le curseur pour récupérer les jours
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Récupérer les données du jour depuis le curseur
                @SuppressLint("Range") int jourId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                // Créer un objet Jour avec les données récupérées
                Jour jour = new Jour(date,jourId);

                // Ajouter le jour à la liste de jours
                jours.add(jour);
            } while (cursor.moveToNext());

            // Fermer le curseur
            cursor.close();
        }

        return jours;
    }

    public Voyage getVoyageById(long voyageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Voyage voyage = null;

        // Clause WHERE pour rechercher le voyage avec l'ID spécifié
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(voyageId) };

        // Effectuer la requête pour récupérer le voyage
        Cursor cursor = db.query(TABLE_VOYAGE, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Récupérer les données du voyage à partir du curseur
            @SuppressLint("Range") String nomVoyage = cursor.getString(cursor.getColumnIndex(COLUMN_NOM));
            @SuppressLint("Range") String dateDepart = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_DEPART));
            @SuppressLint("Range") String dateFin = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_FIN));
            @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));

            // Créer un objet Voyage avec les données récupérées
            voyage = new Voyage(nomVoyage, dateDepart, dateFin, imagePath);

            // Fermer le curseur
            cursor.close();
        }

        return voyage;
    }

    public boolean supprimerVoyage(long voyageId) {
        // Récupérer la référence à la base de données en mode écriture
        SQLiteDatabase db = this.getWritableDatabase();

        // Supprimer le voyage de la base de données en utilisant son ID
        int rowsAffected = db.delete(TABLE_VOYAGE, COLUMN_ID + "=?", new String[] { String.valueOf(voyageId) });

        // Fermer la connexion à la base de données
        db.close();

        // Vérifier si la suppression a réussi en vérifiant le nombre de lignes affectées
        return rowsAffected > 0;
    }
    public void insertJour(long voyageId,Jour jour) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Commencer une transaction
        db.beginTransaction();

        try {
            // Créer un ContentValues pour insérer les données du jour
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, jour.getDate());
            values.put(COLUMN_ID_VOYAGE, jour.getNumeroJour());
            values.put(COLUMN_ID_VOYAGE, voyageId);

            // Insérer le jour dans la table Jour
            db.insert(TABLE_JOUR, null, values);


            // Valider la transaction
            db.setTransactionSuccessful();
        } finally {
            // Terminer la transaction
            db.endTransaction();
        }
    }
    public void ajouterListeJours(long voyageId, List<Jour> jours) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Commencer une transaction
        db.beginTransaction();

        try {
            // Parcourir la liste de jours
            for (Jour jour : jours) {
                // Créer un ContentValues pour insérer les données du jour
                ContentValues values = new ContentValues();
                values.put(COLUMN_DATE, jour.getDate());
                values.put(COLUMN_ID_VOYAGE, jour.getNumeroJour());
                values.put(COLUMN_ID_VOYAGE, voyageId);

                // Insérer le jour dans la table Jour
                db.insert(TABLE_JOUR, null, values);
            }

            // Valider la transaction
            db.setTransactionSuccessful();
        } finally {
            // Terminer la transaction
            db.endTransaction();
        }
    }

}


