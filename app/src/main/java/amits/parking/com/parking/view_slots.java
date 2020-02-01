package amits.parking.com.parking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class view_slots extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "view_slots";
    
    String prop_id,type;
    RequestQueue MyRequestQueue;
    utility utility;
    String[] plants;
    TextView booked,available,total;
    int bookedi,availablei,totali;
    Button button;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slots);
        sessionManager = new SessionManager(this);
        utility=new utility(this);
        booked=(TextView)findViewById(R.id.booked) ;
        available=(TextView)findViewById(R.id.free) ;
        total=(TextView)findViewById(R.id.total) ;
        button=(Button)findViewById(R.id.book);





                MyRequestQueue = Volley.newRequestQueue(this);



        final CharSequence colors[] = new CharSequence[] {"Car", "Bike"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your vehicle type");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                type=colors[which].toString();
                load();

            }
        });
        builder.show();

        prop_id=getIntent().getStringExtra("prop_id");

        Log.d(TAG, "onCreate: prop_id: " + prop_id);

    }
    public void load(){
        String url = utility.ip+"view_slots.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(view_slots.this, response, Toast.LENGTH_SHORT).show();
                        String[] s=response.split("/");
                        booked.setText(s[0].toString());
                        total.setText(s[1].toString());

                        bookedi= Integer.parseInt(s[0].toString());
                        totali= Integer.parseInt(s[1].toString());
                        availablei=totali-bookedi;
                        available.setText(String.valueOf(availablei));



                        if (booked.getText().equals("1")){
                            button.setVisibility(View.GONE);
                        } else {
                            button.setVisibility(View.VISIBLE);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getId());
                params.put("id",prop_id);
                params.put("type", type);
                return params;
            }
        };
        MyRequestQueue.add(postRequest);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,Main.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void bookslot(View view){
        Log.d(TAG, "bookslot: uid: " + sessionManager.getId());
        if (available.getText().toString().equals("0")){
            Toast.makeText(this, "sorry... No more slots available", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d(TAG, "bookslot: called");
            String url = utility.ip+"bookslot.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: book: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String bid = jsonObject.getString("bid");
                                sessionManager.setBid(bid);
                                Log.d(TAG, "onResponse: bid: " + sessionManager.getBid());
                                String success = jsonObject.getString("success");
                                if (success.equals("success")){
                                    Toast.makeText(view_slots.this, response, Toast.LENGTH_SHORT).show();
                                    button.setEnabled(false);
                                    load();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", sessionManager.getId());
                    params.put("id",prop_id);
                    params.put("type", type);
                    return params;
                }
            };
            MyRequestQueue.add(postRequest);
        }



    }

}
