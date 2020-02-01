package amits.parking.com.parking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditProfileActivity";

    private ActionBar actionBar;
    private MaterialButton updateProfileButton;
    private TextInputEditText nameEditText, emailEditText, mobileEditText, addressEditText;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    private utility utility;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        utility = new utility(this);
        requestQueue = Volley.newRequestQueue(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        init();

        loadProfileDetails();
    }

    private void init(){
        sessionManager = new SessionManager(this);
        nameEditText = (TextInputEditText) findViewById(R.id.edit_name);
        emailEditText = (TextInputEditText) findViewById(R.id.edit_email);
        mobileEditText = (TextInputEditText) findViewById(R.id.edit_mobile);
        addressEditText = (TextInputEditText) findViewById(R.id.edit_address);
        updateProfileButton = (MaterialButton) findViewById(R.id.update_profile_btn);
        updateProfileButton.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_profile_btn:
                hideSoftKeyBoard();
                String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
                final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String mobile = mobileEditText.getText().toString();
                final String address = addressEditText.getText().toString();
                if (name.isEmpty() && email.isEmpty() && mobile.isEmpty() && address.isEmpty()){
                    Toast.makeText(this, "Enter all the Fields", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()){
                    Toast.makeText(this, "Enter the name", Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@")||!email.contains(".")) {
                    Toast.makeText(this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()){
                    Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show();
                } else if (mobile.isEmpty()){
                    Toast.makeText(this, "Enter the mobile number", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty()){
                    Toast.makeText(this, "Enter the address", Toast.LENGTH_SHORT).show();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            updateProfile();
                        }
                    }, 2000);
                }
                return;
        }
    }

    private void loadProfileDetails(){
        String ip = utility.ip + "profile.php?uid=" + sessionManager.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip  , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    nameEditText.setText(jsonObject.getString("name"));
                    emailEditText.setText(jsonObject.getString("email"));
                    mobileEditText.setText(jsonObject.getString("phone"));
                    addressEditText.setText(jsonObject.getString("address"));
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

    private void updateProfile(){
        String ip = utility.ip + "updateprofile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    Toast.makeText(EditProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("uid", sessionManager.getId());
                params.put("name", nameEditText.getText().toString());
                params.put("email", emailEditText.getText().toString());
                params.put("phone", mobileEditText.getText().toString());
                params.put("address", addressEditText.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void hideSoftKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
    }
}
