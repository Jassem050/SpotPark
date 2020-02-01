package amits.parking.com.parking;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddedPlacesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<AddedPlaces> addedPlacesList;
    private AddedPlacesAdapter addedPlacesAdapter;
    private utility utility;
    private RequestQueue requestQueue;
    private SessionManager sessionManager;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_places);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Added Slots");
        init();

        loadAddedSlots();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.added_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        addedPlacesList = new ArrayList<>();
        addedPlacesAdapter = new AddedPlacesAdapter(this, addedPlacesList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(addedPlacesAdapter);
        utility = new utility(this);
        requestQueue = Volley.newRequestQueue(this);
        sessionManager = new SessionManager(this);
    }

    private void loadAddedSlots(){
        String ip = utility.ip + "myplace.php?uid=" + sessionManager.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        addedPlacesList.add(new AddedPlaces(jsonObject.getString("paddress"), jsonObject.getString("pname"),
                                jsonObject.getString("cslot"), jsonObject.getString("bslot")));
                    }

                    addedPlacesAdapter = new AddedPlacesAdapter(AddedPlacesActivity.this, addedPlacesList);
                    recyclerView.setAdapter(addedPlacesAdapter);
                    addedPlacesAdapter.notifyDataSetChanged();
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
}
