package uqac.dim.travelmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class OptionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        // Obtenez les vues à partir de la disposition et configurez-les
        TextView aboutTextView = view.findViewById(R.id.text_about);
        aboutTextView.setText("À propos de l'application :\nVersion 1.0\nDéveloppée par Gabriel Houdry--Bohême, Solène Lecomte et Hector Ménétrier");

        TextView assistanceTextView = view.findViewById(R.id.text_assistance);
        assistanceTextView.setText("Assistance :\nContactez-nous à support@travelmanager.com");

        // Vous pouvez ajouter plus de TextView pour d'autres catégories ou options

        return view;
    }
}
