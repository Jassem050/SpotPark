package amits.parking.com.parking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        View.OnClickListener {
    private static final String TAG = "Main";
    private GoogleMap mMap;
    utility utility;
    RequestQueue MyRequestQueue;
    String id;
    private SearchView searchView;
    private SessionManager sessionManager;
    public static Main main;
    private MaterialCardView addSlots, addedSlots, generateQrCode, scanQrCode, userProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        MainActivity.mainActivity.finish();
        main = this;
        sessionManager = new SessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyRequestQueue = Volley.newRequestQueue(this);
        utility = new utility(this);
        searchView = (SearchView) findViewById(R.id.search_view);
        addSlots = (MaterialCardView) findViewById(R.id.add_slots);
        addedSlots = (MaterialCardView) findViewById(R.id.added_slots);
        generateQrCode = (MaterialCardView) findViewById(R.id.qr_code_generate);
        scanQrCode = (MaterialCardView) findViewById(R.id.qr_code_scanner);
        userProfile = (MaterialCardView) findViewById(R.id.user_profile);
        addSlots.setOnClickListener(this);
        addedSlots.setOnClickListener(this);
        generateQrCode.setOnClickListener(this);
        scanQrCode.setOnClickListener(this);
        userProfile.setOnClickListener(this);
        searchView.setFocusable(false);
        searchView.clearFocus();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mMap.clear();
                load(s);
                Toast.makeText(Main.this, s, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//
//        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent intent = new Intent(Main.this, property_reg.class);
            startActivity(intent);


        } else if (id == R.id.nav_added_places) {

        } else if (id == R.id.nav_qr_code) {
            startActivity(new Intent(Main.this, GenerateQRCodeActivity.class));
        } else if (id == R.id.nav_qr_scanner) {
            startActivity(new Intent(Main.this, QRCodeScannerActivity.class));
        } else if (id == R.id.nav_logout){
            sessionManager.setLogin(false);
            startActivity(new Intent(Main.this, MainActivity.class));
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        load("");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);




    }
    public  void load(final String searchText){
        String url = utility.ip+"view.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: view: " + response);
                        try {
                            JSONArray data = new JSONArray(response);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                String[] lat = c.getString("lat").split(",");
                                String[] lng = c.getString("lng").split(",");
                                String[] id = c.getString("propid").split(",");

//                                a = new Album(utility.ip+"/images/"+img[0],id[0]);
//                                albumList.add(a);
//                                utility.image=img;
                                Double latd,lngd;
                                latd= Double.valueOf(lat[0].toString());
                                lngd= Double.valueOf(lng[0].toString());
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marks);
                                LatLng sydney = new LatLng(latd, lngd);
                                mMap.addMarker(new MarkerOptions().position(sydney).icon(icon).title("Parking place").snippet(id[0].toString()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));


                            }
//                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!searchText.equals("")) {
                    params.put("search", searchText);
                }
//                params.put("password", password.getText().toString());
                return params;
            }
        };
        MyRequestQueue.add(postRequest);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String s = marker.getSnippet();
        id=s;
        marker.setSnippet("");
        finish();
        Intent intent=new Intent(this,ViewSlotsActivity.class);
        intent.putExtra("prop_id",id);
        startActivity(intent);

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_slots:
                startActivity(new Intent(Main.this, property_reg.class));
                return;
            case R.id.added_slots:
                startActivity(new Intent(Main.this, AddedPlacesActivity.class));
                return;
            case R.id.qr_code_generate:
                startActivity(new Intent(Main.this, GenerateQRCodeActivity.class));
                return;
            case R.id.qr_code_scanner:
                startActivity(new Intent(Main.this, QRCodeScannerActivity.class));
                return;
            case R.id.user_profile:
                startActivity(new Intent(Main.this, ProfileActivity.class));
                return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
    }
}
