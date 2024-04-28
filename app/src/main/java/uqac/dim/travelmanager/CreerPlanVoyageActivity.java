package uqac.dim.travelmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uqac.dim.travelmanager.models.Voyage;

public class CreerPlanVoyageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creer_plan_voyage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Voyage voyage = (Voyage) getIntent().getSerializableExtra("voyage");
        // Vérifier si l'objet Voyage n'est pas null
        if (voyage != null) {
            // Récupérer la date de départ du voyage
            String dateDepart = voyage.getDateDepart();

            // Trouver le TextView jour1
            TextView textViewJour1 = findViewById(R.id.date_voyage);

            // Définir la date de départ comme texte du TextView
            textViewJour1.setText(dateDepart);
        } else {
            // Gérer le cas où l'objet Voyage est null
            Log.e("CreerPlanVoyageActivity", "Objet Voyage est null dans l'intent");
        }

        CreerPlanVoyageFragment fragment = new CreerPlanVoyageFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_plan_voyage, fragment)
                .commit();

        ImageButton imageButton_ajouter_fragment = findViewById(R.id.btn_ajout_fragment);
        imageButton_ajouter_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créez une nouvelle instance de votre fragment à ajouter
                CreerPlanVoyageFragment nouveauFragment = new CreerPlanVoyageFragment();

                // Obtenez le gestionnaire de fragments et commencez une transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Ajoutez le nouveau fragment en dessous du fragment existant
                // Utilisez un identifiant unique pour chaque fragment ajouté
                int fragmentCount = fragmentManager.getBackStackEntryCount() + 1;
                fragmentTransaction.add(R.id.fragment_plan_voyage, nouveauFragment, "fragment_" + fragmentCount);

                // Ajoutez la transaction à la pile de retour pour permettre le retour en arrière
                fragmentTransaction.addToBackStack(null);

                // Validez la transaction
                fragmentTransaction.commit();
            }
        });

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
                Intent intentTravel = new Intent(CreerPlanVoyageActivity.this, TravelActivity.class);
                startActivity(intentTravel);
                return true;
            } else if (itemId == R.id.navigation_add) {
                // Redirige le bouton "Ajouter" vers l'activité CreerVoyageActivity
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


    }

    private void loadMapFragment() {
        MapFragment mapFragment = new MapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mapFragment);

        transaction.commit();
    }



}