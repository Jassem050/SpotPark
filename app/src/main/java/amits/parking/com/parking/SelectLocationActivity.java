package amits.parking.com.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private static final String TAG = "SelectLocationActivity";
    private GoogleMap mMap;
    double lat;
    double lng;
    LatLng myLocation;
    RequestQueue MyRequestQueue;
    private utility utility;
    private SessionManager sessionManager;
    private Marker addedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        sessionManager = new SessionManager(this);
        utility = new utility(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MyRequestQueue = Volley.newRequestQueue(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Add a addedMarker in Sydney and move the camera
        LatLng myLoc = new LatLng(12.8708975, 74.8393176);
        addedMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        addedMarker.setDraggable(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15.0f));

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

//        mMap.setOnMapLongClickListener(this);

        lat = addedMarker.getPosition().latitude;
        lng = addedMarker.getPosition().longitude;
        Log.d(TAG, "onMapReady: lat: " + String.valueOf(lat));
        Log.d(TAG, "onMapReady: lng: " + String.valueOf(lng));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }


    @Override
    public void onMarkerDragEnd(Marker marker) {
        lat = marker.getPosition().latitude;
        lng = marker.getPosition().longitude;
        Log.d(TAG, "onMarkerDragEnd: latlng" + String.valueOf(lat) + ":" + String.valueOf(lng));
        Toast.makeText(this, String.valueOf(marker.getPosition().latitude), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, String.valueOf(marker.getPosition().latitude), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addedMarker.setPosition(latLng);
        lat = latLng.latitude;
        lng = latLng.longitude;
        Toast.makeText(this, String.valueOf(lat), Toast.LENGTH_SHORT).show();
    }

    public void propreg(View view) {


        String url = utility.ip + "property_reg.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("registration failed")) {
                            Toast.makeText(SelectLocationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        } else {


                            Toast.makeText(SelectLocationActivity.this, response, Toast.LENGTH_SHORT).show();
                            Intent intentent = new Intent(SelectLocationActivity.this, MainActivity.class);
                            startActivity(intentent);
                            Log.d("Response", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SelectLocationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", sessionManager.getId());
                params.put("address", getIntent().getExtras().getString("address"));
                params.put("placename", getIntent().getExtras().getString("place"));
                params.put("slotscar", getIntent().getExtras().getString("carslots"));
                params.put("slotsbike", getIntent().getExtras().getString("bikeslots"));
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));
                params.put("cswidth", getIntent().getExtras().getString("carslots_width"));
                params.put("csheight", getIntent().getExtras().getString("carslots_height"));
                params.put("bswidth", getIntent().getExtras().getString("bikeslots_width"));
                params.put("bsheight", getIntent().getExtras().getString("bikeslots_height"));
                params.put("crate", getIntent().getExtras().getString("carslots_price"));
                params.put("brate", getIntent().getExtras().getString("bikeslots_price"));

                return params;
            }
        };
        MyRequestQueue.add(postRequest);
    }

}


