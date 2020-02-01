package amits.parking.com.parking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentBookingActivity extends AppCompatActivity {

    private static final String TAG = "CurrentBookingActivity";

    private AppCompatTextView address, place, vehicleType;
    private MaterialButton cancelBtn, directionBtn;
    private SessionManager sessionManager;
    private utility utility;
    private RequestQueue requestQueue;
    String lat, lng;
    private MaterialCardView bookingCardView;
    private AppCompatTextView noBooking;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("My Bookings");

        requestQueue = Volley.newRequestQueue(this);
        sessionManager = new SessionManager(this);
        utility = new utility(this);
        address = (AppCompatTextView) findViewById(R.id.address);
        place = (AppCompatTextView) findViewById(R.id.place);
        vehicleType = (AppCompatTextView) findViewById(R.id.vehicle_type);
        cancelBtn = (MaterialButton) findViewById(R.id.cancel_btn);
        directionBtn = (MaterialButton) findViewById(R.id.direction_btn);
        bookingCardView = (MaterialCardView) findViewById(R.id.booking_text);
        noBooking = (AppCompatTextView) findViewById(R.id.no_booking);
        bookingCardView.setVisibility(View.GONE);
        noBooking.setVisibility(View.VISIBLE);

        loadBookingDetails();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });

        directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                try{
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage() );
                    Toast.makeText(CurrentBookingActivity.this, "Couldn't open map", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void loadBookingDetails(){
        String ip = utility.ip + "mybooking.php?uid="+sessionManager.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    bookingCardView.setVisibility(View.VISIBLE);
                    noBooking.setVisibility(View.GONE);
                    address.setText(jsonObject.getString("paddress"));
                    place.setText(jsonObject.getString("pname"));
                    vehicleType.setText(jsonObject.getString("vtype"));
                    lat = jsonObject.getString("lat");
                    lng = jsonObject.getString("lng");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    private void cancelBooking(){
        String ip = utility.ip + "cancel.php?bid="+sessionManager.getBid();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    Toast.makeText(CurrentBookingActivity.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                    bookingCardView.setVisibility(View.GONE);
                    noBooking.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }
}
