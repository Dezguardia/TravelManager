package uqac.dim.travelmanager.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import uqac.dim.travelmanager.R;
import uqac.dim.travelmanager.models.Jour;

public class JourAdapter extends RecyclerView.Adapter<JourAdapter.JourViewHolder> {

    private Context context;
    private List<Jour> jourList;

    public JourAdapter(Context context, List<Jour> jourList) {
        this.context = context;
        this.jourList = jourList;
    }

    @NonNull
    @Override
    public JourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jour, parent, false);
        return new JourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JourViewHolder holder, int position) {
        Jour jour = jourList.get(position);
        holder.bind(jour);
    }

    @Override
    public int getItemCount() {
        return jourList.size();
    }

    public class JourViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView lieuTextView;

        public JourViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            lieuTextView = itemView.findViewById(R.id.lieuTextView);
        }

        public void bind(Jour jour) {
            dateTextView.setText(jour.getDate());
            lieuTextView.setText(jour.getLieuDepart());
        }
    }
}

