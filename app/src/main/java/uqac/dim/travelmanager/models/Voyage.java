package uqac.dim.travelmanager.models;


import java.io.Serializable;

public class Voyage implements Serializable {
    private String nomVoyage;
    private String dateDepart;
    private String dateFin;
    private String imagePath;


    public Voyage(String nomVoyage, String dateDepart, String dateFin, String imagePath) {
        this.nomVoyage = nomVoyage;
        this.dateDepart = dateDepart;
        this.dateFin = dateFin;
        this.imagePath = imagePath;
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

    @Override
    public String toString() {
        return "Voyage{" +
                "nomVoyage='" + nomVoyage + '\'' +
                ", dateDepart='" + dateDepart + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}


