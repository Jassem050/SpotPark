package amits.parking.com.parking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

public class ViewSlotsActivity extends AppCompatActivity implements CarSlotFragment.OnFragmentInteractionListener,
        BikeSlotFragment.OnFragmentInteractionListener {

    private static final String TAG = "ViewSlotsActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CategoryAdapter categoryAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slots2);

        actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(true);

        categoryAdapter = new CategoryAdapter(this, getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(categoryAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
