package amits.parking.com.parking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";

    private RequestQueue requestQueue;
    private SessionManager sessionManager;
    private utility utility;
    private TextInputEditText cardNumberInput, cvvInput, expiryInput;
    private MaterialButton payButton;
    private TextView parkHour, parkFare;
    private ActionBar actionBar;
    private String fare;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        sessionManager = new SessionManager(this);
        utility = new utility(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar); 

        cardNumberInput = (TextInputEditText) findViewById(R.id.card_number_input);
        cvvInput = (TextInputEditText) findViewById(R.id.card_cvv_input);
        expiryInput = (TextInputEditText) findViewById(R.id.card_expiry_input);
        payButton = (MaterialButton) findViewById(R.id.pay_btn);
        parkHour = (TextView) findViewById(R.id.park_hour);
        parkFare = (TextView) findViewById(R.id.park_fare);

        loadPaymentDetails();
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardNumberInput.getText().toString().equals("")){
                    Toast.makeText(PaymentActivity.this, "Enter the Card Number", Toast.LENGTH_SHORT).show();
                } else if (cvvInput.getText().toString().equals("")){
                    Toast.makeText(PaymentActivity.this, "Enter the Cvv Number", Toast.LENGTH_SHORT).show();
                } else if (expiryInput.getText().toString().equals("")){
                    Toast.makeText(PaymentActivity.this, "Enter the Expiry Date", Toast.LENGTH_SHORT).show();
                } else if (!expiryInput.getText().toString().contains("/")){
                    Toast.makeText(PaymentActivity.this, "Enter Expiry Date in Proper Format", Toast.LENGTH_SHORT).show();
                }else {
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                payAmount();
                            }
                        }, 2000);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void loadPaymentDetails(){
        String ip = utility.ip + "amount.php?uid="+sessionManager.getId()+"&bid="+sessionManager.getBid();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: pay: "+ response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    parkHour.setText(jsonObject.getString("time"));
                    parkFare.setText("\u20B9"+ " " + jsonObject.getString("amount"));
                    fare = jsonObject.getString("amount");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: pay: ",error );
            }
        });

        requestQueue.add(stringRequest);
    }
    
    private void payAmount(){
        String ip = utility.ip + "pay.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    progressBar.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                    builder.setMessage("Payment Successfull. Gate is Opened.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                    AlertDialog dialog1 = builder.create();
                    dialog1.show();
                } else {
                    Toast.makeText(PaymentActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
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
                params.put("bid", sessionManager.getBid());
                params.put("cardno", cardNumberInput.getText().toString());
                params.put("cvv", cvvInput.getText().toString());
                params.put("expiry", expiryInput.getText().toString());
                params.put("amount", fare);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
