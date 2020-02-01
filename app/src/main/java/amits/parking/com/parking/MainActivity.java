package amits.parking.com.parking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextInputEditText username,password;
    RequestQueue MyRequestQueue;
    utility utility;
    private SessionManager sessionManager;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        mainActivity = this;
        Log.d(TAG, "onCreate: login called");
        if (sessionManager.isLoggedIn()){
            startActivity(new Intent(MainActivity.this, Main.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);
            username = (TextInputEditText) findViewById(R.id.username);
            password = (TextInputEditText) findViewById(R.id.password);
            MyRequestQueue = Volley.newRequestQueue(this);
            utility = new utility(this);
        }
    }
    public void userreg(View v){
        Intent inten=new Intent(MainActivity.this,Register.class);
        startActivity(inten);

    }

    public void forgotPass(View view){
        startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
    }
    public void login(View view) {
        if (username.getText().toString().equals(""))
            username.setError("Enter Username");
        else if (password.getText().toString().equals("")) {
            password.setError("Enter Password");
        }
        else if (!username.getText().toString().contains("@")||!username.getText().toString().contains(".")) {
            username.setError("Enter a valid email id");
        }else {


            String url = utility.ip+"login.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("login failed")){
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    utility.user_id=response;
                                    sessionManager.setLogin(true);
                                    sessionManager.setId(jsonObject.getString("uid"));
                                    Log.d(TAG, "onResponse: uid: " + jsonObject.getString("uid"));
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                    Intent intentent = new Intent(MainActivity.this, Main.class);
                                    username.setText("");
                                    password.setText("");
                                    startActivity(intentent);
                                    Log.d("Response", response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", username.getText().toString());
                    params.put("password", password.getText().toString());
                    return params;
                }
            };
            MyRequestQueue.add(postRequest);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: called");
    }
}

