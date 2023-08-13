package test.example.firstapplication;

public class Customer {

    public Customer() {
    }

    private String firstname, lastname, email, password, phone, latitude, longitude;

    public Customer(String firstname, String lastname, String email, String password, String phone,  String latitude, String longitude) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
