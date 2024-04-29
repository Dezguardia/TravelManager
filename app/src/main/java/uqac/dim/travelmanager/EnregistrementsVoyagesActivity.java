package uqac.dim.travelmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import uqac.dim.travelmanager.database.DatabaseHelper;
import uqac.dim.travelmanager.models.Voyage;
import uqac.dim.travelmanager.models.VoyageAdapter;

public class EnregistrementsVoyagesActivity extends AppCompatActivity {

    private ListView listView;
    private RecyclerView mRecyclerView;
    private ArrayAdapter<String> adapter;
    private List<Voyage> voyagesList;
    private VoyageAdapter voyageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enregistrements_voyages);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //listView = findViewById(R.id.list_view_voyages);
        mRecyclerView = findViewById(R.id.recycler_view_voyages);
        voyagesList = new ArrayList<>();

        // Créer et configurer l'adaptateur
        voyageAdapter = new VoyageAdapter(this, voyagesList);
        mRecyclerView.setAdapter(voyageAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //listView.setAdapter(voyageAdapter);

        // Charger les voyages depuis la base de données
        chargerVoyagesDepuisDB();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                loadMapFragment();
                return true;
            } else if (itemId == R.id.navigation_travel) {
                Intent intentTravel = new Intent(EnregistrementsVoyagesActivity.this, TravelActivity.class);
                startActivity(intentTravel);
                return true;
            } else if (itemId == R.id.navigation_add) {
                Intent intent = new Intent(EnregistrementsVoyagesActivity.this, CreerVoyageActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                Intent intent = new Intent(EnregistrementsVoyagesActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_options) {
                Intent intentOptions = new Intent(EnregistrementsVoyagesActivity.this, OptionsActivity.class);
                startActivity(intentOptions);
                return true;
            }

            // Si aucun des éléments ne correspond, renvoyez false
            return false;
        });

    }

    private void chargerVoyagesDepuisDB() {
        // Instancier votre DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Récupérer tous les voyages depuis la base de données
        Cursor cursor = dbHelper.getAllVoyages();

        // Parcourir le curseur pour extraire les voyages
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String nom = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOM));
                @SuppressLint("Range") String dateDepart = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_DEPART));
                @SuppressLint("Range") String dateFin = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_FIN));
                @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_PATH));
                // Créer un objet Voyage à partir des données récupérées
                Voyage voyage = new Voyage(id, nom, dateDepart, dateFin, imagePath);

                // Ajouter le voyage à la liste
                voyagesList.add(voyage);
            } while (cursor.moveToNext());

            // Fermer le curseur
            cursor.close();

            // Actualiser l'adaptateur de la liste
            voyageAdapter.notifyDataSetChanged();
            if (voyageAdapter != null) {
                voyageAdapter.notifyDataSetChanged();
            }
        }
    }
    private void loadMapFragment() {
        MapFragment mapFragment = new MapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mapFragment);

        transaction.commit();
    }
}