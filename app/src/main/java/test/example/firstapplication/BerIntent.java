package test.example.firstapplication;

public class BerIntent {
    private String ownerName, propertyAge, atticUpgrade, wallUpgrade, floorUpgrade, heatingUpgrade, windowUpgrade, custId, chosenAssessorID, date;


    public BerIntent() {
    }

    public BerIntent(String ownerName, String propertyAge, String atticUpgrade, String wallUpgrade, String floorUpgrade, String heatingUpgrade, String windowUpgrade, String chosenAssessorID, String custId, String date) {
        this.ownerName = ownerName;
        this.propertyAge = propertyAge;
        this.atticUpgrade = atticUpgrade;
        this.wallUpgrade = wallUpgrade;
        this.floorUpgrade = floorUpgrade;
        this.heatingUpgrade = heatingUpgrade;
        this.windowUpgrade = windowUpgrade;
        this.chosenAssessorID = chosenAssessorID;
        this.custId = custId;
        this.date = date;

    }

    @Override
    public String toString() {
        return "Ber Intent Application\n" +
                "Applicant Name: " + ownerName +
                "\nProperty Age: " + propertyAge +
                "\nAttic Upgrade: " + atticUpgrade +
                "\nWall Upgrade: " + wallUpgrade +
                "\nFloor Upgrade: " + floorUpgrade +
                "\nHeating Upgrade: " + heatingUpgrade +
                "\nWindow Upgrade: " + windowUpgrade +
                "\nDate: " + date + "\n";
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPropertyAge() {
        return propertyAge;
    }

    public String getAtticUpgrade() {
        return atticUpgrade;
    }

    public String getWallUpgrade() {
        return wallUpgrade;
    }

    public String getFloorUpgrade() {
        return floorUpgrade;
    }

    public String getHeatingUpgrade() {
        return heatingUpgrade;
    }

    public String getWindowUpgrade() {
        return windowUpgrade;
    }

    public String getChosenAssessorID() {
        return chosenAssessorID;
    }

    public String getDate() {
        return date;
    }

    public String getCustId() {
        return custId;
    }
}
