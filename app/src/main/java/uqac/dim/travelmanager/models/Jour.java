package uqac.dim.travelmanager.models;

import static uqac.dim.travelmanager.models.Lieu.CREATOR;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Jour implements Serializable {

    String date;
    int numeroJour;
    ArrayList<Lieu> lieux;
    String lieuDepart;

    public Jour(String date, int numeroJour){
        this.date = date;
        this.numeroJour = numeroJour;
        this.lieux = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumeroJour() {
        return numeroJour;
    }

    public void setNumeroJour(int numeroJour) {
        this.numeroJour = numeroJour;
    }

    public ArrayList<Lieu> getLieux() {
        return lieux;
    }

    public void setLieux(ArrayList<Lieu> lieux) {
        this.lieux = lieux;
    }

    public void ajouterLieu(Lieu lieu) {
        lieux.add(lieu);
    }

    public void supprimerLieu(Lieu lieu) {
        lieux.remove(lieu);
    }

    public Lieu getLieu(int index) {
        return lieux.get(index);
    }

    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }
}
