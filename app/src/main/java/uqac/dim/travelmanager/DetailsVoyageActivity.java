package uqac.dim.travelmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import uqac.dim.travelmanager.models.Jour;
import uqac.dim.travelmanager.models.JourAdapter;
import uqac.dim.travelmanager.models.Voyage;
import uqac.dim.travelmanager.models.VoyageAdapter;


public class DetailsVoyageActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView mRecyclerView;
    private Bundle bundle;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_voyage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiser le DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        // Récupérer les informations du voyage de l'intent
        bundle = getIntent().getExtras();

        if (bundle != null) {
            String nomVoyage = bundle.getString("nom");
            String dateDepart = bundle.getString("dateDepart");
            String dateFin = bundle.getString("dateFin");

            // Afficher les informations du voyage dans votre mise en page
            TextView nomTextView = findViewById(R.id.nom_du_voyage);
            nomTextView.setText(nomVoyage);

            TextView dateDepartTextView = findViewById(R.id.date_depart);
            dateDepartTextView.setText(dateDepart);

            TextView dateFinTextView = findViewById(R.id.date_fin);
            dateFinTextView.setText(dateFin);

            // Initialize RecyclerView
            mRecyclerView = findViewById(R.id.recycler_view_jours);
            // Create a list to hold the days
            List<Jour> joursList = (List<Jour>) bundle.getSerializable("jours");
            // Create an adapter for the RecyclerView
            JourAdapter jourAdapter = new JourAdapter(this, joursList);
            // Set the adapter to the RecyclerView
            mRecyclerView.setAdapter(jourAdapter);
            // Set the layout manager for the RecyclerView
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Récupérer l'ID du voyage
            long voyageId = bundle.getLong("id");

            if (voyageId != -1) {
                Log.d("DetailsVoyageActivity", "ID du voyage récupéré : " + voyageId);
                Voyage voyage = databaseHelper.getVoyageById(voyageId);
                // Récupérer les jours associés au voyage depuis la base de données
                List<Jour> jours = databaseHelper.getJoursForVoyage(voyageId);
                joursList.addAll(jours);
                // Notify the adapter that the data set has changed
                jourAdapter.notifyDataSetChanged();

                if (jours.isEmpty()) {
                    Log.d("DetailsVoyageActivity", "La liste des jours est vide.");
                } else {
                    Log.d("DetailsVoyageActivity", "Nombre de jours récupérés : " + jours.size());
                }

                // Ajouter les jours au voyage affiché
                for (Jour jour : jours) {
                    voyage.ajouterJour(jour);
                }

            } else {
                Log.e("DetailsVoyageActivity", "ID du voyage non trouvé dans l'Intent");
            }
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                loadMapFragment();
                return true;
            } else if (itemId == R.id.navigation_travel) {
                Intent intentTravel = new Intent(DetailsVoyageActivity.this, TravelActivity.class);
                startActivity(intentTravel);
                return true;
            } else if (itemId == R.id.navigation_add) {
                Intent intent = new Intent(DetailsVoyageActivity.this, CreerVoyageActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                Intent intent = new Intent(DetailsVoyageActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_options) {
                Intent intentOptions = new Intent(DetailsVoyageActivity.this, OptionsActivity.class);
                startActivity(intentOptions);
                return true;
            }

            // Si aucun des éléments ne correspond, renvoyez false
            return false;
        });


        Button buttonSupprimerVoyage = findViewById(R.id.button_supprimer_voyage);
        buttonSupprimerVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer l'ID du voyage à supprimer
                //long voyageId = getIntent().getLongExtra("voyageId", -1);
                long voyageId = bundle.getLong("id");
                Log.d("Enregistrement id", "ID du voyage : " + voyageId);
                if (voyageId != -1) {
                    Log.d("Supprimer voyage", "Voyage Id différent de -1");
                    // Supprimer le voyage de la base de données
                    boolean isDeleted = databaseHelper.supprimerVoyage(voyageId);
                    if (isDeleted) {
                        // Afficher un message indiquant que le voyage a été supprimé avec succès
                        Toast.makeText(DetailsVoyageActivity.this, "Voyage supprimé avec succès", Toast.LENGTH_SHORT).show();
                        Log.d("Supprimer voyage", "Le voyage a été supprimer : ");

                        // Rediriger l'utilisateur vers une autre activité, par exemple la liste des voyages
                        Intent intent = new Intent(DetailsVoyageActivity.this, EnregistrementsVoyagesActivity.class);
                        startActivity(intent);
                        finish();
                        // Fermer cette activité pour empêcher l'utilisateur de revenir en arrière
                    } else {
                        // Afficher un message d'erreur si la suppression du voyage a échoué
                        Toast.makeText(DetailsVoyageActivity.this, "Échec de la suppression du voyage", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("Supprimer voyage", "Voyage Id = -1");
                }
                Log.d("Supprimer voyage", "Le voyage est supprimer");
            }
        });



        Button buttonModifierVoyage = findViewById(R.id.button_modifier_voyage);
        buttonModifierVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long voyageId = bundle.getLong("id");
                Log.d("Enregistrement id", "ID du voyage : " + voyageId);
                if (voyageId != -1) {
                    Log.d("Modifier voyage", "Voyage Id différent de -1");
                    Intent intent = new Intent(DetailsVoyageActivity.this, ModifierVoyageActivity.class);
                    intent.putExtras(bundle);
                    //intent.putExtra("voyageId", voyageId);
                    startActivity(intent);
                }else {
                    Log.d("Modifier voyage", "Voyage Id = -1");
                }
            }
        });
        
    }
    private void loadMapFragment() {
        MapFragment mapFragment = new MapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mapFragment);

        transaction.commit();
    }
}
