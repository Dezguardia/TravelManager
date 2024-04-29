package uqac.dim.travelmanager.models;


import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Voyage implements Serializable {
    private String nomVoyage;
    private String dateDepart;
    private String dateFin;
    private String imagePath;
    private ArrayList<Jour> jours;
    private long id;

    public Voyage(String nomVoyage, String dateDepart, String dateFin, String imagePath) {
        this.nomVoyage = nomVoyage;
        this.dateDepart = dateDepart;
        this.dateFin = dateFin;
        this.imagePath = imagePath;
        this.jours = new ArrayList<>();
    }
    public Voyage(long id, String nomVoyage, String dateDepart, String dateFin, String imagePath) {
        this.id = id;
        this.nomVoyage = nomVoyage;
        this.dateDepart = dateDepart;
        this.dateFin = dateFin;
        this.imagePath = imagePath;
        this.jours = new ArrayList<>();
    }

    public Voyage(long id, String nomVoyage, String dateDepart, String dateFin) {
        this.id = id;
        this.nomVoyage = nomVoyage;
        this.dateDepart = dateDepart;
        this.dateFin = dateFin;
        this.imagePath = imagePath;
        this.jours = new ArrayList<>();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomVoyage() {
        return nomVoyage;
    }

    public void setNomVoyage(String nomVoyage) {
        this.nomVoyage = nomVoyage;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<Jour> getJours() {
        return jours;
    }

    public void setJours(ArrayList<Jour> jours) {
        this.jours = jours;
    }

    public void ajouterJour(Jour jour){
        this.jours.add(jour);
    }

    public void supprimerJour(Jour jour){
        this.jours.remove(jour);
    }

    public Jour getJour(int index){
        return jours.get(index);
    }
    @Override
    public String toString() {
        return "Voyage{" +
                "nomVoyage='" + nomVoyage + '\'' +
                ", dateDepart='" + dateDepart + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", jours=" + jours +
                '}';
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("nom", nomVoyage);
        bundle.putString("dateDepart", dateDepart);
        bundle.putString("dateFin", dateFin);
        bundle.putLong("id", id);
        //bundle.putParcelableArrayList("jours", (ArrayList<? extends Parcelable>) jours);
        bundle.putSerializable("jours", jours);
        return bundle;
    }

}


