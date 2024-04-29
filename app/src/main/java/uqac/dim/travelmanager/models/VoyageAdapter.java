package uqac.dim.travelmanager.models;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import uqac.dim.travelmanager.DetailsVoyageActivity;
import uqac.dim.travelmanager.R;
import uqac.dim.travelmanager.models.Voyage;
import com.squareup.picasso.Picasso;

public class VoyageAdapter extends RecyclerView.Adapter<VoyageAdapter.VoyageViewHolder> {

    private Context context;
    private List<Voyage> voyagesList;

    public VoyageAdapter(Context context, List<Voyage> voyagesList) {
        this.context = context;
        this.voyagesList = voyagesList;
    }
    @NonNull
    @Override
    public VoyageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voyage, parent, false);
        return new VoyageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VoyageViewHolder holder, int position) {
        Voyage currentVoyage = voyagesList.get(position);
        holder.bind(currentVoyage);
    }

    @Override
    public int getItemCount() {
        return voyagesList.size();
    }




    public class VoyageViewHolder extends RecyclerView.ViewHolder {
        private TextView nomTextView;
        private TextView dateDepartTextView;
        private TextView dateFinTextView;
        private ImageView imageView;

        public VoyageViewHolder(@NonNull View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.nom_voyage);
            dateDepartTextView = itemView.findViewById(R.id.date_depart_voyage);
            dateFinTextView = itemView.findViewById(R.id.date_fin_voyage);
            imageView = itemView.findViewById(R.id.image_voyage);

            nomTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Récupérer le voyage associé à cette vue
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Voyage voyage = voyagesList.get(position);
                        //voyage.setId(position);
                        Log.d("cliquer voyage", "L'id du voyage cliquer : " + voyage.getId());
                        Intent intent = new Intent(context, DetailsVoyageActivity.class);
                        // Ajouter les informations du voyage à l'intent
                        Bundle bundle = voyage.toBundle();
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                }
            });

        }

        public void bind(Voyage voyage) {
            nomTextView.setText(voyage.getNomVoyage());
            dateDepartTextView.setText(voyage.getDateDepart());
            dateFinTextView.setText(voyage.getDateFin());
            String imagePath = voyage.getImagePath();
            // Chargez l'image avec la bibliothèque Picasso
            Picasso.get().load(voyage.getImagePath()).into(imageView);
        }
/*
        @Override
        public void onClick(View view) {
            // Ouvrir une nouvelle activité avec les détails du voyage
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Voyage clickedVoyage = voyagesList.get(position);
                Intent intent = new Intent(context, DetailsVoyageActivity.class);
                intent.putExtra("voyage", clickedVoyage);
                context.startActivity(intent);
            }
        }

 */
    }
}

