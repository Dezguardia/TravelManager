package uqac.dim.travelmanager.models;

import java.util.ArrayList;

public class Location {

    int id;
    String name;
    String latitude;
    String longitude;
    String description;
    String img;

    public Location(String name, String latitude, String longitude, String description, String img) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.img = img;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }
    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        Location test = new Location("San Francisco",
                "37.773972",
                "-122.431297",
                "San Francisco, officially the City and County of San Francisco, is a commercial, financial, and cultural center in Northern California. With a population of 808,437 residents as of 2022, San Francisco is the fourth most populous city in the U.S. state of California.", "img.jpg");
        Location test2 = new Location("Los Angeles",
                "34.052235",
                "-118.243683",
                "Los Angeles, often referred to by its initials L.A., is the most populous city in the U.S. state of California. With roughly 3.9 million residents within the city limits as of 2020, Los Angeles is the second-most populous city in the United States, behind only New York City; it is also the commercial, financial and cultural center of Southern California.",
                "img2.jpg");
        locations.add(test);
        locations.add(test);
        return locations;
    }
}
