package uqac.dim.travelmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        OptionsFragment optionsFragment = new OptionsFragment();

        // Utilisez FragmentTransaction pour ajouter ou remplacer le fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Remplace le contenu du conteneur de l'activité (par exemple, R.id.fragment_container)
        transaction.replace(R.id.fragment_container, optionsFragment);

        // Commit la transaction
        transaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                loadMapFragment();
                return true;
            } else if (itemId == R.id.navigation_travel) {
                Intent intentTravel = new Intent(OptionsActivity.this, TravelActivity.class);
                startActivity(intentTravel);
                return true;
            } else if (itemId == R.id.navigation_add) {
                Intent intent = new Intent(OptionsActivity.this, CreerVoyageActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                Intent intent = new Intent(OptionsActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_options) {
                Intent intentOptions = new Intent(OptionsActivity.this, OptionsActivity.class);
                startActivity(intentOptions);
                return true;
            }

            // Si aucun des éléments ne correspond, renvoyez false
            return false;
        });


    }


    private void loadMapFragment() {
        MapFragment mapFragment = new MapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mapFragment);

        transaction.commit();
    }
}