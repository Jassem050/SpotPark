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

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    TextInputEditText username, password, type, regno, address, email, contactno, cpass;
    RequestQueue MyRequestQueue;
utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (TextInputEditText) findViewById(R.id.username);
        address = (TextInputEditText) findViewById(R.id.address);
        email = (TextInputEditText) findViewById(R.id.email);
        contactno = (TextInputEditText) findViewById(R.id.contact);
        password = (TextInputEditText) findViewById(R.id.password);
        cpass = (TextInputEditText) findViewById(R.id.cpass);
        MyRequestQueue = Volley.newRequestQueue(this);
        utility=new utility(this);

    }

    public void RegisterButton(View view) {
        if (username.getText().toString().equals("")) {
            username.setError("Enter user name");
        } else if (address.getText().toString().equals("")) {
            address.setError("Enter address");
        } else if (email.getText().toString().equals("")) {
            email.setError("Enter email");
        } else if (contactno.getText().toString().equals("")) {
            contactno.setError("Enter contact no");
        } else if (!password.getText().toString().equals(cpass.getText().toString())) {
            cpass.setError("password couldnt match");
        } else if (!email.getText().toString().contains("@")||!email.getText().toString().contains(".")) {
            email.setError("Enter a valid email id");
        }else {


            String url = utility.ip+"register.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("registration failed")){
                                Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }else if (response.contains("emailid or contact number already exist")) {
                                Toast.makeText(Register.this, "Email or mobile number exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                                Intent intentent = new Intent(Register.this, MainActivity.class);
                                startActivity(intentent);
                                Log.d("Response", response);
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Register.this, error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", username.getText().toString());
                    params.put("address", address.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("contact", contactno.getText().toString());
                    params.put("password", password.getText().toString());
                    params.put("cpass", cpass.getText().toString());

                    return params;
                }
            };
            MyRequestQueue.add(postRequest);
        }

    }
}




