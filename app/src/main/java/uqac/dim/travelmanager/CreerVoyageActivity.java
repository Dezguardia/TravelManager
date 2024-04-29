package uqac.dim.travelmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uqac.dim.travelmanager.database.DatabaseHelper;
import uqac.dim.travelmanager.models.Jour;
import uqac.dim.travelmanager.models.Voyage;

public class CreerVoyageActivity extends AppCompatActivity {
    EditText dateDepartEditText, dateFinEditText, nomVoyageEditText;
    String imagePath;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnImageCouverture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creer_voyage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nomVoyageEditText = findViewById(R.id.nom_voyage_edit_text);
        dateDepartEditText = findViewById(R.id.date_edit_text1);
        dateFinEditText = findViewById(R.id.date_edit_text2);

        btnImageCouverture = findViewById(R.id.btn_telecharger_image);
        btnImageCouverture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        Button suivantButton = findViewById(R.id.suivant_button);
        suivantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomVoyage = nomVoyageEditText.getText().toString();
                String dateDepart = dateDepartEditText.getText().toString();
                String dateFin = dateFinEditText.getText().toString();

                // Vérifier si les champs ne sont pas vides
                if (nomVoyage.isEmpty() || dateDepart.isEmpty() || dateFin.isEmpty()) {
                    Toast.makeText(CreerVoyageActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convertir les dates en objets Date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date depart, fin;
                try {
                    depart = sdf.parse(dateDepart);
                    fin = sdf.parse(dateFin);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(CreerVoyageActivity.this, "Format de date incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier que la date de départ est après aujourd'hui
                Date aujourdhui = new Date();
                if (depart.before(aujourdhui)) {
                    Toast.makeText(CreerVoyageActivity.this, "La date de départ doit être après aujourd'hui", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier que la date de fin est après la date de départ
                if (fin.before(depart)) {
                    Toast.makeText(CreerVoyageActivity.this, "La date de fin doit être après la date de départ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier que la date de départ n'est pas la même que la date de fin
                if (depart.equals(fin)) {
                    Toast.makeText(CreerVoyageActivity.this, "La date de départ ne peut pas être la même que la date de fin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Voyage voyage = new Voyage(nomVoyage, dateDepart, dateFin, imagePath);
                Log.d("suivant", "Voyage : " + voyage);

                DatabaseHelper databaseHelper = new DatabaseHelper(CreerVoyageActivity.this);
                /*
                // Vérifier si l'objet Voyage possède un ID
                if (voyage.getId() == -1) {
                    // L'objet Voyage ne possède pas d'ID, donc générer un ID unique
                    long uniqueId = databaseHelper.generateUniqueVoyageId();
                    voyage.setId(uniqueId);
                }
                 */
                Log.d("Creer voyage", "id voyage : " + voyage.getId());
                // Ajouter chaque jour entre la date de départ et la date de fin au voyage
                for (Date date = depart; !date.after(fin);) {
                    // Créer un objet Jour pour chaque jour et l'ajouter au voyage
                    Jour jour = new Jour(sdf.format(date), voyage.getJours().size() + 1);
                    // Incrementer le numero de jour
                    voyage.ajouterJour(jour);

                    // Ajouter un jour à la date
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    date = calendar.getTime();
                }

                // Insérer les données dans la base de données
                long result = databaseHelper.insertVoyage(voyage.getNomVoyage(), voyage.getDateDepart(), voyage.getDateFin(), voyage.getImagePath());

                if (result != -1) {
                    // Les données ont été insérées avec succès
                    Log.d("Suivant", "Voyage enregistré dans la base de données avec l'ID : " + result);
                    Intent intent = new Intent(CreerVoyageActivity.this, CreerPlanVoyageActivity.class);
                    intent.putExtra("voyage", voyage);
                    startActivity(intent);
                } else {
                    // Échec de l'insertion des données
                    Log.e("Suivant", "Erreur lors de l'enregistrement du voyage dans la base de données");
                }


            }
        });

        Button annulerButton = findViewById(R.id.annuler_button);
        annulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreerVoyageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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
                Intent intentTravel = new Intent(CreerVoyageActivity.this, TravelActivity.class);
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
                Intent intent = new Intent(CreerVoyageActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadMapFragment() {
        MapFragment mapFragment = new MapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mapFragment);

        transaction.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // L'utilisateur a sélectionné une image depuis la galerie
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                btnImageCouverture.setImageBitmap(bitmap);
                imagePath = getPathFromUri(uri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getPathFromUri(Uri uri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    public void showDatePickerDialog1(View v) {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateDepartEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void showDatePickerDialog2(View v) {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateFinEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}