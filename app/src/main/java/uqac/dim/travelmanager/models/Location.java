package uqac.dim.travelmanager.models;

public class Location {

    int id;
    String name;
    String latitude;
    String longitude;

    public Location(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getLatitude(){
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
}
