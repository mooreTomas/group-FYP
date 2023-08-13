package test.example.firstapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import test.example.firstapplication.R;
import test.example.firstapplication.databinding.ActivityMapsBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    // set a value by default in case onMarkerListener doesn't catch it
    static String chosenAssessorID;

    public void returnToBerActivity() {
        Intent intent = new Intent(this, BerActivity.class);
        finish();
        startActivity(intent);
    }

    public static String getChosenAssessorID() {
        return chosenAssessorID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void confirmationDialog() {

        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Assessor Chosen! Hit Ok and then return to Form Page");
        alertDlg.setCancelable(false);

        alertDlg.setPositiveButton("OK!", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        //alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        //   @Override
        // public void onClick(DialogInterface dialogInterface, int i) {
        // do nothing
        // }
        // });

        alertDlg.create().show();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Nyle_Wolfe = new LatLng(52.6386, -8.6531);
        LatLng James_Geary = new LatLng(52.6, -8.6);
        mMap.addMarker(new MarkerOptions().position(Nyle_Wolfe).title("Nyle Wolfe"));
        mMap.addMarker(new MarkerOptions().position(James_Geary).title("James Geary"));

        // start Maps zoomed in appropriately
        float zoomLevel = 10.0f;
        LatLng startingZoom = new LatLng(52.6386, -8.6531);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingZoom, zoomLevel));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(limerick1));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String markerName = marker.getTitle();
                Toast.makeText(MapsActivity.this, "You've chosen " + markerName + " as your Assessor", Toast.LENGTH_SHORT).show();
                if (markerName == "Nyle_Wolfe") {
                    chosenAssessorID = "Pn4JSGOZJccjPG2CtCXOhKQul8s2";
                } else {
                    chosenAssessorID = "wX5Cxpbsunb56GNzavCNJ22kE8e2";
                }

                confirmationDialog();
                return false;
            }




        });

    }


}






