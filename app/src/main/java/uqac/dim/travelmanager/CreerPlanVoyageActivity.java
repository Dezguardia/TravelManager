package uqac.dim.travelmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

import uqac.dim.travelmanager.database.DatabaseHelper;
import uqac.dim.travelmanager.models.Jour;
import uqac.dim.travelmanager.models.Lieu;
import uqac.dim.travelmanager.models.Voyage;

public class CreerPlanVoyageActivity extends AppCompatActivity {
    private EditText lieuDepartEditText ,lieuChoisi;
    private TextView moyenTransport;
    private Lieu lieu;
    Voyage voyage;

    Jour jour;
    ImageButton btnNouveauJour;
    ImageButton btnJourPrecedant;
    private int indiceJourActuel = 0;

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

        voyage = (Voyage) getIntent().getSerializableExtra("voyage");
        lieuDepartEditText = findViewById(R.id.lieu_depart_journee_edit_text);



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
        ImageButton imageButtonEnregistrementsVoyage = findViewById(R.id.btn_enregistrements);
        imageButtonEnregistrementsVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreerPlanVoyageActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
            }
        });

        btnNouveauJour = findViewById(R.id.nouveau_jour);
        btnJourPrecedant = findViewById(R.id.jour_precedant);



        btnNouveauJour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter 1 au jour
                indiceJourActuel++;
                // Obtenir les valeurs du lieu et de lieuDepart à partir du fragment
                String lieuDepart = getLieuDepart();
                Lieu lieu = fragment.getLieu();
                voyage.getJour(indiceJourActuel).setLieuDepart(lieuDepart);
                voyage.getJour(indiceJourActuel).ajouterLieu(lieu);
                // Afficher un nouveau fragment avec le jour mis à jour
                afficherNouveauFragment();
                // Mettre à jour la visibilité des boutons
                updateButtonVisibility();
            }
        });

        btnJourPrecedant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier si vous pouvez soustraire 1 au jour
                if (indiceJourActuel > 0) {
                    // Soustraire 1 au jour
                    indiceJourActuel--;
                    // Obtenir les valeurs du lieu et de lieuDepart à partir du fragment
                    String lieuDepart = getLieuDepart();
                    Lieu lieu = fragment.getLieu();
                    // Enregistrer les valeurs dans l'objet Jour
                    voyage.getJour(indiceJourActuel).setLieuDepart(lieuDepart);
                    voyage.getJour(indiceJourActuel).ajouterLieu(lieu);

                    // Afficher un nouveau fragment avec le jour mis à jour
                    afficherNouveauFragment();
                }
                // Mettre à jour la visibilité des boutons
                updateButtonVisibility();
            }
        });

        Button enregistrerButton = findViewById(R.id.enregistrer_button);
        enregistrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instancier votre DatabaseHelper
                DatabaseHelper dbHelper = new DatabaseHelper(CreerPlanVoyageActivity.this);

                // Vérifier s'il existe déjà un voyage avec le même nom, les mêmes dates de départ et d'arrivée
                Voyage existingVoyage = dbHelper.getExistingVoyage(voyage.getNomVoyage(), voyage.getDateDepart(), voyage.getDateFin());

                if (existingVoyage != null && existingVoyage.getJours().isEmpty()) {
                    // Si un voyage existant a été trouvé et qu'il n'a pas de jours avec des lieux associés,
                    // mettez à jour les informations de ce voyage
                    existingVoyage.setImagePath(voyage.getImagePath());
                    // Autres mises à jour d'informations si nécessaire

                    // Mettez à jour le voyage existant dans la base de données
                    long result = dbHelper.updateVoyage(existingVoyage);
                    if (result != -1) {
                        // Afficher un message de confirmation
                        Toast.makeText(CreerPlanVoyageActivity.this, "Voyage existant mis à jour avec succès", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreerPlanVoyageActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Afficher un message d'erreur en cas d'échec de la mise à jour
                        Toast.makeText(CreerPlanVoyageActivity.this, "Erreur lors de la mise à jour du voyage existant", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Sinon, insérer le nouveau voyage dans la base de données
                    long result = dbHelper.insertVoyageComplet(voyage);

                    // Vérifier si l'insertion a réussi
                    if (result != -1) {
                        // Afficher un message de confirmation
                        Toast.makeText(CreerPlanVoyageActivity.this, "Nouveau voyage enregistré avec succès", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreerPlanVoyageActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Afficher un message d'erreur en cas d'échec de l'insertion
                        Toast.makeText(CreerPlanVoyageActivity.this, "Erreur lors de l'enregistrement du nouveau voyage", Toast.LENGTH_SHORT).show();
                    }
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
    // Méthode pour mettre à jour la visibilité des boutons en fonction de l'indice du jour
    private void updateButtonVisibility() {
        if (indiceJourActuel == 0) {
            // Si l'indice du jour est 0, masquer le bouton "jour_precedant"
            btnJourPrecedant.setVisibility(View.GONE);
        } else {
            // Sinon, afficher le bouton "jour_precedant"
            btnJourPrecedant.setVisibility(View.VISIBLE);
        }

        if (indiceJourActuel == voyage.getJours().size()-1){
            btnNouveauJour.setVisibility(View.GONE);
        } else {
            btnNouveauJour.setVisibility(View.VISIBLE);
        }
    }
    public Voyage getVoyage(){
        return voyage;
    }

    // Méthode pour récupérer le jour en fonction de l'indice
    private Jour getJourFromVoyage(int indiceJour) {
        Voyage voyage = getVoyage(); // Obtenez votre objet Voyage
        return voyage.getJour(indiceJour);
    }
    // Méthode pour afficher un nouveau fragment avec le jour correspondant
    private void afficherNouveauFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Créez une nouvelle instance de votre fragment
        CreerPlanVoyageFragment fragment = new CreerPlanVoyageFragment();
        // Vérifiez d'abord si la liste de jours dans le voyage est valide
        if (voyage != null && voyage.getJours() != null && !voyage.getJours().isEmpty()) {
            int nombreJours = voyage.getJours().size();
            if (indiceJourActuel >= 0 && indiceJourActuel < nombreJours) {
                // Obtenez le jour correspondant à l'indice actuel
                jour = voyage.getJour(indiceJourActuel);

                // Passez le jour au fragment en utilisant un Bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("jour", (Serializable) jour);
                fragment.setArguments(bundle);

                // Ajoutez le fragment à votre activité
                fragmentTransaction.replace(R.id.fragment_plan_voyage, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                updateButtonVisibility();

                // Mettez à jour le numéro du jour
                TextView numeroJourTextView = findViewById(R.id.numero_jour);
                numeroJourTextView.setText("Jour " + (indiceJourActuel + 1));

                // Mettez à jour la date du voyage
                TextView dateVoyageTextView = findViewById(R.id.date_voyage);
                dateVoyageTextView.setText(jour.getDate());

                ImageButton nouveauJourButton = findViewById(R.id.nouveau_jour);

                // Vérifiez si vous êtes sur le dernier jour
                if (indiceJourActuel == nombreJours) {
                    // Si c'est le dernier jour, masquez le bouton
                    nouveauJourButton.setVisibility(View.GONE);
                } else {
                    // Si ce n'est pas le dernier jour, affichez le bouton
                    nouveauJourButton.setVisibility(View.VISIBLE);
                }
            } else {
                Log.e("CreerPlanVoyageActivity", "Index de jour invalide: " + indiceJourActuel);
            }
        } else {
            Log.e("CreerPlanVoyageActivity", "Voyage ou liste de jours invalide(s)");
        }
    }



    public String getLieuDepart() {
        // Obtenez la valeur de lieuDepart à partir de vos vues
        String lieuDepart = lieuDepartEditText.getText().toString(); // textView est votre TextView pour lieuDepart
        return lieuDepart;
    }

}