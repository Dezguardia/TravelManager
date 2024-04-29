package uqac.dim.travelmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Lieu implements Parcelable{

    String lieu;
    String transport;

    public Lieu(String lieu, String transport){
        this.lieu = lieu;
        this.transport = transport;
    }
    protected Lieu(Parcel in) {
        lieu = in.readString();
        transport = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lieu);
        dest.writeString(transport);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lieu> CREATOR = new Creator<Lieu>() {
        @Override
        public Lieu createFromParcel(Parcel in) {
            return new Lieu(in);
        }

        @Override
        public Lieu[] newArray(int size) {
            return new Lieu[size];
        }
    };
    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Lieu{" +
                "lieu='" + lieu + '\'' +
                ", transport='" + transport + '\'' +
                '}';
    }
}
