package uqac.dim.travelmanager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uqac.dim.travelmanager.database.DatabaseHelper;
import uqac.dim.travelmanager.models.Jour;
import uqac.dim.travelmanager.models.Voyage;

public class ModifierVoyageActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText nouveauNomEditText;
    private EditText nouvelleDateDepartEditText;
    private EditText nouvelleDateFinEditText;
    private TextView nomEditText, dateDepartEditText, dateFinEditText;
    private long voyageId;
    private Bundle bundle;
    private String nomVoyage, dateDepart, dateFin;
    private String nouveauNomVoyage, nouvelleDateDepart, nouvelleDateFin;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modifier_voyage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);
        // Récupérer l'ID du voyage à modifier depuis l'intent
        bundle = getIntent().getExtras();
        //Intent intent = getIntent();
        if (bundle != null){
            long voyageId = bundle.getLong("id");
            //voyageId = intent.getLongExtra("voyageId", -1);
            Log.d("ModifierVoyageActivity", "ID du voyage récupéré : " + voyageId);

            // Vérifier si l'ID du voyage est valide
            if (voyageId == -1) {
                Log.e("ModifierVoyageActivity", "ID du voyage non trouvé dans l'Intent");
                finish();
            }
            nomVoyage = bundle.getString("nom");
            dateDepart = bundle.getString("dateDepart");
            dateFin = bundle.getString("dateFin");

            nomEditText = findViewById(R.id.editText_nom_voyage);
            nomEditText.setText(nomVoyage);

            dateDepartEditText = findViewById(R.id.editText_date_depart);
            dateDepartEditText.setText(dateDepart);

            dateFinEditText = findViewById(R.id.editText_date_fin);
            dateFinEditText.setText(dateFin);

            nouveauNomEditText = findViewById(R.id.modifier_nom_voyage);
            nouvelleDateDepartEditText = findViewById(R.id.modifier_date_depart);
            nouvelleDateFinEditText = findViewById(R.id.modifier_date_retour);

            nouveauNomVoyage = nouveauNomEditText.getText().toString();
            nouvelleDateDepart = nouvelleDateDepartEditText.getText().toString();
            nouvelleDateFin = nouvelleDateFinEditText.getText().toString();

        } else {
            Log.e("Modifier Voyage", "erreur lors du chargement du voyage");
        }

        // Bouton pour enregistrer les modifications
        Button enregistrerButton = findViewById(R.id.button_enregistrer_modifications);
        enregistrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ModifierVoyageActivity", "Clic sur le bouton Enregistrer");

                if (nouveauNomVoyage.isEmpty()){
                    nouveauNomVoyage = nomVoyage;
                }
                if(nouvelleDateDepart.isEmpty()){
                    nouvelleDateDepart = dateDepart;
                }
                if (nouvelleDateFin.isEmpty()){
                    nouvelleDateFin = dateFin;
                }

                // Convertir les dates en objets Date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date depart, fin;
                try {
                    depart = sdf.parse(nouvelleDateDepart);
                    fin = sdf.parse(nouvelleDateFin);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ModifierVoyageActivity.this, "Format de date incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier que la date de départ est après aujourd'hui
                Date aujourdhui = new Date();
                if (depart.before(aujourdhui)) {
                    Toast.makeText(ModifierVoyageActivity.this, "La date de départ doit être après aujourd'hui", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier que la date de fin est après la date de départ
                if (fin.before(depart)) {
                    Toast.makeText(ModifierVoyageActivity.this, "La date de fin doit être après la date de départ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier que la date de départ n'est pas la même que la date de fin
                if (depart.equals(fin)) {
                    Toast.makeText(ModifierVoyageActivity.this, "La date de départ ne peut pas être la même que la date de fin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Voyage voyage = new Voyage(voyageId, nouveauNomVoyage, nouvelleDateDepart, nouvelleDateFin);
                Log.d("suivant", "Voyage : " + voyage);

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
                Log.d("ModifierVoyageActivity", "Clic sur le bouton Enregistrer");
                //long rowsUpdated = databaseHelper.updateVoyage(voyage);
                int rowsUpdated = databaseHelper.updateVoyageById(voyageId, voyage);

                // Mettre à jour le voyage dans la base de données
                //int success = databaseHelper.updateVoyage(voyage);
                if (rowsUpdated > 0) {
                    Log.d("ModifierVoyageActivity", "Voyage mis à jour avec succès");
                    Toast.makeText(ModifierVoyageActivity.this, "Voyage mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    // Terminer l'activité et retourner à l'activité précédente
                    finish();
                } else {
                    Toast.makeText(ModifierVoyageActivity.this, "Échec de la mise à jour du voyage", Toast.LENGTH_SHORT).show();
                    Log.d("ModifierVoyageActivity", "Échec de la mise à jour du voyage");
                }
            }
        });

        ImageButton imageButtonAccueil = findViewById(R.id.btn_accueil);
        imageButtonAccueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifierVoyageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton imageButtonCreerVoyage = findViewById(R.id.btn_plus);
        imageButtonCreerVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifierVoyageActivity.this, CreerVoyageActivity.class);
                startActivity(intent);
            }
        });

        ImageButton imageButtonEnregistrementsVoyage = findViewById(R.id.btn_enregistrements);
        imageButtonEnregistrementsVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifierVoyageActivity.this, EnregistrementsVoyagesActivity.class);
                startActivity(intent);
            }
        });

        ImageButton imageButtonRetour = findViewById(R.id.btn_retour);
        imageButtonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(ModifierVoyageActivity.this, EnregistrementsVoyagesActivity.class);
                //startActivity(intent);
                finish();
            }
        });

    }

    public void showDateDepart(View v) {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        nouvelleDateDepartEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void showDateRetour(View v) {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        nouvelleDateFinEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}