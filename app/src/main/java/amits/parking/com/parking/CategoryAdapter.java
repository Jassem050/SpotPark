package amits.parking.com.parking;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CategoryAdapter extends FragmentStatePagerAdapter {


    private Context context;
    public CategoryAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return CarSlotFragment.newInstance("car","car");
            case 1:
                return BikeSlotFragment.newInstance("bike","bike");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "CarSlots";
            case 1:
                return "BikeSlots";
        }
        return super.getPageTitle(position);
    }
}
