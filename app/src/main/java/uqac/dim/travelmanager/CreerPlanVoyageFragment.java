package uqac.dim.travelmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CreerPlanVoyageFragment extends Fragment {
    TextView textView_transport;
    private ImageButton editButton;
    private TextView textView;
    private EditText editText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfler le layout XML de votre fragment
        View rootView = inflater.inflate(R.layout.fragment_creer_plan_voyage, container, false);

        textView_transport = rootView.findViewById(R.id.texte_cliquable);
        textView_transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher une boîte de dialogue ou une liste déroulante pour choisir le mode de transport
                afficherListeModesTransport();
            }
        });

        editButton = rootView.findViewById(R.id.edit_button);
        textView = rootView.findViewById(R.id.text_view);
        editText = rootView.findViewById(R.id.edit_text);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditTextVisibility();
            }
        });
        return rootView;
    }

    private void toggleEditTextVisibility() {
        if (editText.getVisibility() == View.VISIBLE) {
            // L'EditText est déjà visible, le cacher et mettre à jour le TextView
            editText.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE); // Afficher à nouveau le TextView
            editButton.setVisibility(View.VISIBLE); // Afficher à nouveau le bouton
            textView.setText(editText.getText().toString());
        } else {
            // L'EditText est invisible, le rendre visible et cacher le TextView et le bouton
            editText.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }
    }
    private void afficherListeModesTransport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choisir un mode de transport")
                .setItems(R.array.modes_transport, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Récupérer le tableau de chaînes des modes de transport
                        String[] modesTransport = getResources().getStringArray(R.array.modes_transport);

                        // Récupérer le mode de transport choisi à partir de l'indice
                        String modeTransportChoisi = modesTransport[which];

                        // Mettre à jour le texte du TextView avec le mode de transport choisi
                        textView_transport.setText(modeTransportChoisi);                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
