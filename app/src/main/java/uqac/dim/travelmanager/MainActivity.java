package uqac.dim.travelmanager;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView; // Déclaration de la SearchView
    private static Context context;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            loadMapFragment();
        }

        ImageButton imageButtonCreerVoyage = findViewById(R.id.btn_plus);
        imageButtonCreerVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreerVoyageActivity.class);
                startActivity(intent);
            }
        });
        // Récupération de la SearchView depuis le layout XML
        searchView = findViewById(R.id.search_view);

        // Ajout d'un listener pour écouter les changements dans la SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO
                return true;
            }
        });
    }

    private void loadMapFragment() {
        mapFragment = new MapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mapFragment);
        transaction.commit();
    }

    public void goToLocation(View view) {
    }

    private class FindLocationTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... queries) {
            String query = queries[0];
            GeocoderNominatim geocoder = new GeocoderNominatim("test");
            try {
                return geocoder.getFromLocationName(query, 1);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Address> geoPoints) {
            if (geoPoints == null || geoPoints.isEmpty()) {
                Toast.makeText(MainActivity.this, "Lieu introuvable.", Toast.LENGTH_SHORT).show();
            } else {
                Address address = geoPoints.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                String title = (address.getFeatureName() != null) ? address.getFeatureName() : "";
                title += address.getAddressLine(0)+", "+address.getLocality()+", "+address.getPostalCode();
                // Centre la carte et ajoute le marqueur
                if (mapFragment != null) {
                    mapFragment.centerOnLocation(latitude, longitude);
                    mapFragment.addMarker(latitude, longitude, title);
                }
            }
        }
    }

    public void findLocation(String query) {
        new FindLocationTask().execute(query);
    }
}
