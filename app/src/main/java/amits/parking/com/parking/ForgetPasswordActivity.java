package amits.parking.com.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText, confirmPasswordEditText;
    private MaterialButton updatePassButton;
    private SessionManager sessionManager;
    private utility utility;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        utility = new utility(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sessionManager = new SessionManager(this);
        requestQueue = Volley.newRequestQueue(this);
        emailEditText = (TextInputEditText) findViewById(R.id.email_input);
        passwordEditText = (TextInputEditText) findViewById(R.id.password_input);
        confirmPasswordEditText = (TextInputEditText) findViewById(R.id.confirm_password_input);
        updatePassButton = (MaterialButton) findViewById(R.id.update_password_btn);

        updatePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, "Enter the Email", Toast.LENGTH_SHORT).show();
                } else if (!emailEditText.getText().toString().contains("@") && !emailEditText.getText().toString().contains(".")){
                    Toast.makeText(ForgetPasswordActivity.this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                } else if (passwordEditText.getText().toString().equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, "Enter the Password", Toast.LENGTH_SHORT).show();
                } else if (confirmPasswordEditText.getText().toString().equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, "Enter the Password", Toast.LENGTH_SHORT).show();
                } else if (!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())){
                    Toast.makeText(ForgetPasswordActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updatePassword();
                        }
                    }, 2000);
                }
            }
        });
    }

    private void updatePassword(){
        String ip = utility.ip + "forgotpass.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    Toast.makeText(ForgetPasswordActivity.this, "Password has been Updated Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Failed to Update", Toast.LENGTH_SHORT).show();
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
                params.put("email", emailEditText.getText().toString());
                params.put("password", passwordEditText.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
