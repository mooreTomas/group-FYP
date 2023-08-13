package test.example.firstapplication;

public class Notification {
    String custId, berId, details, dateAdded;

    public Notification() {
    }

    public Notification(String custId, String berId, String details, String dateAdded) {
        this.custId = custId;
        this.berId = berId;
        this.details = details;
        this.dateAdded = dateAdded;
    }

    public String getCustId() {
        return custId;
    }

    public String getBerId() {
        return berId;
    }

    public String getDetails() {
        return details;
    }

    public String getDateAdded() {
        return dateAdded;
    }

}
