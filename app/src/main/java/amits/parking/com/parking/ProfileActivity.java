package amits.parking.com.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private SessionManager sessionManager;
    private MaterialButton logoutButton;
    private AppCompatTextView profileName, profileEmail, profileContact, profileAddress;

    private utility utility;
    private RequestQueue requestQueue;
    private MaterialButton editProfileButton;
    private MaterialCardView myBookingCardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sessionManager = new SessionManager(this);
        utility = new utility(this);
        requestQueue = Volley.newRequestQueue(this);
        logoutButton = (MaterialButton) findViewById(R.id.logout_btn);
        profileName = (AppCompatTextView) findViewById(R.id.profile_name);
        profileEmail = (AppCompatTextView) findViewById(R.id.profile_email);
        profileContact = (AppCompatTextView) findViewById(R.id.profile_mobile);
        profileAddress = (AppCompatTextView) findViewById(R.id.profile_address);
        editProfileButton = (MaterialButton) findViewById(R.id.edit_profile_btn);
        myBookingCardview = (MaterialCardView) findViewById(R.id.btn_current_bookings);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setLogin(false);
                Main.main.finish();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });


        myBookingCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CurrentBookingActivity.class));
            }
        });

    }

    private void loadProfileDetails(){
        String ip = utility.ip + "profile.php?uid=" + sessionManager.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip  , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    profileName.setText(jsonObject.getString("name"));
                    profileEmail.setText(jsonObject.getString("email"));
                    profileContact.setText(jsonObject.getString("phone"));
                    profileAddress.setText(jsonObject.getString("address"));
                    Log.d(TAG, "onResponse: name: " + jsonObject.getString("phone"));
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

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileDetails();
    }
}
