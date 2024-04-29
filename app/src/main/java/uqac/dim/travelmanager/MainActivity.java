package uqac.dim.travelmanager;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;
import uqac.dim.travelmanager.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView; // Déclaration de la SearchView
    private static Context context;
    private MapFragment mapFragment;
    private RoutingManager routingManager = new RoutingManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            loadMapFragment();
        }

        // Initialisation de BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ajoutez un écouteur de navigation pour gérer les sélections de menu
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Utilisez une déclaration if-else if-else pour gérer les sélections de menu
            if (itemId == R.id.navigation_home) {
                // Appellez votre méthode pour charger le fragment de carte
                loadMapFragment();
                return true;
            } else if (itemId == R.id.navigation_travel) {
                Intent intentTravel = new Intent(MainActivity.this, TravelActivity.class);
                startActivity(intentTravel);
                return true;
            } else if (itemId == R.id.navigation_add) {
                // Redirige le bouton "Ajouter" vers l'activité CreerVoyageActivity
                Intent intent = new Intent(MainActivity.this, CreerVoyageActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                //Intent intentFavorites = new Intent(MainActivity.this, FavoritesActivity.class);
                //startActivity(intentFavorites);
                return true;
            } else if (itemId == R.id.navigation_options) {
                //Intent intentOptions = new Intent(MainActivity.this, OptionsActivity.class);
                //startActivity(intentOptions);
                return true;
            }

            // Si aucun des éléments ne correspond, renvoyez false
            return false;
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

        ImageButton imageButtonEnregistrementsVoyage = findViewById(R.id.btn_enregistrements);
        imageButtonEnregistrementsVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
            }
        });
    }


        // Vous avez déjà une écoute d'événement pour le bouton "Ajouter"
        // Vous pouvez le laisser tel quel ou supprimer le code ci-dessous
        // si vous utilisez `setOnItemSelectedListener` pour gérer le bouton "Ajouter"
        // dans le menu de navigation.

        // ImageButton imageButtonCreerVoyage = findViewById(R.id.btn_plus);
        // imageButtonCreerVoyage.setOnClickListener(v -> {
        //     Intent intent = new Intent(MainActivity.this, CreerVoyageActivity.class);
        //     startActivity(intent);
        // });


    // Méthode pour charger le fragment de carte
    private void loadMapFragment() {
        mapFragment = new MapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mapFragment);
        transaction.commit();
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
                    routingManager.addToGeopoints(new GeoPoint(latitude, longitude));
                }
            }
        }
    }

    public void findLocation(String query) {
        new FindLocationTask().execute(query);
    }

    public void goToLocation(View view) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            if (!routingManager.getGeopoints().isEmpty()) {
                                GeoPoint endPoint = routingManager.getGeopoints().get(routingManager.getGeopoints().size()-1);
                                mapFragment.centerOnLocation(location.getLatitude(), location.getLongitude());
                                new RouteTask(startPoint, endPoint).execute();
                            }
                        }
                    }
                });
    }
    private class RouteTask extends AsyncTask<Void, Void, Polyline> {

        private final GeoPoint startPoint;
        private final GeoPoint endPoint;
        private final Context context;

        public RouteTask(GeoPoint startPoint, GeoPoint endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.context = getApplicationContext();
        }

        @Override
        protected Polyline doInBackground(Void... voids) {
            return routingManager.createRoute(startPoint, endPoint, context);
        }

        @Override
        protected void onPostExecute(Polyline line) {
            if (line != null) {
                mapFragment.addRoute(line);
            } else {
                // Handle error or notify user
            }
        }
    }

}
