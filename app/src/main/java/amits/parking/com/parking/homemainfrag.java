package amits.parking.com.parking;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Belal on 18/09/16.
 */


public class homemainfrag extends Fragment {
    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homemain, container, false);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Messaging");
    }


//    public void onClick(View v) {
//        if (v.getId()==R.id.button4){
//
//            rel.removeAllViewsInLayout();
//
//            Toast.makeText(getActivity(), whatsapp.getText().toString(), Toast.LENGTH_SHORT).show();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            FourFragment hello = new FourFragment();
//            fragmentTransaction.add(R.id.jaba, hello, "HELLO");
//            fragmentTransaction.commit();
//        }
//        if (v.getId()==R.id.button5){
//            rel.removeAllViewsInLayout();
//            Toast.makeText(getActivity(), messenger.getText().toString(), Toast.LENGTH_SHORT).show();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            ThreeFragment hello = new ThreeFragment();
//            fragmentTransaction.add(R.id.jaba, hello, "HELLO");
//            fragmentTransaction.commit();
//
//
//        }
//        if (v.getId()==R.id.button6){
//            Toast.makeText(getActivity(), gmail.getText().toString(), Toast.LENGTH_SHORT).show();
//            rel.removeAllViewsInLayout();
//
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            NineFragment hello = new NineFragment();
//            fragmentTransaction.add(R.id.jaba, hello, "HELLO");
//            fragmentTransaction.commit();
//        }

}
