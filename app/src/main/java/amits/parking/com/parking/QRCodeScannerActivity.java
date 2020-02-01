package amits.parking.com.parking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "QRCodeScannerActivity";

    private static final int MY_CAMERA_REQUEST_CODE = 999;
    private ZXingScannerView zXingScannerView;
    private ActionBar actionBar;
    private utility utility;
    private SessionManager sessionManager;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        sessionManager = new SessionManager(this);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        utility = new utility(this);
        zXingScannerView = (ZXingScannerView) findViewById(R.id.qr_code_scanner);
        setScannerProperties();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setScannerProperties() {
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.QR_CODE);
        zXingScannerView.setFormats(list);
        zXingScannerView.setAutoFocus(true);
        zXingScannerView.setLaserColor(R.color.colorAccent);
        zXingScannerView.setMaskColor(R.color.colorAccent);
        if (Build.MANUFACTURER.equalsIgnoreCase("HUAWEI"))
            zXingScannerView.setAspectTolerance(0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
                return;
            }
        }
        zXingScannerView.startCamera();
        zXingScannerView.setResultHandler(this);
    }

    @Override
    public void handleResult(Result rawResult) {
        if (rawResult != null){
            Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
            String[] results = rawResult.getText().split(",");
            getParkedStatus(results[0], results[1]);
            sessionManager.setBid(results[0]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    private void getParkedStatus(final String bid, final String uid){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, utility.ip + "park.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: park+: " + response);
                if (response.contains("success")){
                    Toast.makeText(QRCodeScannerActivity.this, "Gate is Opened", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScannerActivity.this);
                    builder.setMessage("Gate is Opened. You Can Park Your Vehicle Now");
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
                } else if (response.contains("parked")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScannerActivity.this);
                    builder.setMessage("Pay the Amount to Open the Gate");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(QRCodeScannerActivity.this, PaymentActivity.class));
                            finish();
                        }
                    });

                    AlertDialog dialog1 = builder.create();
                    dialog1.show();
                } else {

                    Toast.makeText(QRCodeScannerActivity.this, response, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScannerActivity.this);
                    builder.setMessage(response);
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
                    Log.d(TAG, "onResponse: park: " + response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("uid", uid);
                params.put("bid", bid);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
