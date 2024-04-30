package uqac.dim.travelmanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uqac.dim.travelmanager.models.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private ArrayList<Location> locations;
    private Context context;

    public LocationAdapter(Context context) {
        this.context = context;
        this.locations = new ArrayList<>();
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card_layout, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.textViewName.setText(location.getName());
        holder.textViewDescription.setText(location.getDescription());

        Log.w("RESSOURCEIMG",location.getImg());
        // Charger l'image à partir du nom de fichier dans le dossier drawable
        int resourceId = context.getResources().getIdentifier( location.getImg(), "drawable", context.getPackageName() );
        // Set the image resource if resourceId is valid
        if (resourceId != 0) {
            holder.imageView.setImageResource(resourceId);
        } else {
            // Handle the case where the resource ID is not found (e.g., display a placeholder image)
            holder.imageView.setImageResource(R.drawable.image);
            Log.e("RESSOURCEIMG", "Image not found in the drawable folder");
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription;
        ImageView imageView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageView = itemView.findViewById(R.id.travelImageView);
        }
    }
}
