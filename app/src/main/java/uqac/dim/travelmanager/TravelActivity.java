package uqac.dim.travelmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import uqac.dim.travelmanager.R;
import uqac.dim.travelmanager.models.Location;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class TravelActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocationAdapter locationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        recyclerView = findViewById(R.id.travel_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationAdapter = new LocationAdapter(this);
        recyclerView.setAdapter(locationAdapter);

        // Charger les données d'emplacement et les définir dans l'adaptateur
        ArrayList<Location> locations = getAllLocations();
        locationAdapter.setLocations(locations);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                loadMapFragment();
                return true;
            } else if (itemId == R.id.navigation_travel) {
                Intent intentTravel = new Intent(TravelActivity.this, TravelActivity.class);
                startActivity(intentTravel);
                return true;
            } else if (itemId == R.id.navigation_add) {
                Intent intent = new Intent(TravelActivity.this, CreerVoyageActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                Intent intent = new Intent(TravelActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_options) {
                Intent intentOptions = new Intent(TravelActivity.this, OptionsActivity.class);
                startActivity(intentOptions);
                return true;
            }

            return false;
        });


    }
    private ArrayList<Location> getAllLocations() {
        // Simulez la récupération des données d'emplacement depuis une source de données
        Location location1 = new Location("San Francisco", "37.773972", "-122.431297", "San Francisco, officially the City and County of San Francisco, is a commercial, financial, and cultural center in Northern California. With a population of 808,437 residents as of 2022, San Francisco is the fourth most populous city in the U.S. state of California.", "img1");
        Location location2 = new Location("Los Angeles", "34.052235", "-118.243683", "Los Angeles, often referred to by its initials L.A., is the most populous city in the U.S. state of California. With roughly 3.9 million residents within the city limits as of 2020, Los Angeles is the second-most populous city in the United States, behind only New York City; it is also the commercial, financial and cultural center of Southern California.", "img2");

        ArrayList<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        return locations;
    }
    private void loadMapFragment() {
        MapFragment mapFragment = new MapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mapFragment);

        transaction.commit();
    }
}
