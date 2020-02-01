package amits.parking.com.parking;

public class AddedPlaces {

    private String id;
    private String address;
    private String place;
    private String carSlots;
    private String bikeSlots;

    public AddedPlaces(String address, String place, String carSlots, String bikeSlots) {
        this.address = address;
        this.place = place;
        this.carSlots = carSlots;
        this.bikeSlots = bikeSlots;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCarSlots() {
        return carSlots;
    }

    public void setCarSlots(String carSlots) {
        this.carSlots = carSlots;
    }

    public String getBikeSlots() {
        return bikeSlots;
    }

    public void setBikeSlots(String bikeSlots) {
        this.bikeSlots = bikeSlots;
    }
}
