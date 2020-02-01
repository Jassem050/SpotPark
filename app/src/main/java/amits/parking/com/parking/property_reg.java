package amits.parking.com.parking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class property_reg extends AppCompatActivity  {

    private static final String TAG = "property_reg";

    TextInputEditText placeaddr, placename, slotscar, slotsbike, carSlotWidth, carSlotHeight, bikeSlotWidth,
            bikeSlotHeight, carSlotPrice, bikeSlotPrice;
    String mappoint;
    utility utility;
    RequestQueue MyRequestQueue;
    private SessionManager sessionManager;
    private ActionBar actionBar;
    private MaterialButton selectLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_reg);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);
        placeaddr = (TextInputEditText) findViewById(R.id.address);
        placename = (TextInputEditText) findViewById(R.id.placename);
        slotscar = (TextInputEditText) findViewById(R.id.carslots);
        slotsbike = (TextInputEditText) findViewById(R.id.bikeslots);
        carSlotWidth = (TextInputEditText) findViewById(R.id.carslots_width);
        carSlotHeight = (TextInputEditText) findViewById(R.id.carslots_height);
        bikeSlotWidth = (TextInputEditText) findViewById(R.id.bikeslots_width);
        bikeSlotHeight = (TextInputEditText) findViewById(R.id.bikeslots_height);
        carSlotPrice = (TextInputEditText) findViewById(R.id.carslots_price);
        bikeSlotPrice = (TextInputEditText) findViewById(R.id.bikeslots_price);
        utility = new utility(this);
        MyRequestQueue = Volley.newRequestQueue(this);
        selectLocationBtn = (MaterialButton) findViewById(R.id.button2);

        selectLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeaddr.getText().toString().equals("")) {
                    placeaddr.setError("Enter address");
                } else if (placename.getText().toString().equals("")) {
                    placename.setError("Enter place name");
                } else if (slotscar.getText().toString().equals("")) {
                    slotscar.setError("Enter available car slots");
                } else if (slotsbike.getText().toString().equals("")) {
                    slotsbike.setError("Enter available bike slots");
                } else if (carSlotWidth.getText().toString().equals("")){
                    carSlotWidth.setError("Enter Car slot Width ");
                } else if (carSlotHeight.getText().toString().equals("")){
                    carSlotHeight.setError("Enter Car slot Height");
                } else if (bikeSlotWidth.getText().toString().equals("")){
                    bikeSlotWidth.setError("Enter Bike Slot Width");
                } else if (bikeSlotHeight.getText().toString().equals("")){
                    bikeSlotHeight.setError("Enter Bike Slot Height");
                } else if (carSlotPrice.getText().toString().equals("")){
                    carSlotPrice.setError("Enter the Car Slot Price");
                } else if (bikeSlotPrice.getText().toString().equals("")){
                    bikeSlotPrice.setError("Enter the Bike Slot Price");
                }else {
                    Intent intent = new Intent(property_reg.this, SelectLocationActivity.class);
                    intent.putExtra("address", placeaddr.getText().toString());
                    intent.putExtra("place", placename.getText().toString());
                    intent.putExtra("carslots", slotscar.getText().toString());
                    intent.putExtra("carslots_width", carSlotWidth.getText().toString());
                    intent.putExtra("carslots_height", carSlotHeight.getText().toString());
                    intent.putExtra("bikeslots", slotsbike.getText().toString());
                    intent.putExtra("bikeslots_width", bikeSlotWidth.getText().toString());
                    intent.putExtra("bikeslots_height", bikeSlotHeight.getText().toString());
                    intent.putExtra("carslots_price", carSlotPrice.getText().toString());
                    intent.putExtra("bikeslots_price", bikeSlotPrice.getText().toString());
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }








//    @Override
//    public void onMapLongClick(LatLng latLng) {
//        LatLng coordinate = new LatLng(lat, lng);
//        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 5);
//        mMap.animateCamera(yourLocation);
//
//        mappoint = latLng.toString();
//
//        mMap.clear();
//        mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title(latLng.toString())
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//
//        lat = latLng.latitude;
//        lng = latLng.longitude;
////        getAddressFromLocation(lat,lng);
//
//    }

    //    private void getAddressFromLocation(double latitude, double longitude) {
//
//        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
//
//
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            Toast.makeText(this, addresses.get(0).toString(), Toast.LENGTH_SHORT).show();
//            if (addresses.size() > 0) {
//                Address fetchedAddress = addresses.get(0);
//                StringBuilder strAddress = new StringBuilder();
//                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
//                    strAddress.append(fetchedAddress.getAddressLine(i)).append("");
//                }
//
//                placename.setText(strAddress.toString());
//                placename.setEnabled(false);
//                Toast.makeText(this, strAddress, Toast.LENGTH_SHORT).show();
//
//            } else {
//                placename.setEnabled(false);
//
//                placename.setHint("Enter your address");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "couldnt find", Toast.LENGTH_SHORT).show();
//        }
//    }

}

