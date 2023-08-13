package test.example.firstapplication;

public class CustomerPropertyForm {
    private String ownerName, propertyAge, atticInsulation, wallInsulation, floorInsulation, heatingType, heatingAge, windowGlazing;

    public CustomerPropertyForm(String ownerName, String propertyAge, String atticInsulation, String wallInsulation, String floorInsulation, String heatingType, String heatingAge, String windowGlazing) {
        this.ownerName = ownerName;
        this.propertyAge = propertyAge;
        this.atticInsulation = atticInsulation;
        this.wallInsulation = wallInsulation;
        this.floorInsulation = floorInsulation;
        this.heatingType = heatingType;
        this.heatingAge = heatingAge;
        this.windowGlazing = windowGlazing;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPropertyAge() {
        return propertyAge;
    }

    public String getAtticInsulation() {
        return atticInsulation;
    }

    public String getWallInsulation() {
        return wallInsulation;
    }

    public String getFloorInsulation() {
        return floorInsulation;
    }

    public String getHeatingType() {
        return heatingType;
    }

    public String getHeatingAge() {
        return heatingAge;
    }

    public String getWindowGlazing() {
        return windowGlazing;
    }
}
