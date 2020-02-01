package amits.parking.com.parking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarSlotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarSlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarSlotFragment extends Fragment {
    private static final String TAG = "CarSlotFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String prop_id ,type;
    private RequestQueue requestQueue;
    private SessionManager sessionManager;
    String ip="http://192.168.31.172/plop/";

    private AppCompatTextView totalSlotsText, availableSlotsText, availableWidthText, availableHeightText;
    private AppCompatTextView bookedSlotsText, bookedSlotsWidth, bookedSlotsHeight;
    private MaterialButton bookSlotButton;
    private int bookedi, availablei, totali;
    private MaterialCardView bottomCardView;

    public CarSlotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarSlotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarSlotFragment newInstance(String param1, String param2) {
        CarSlotFragment fragment = new CarSlotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_car_slot, container, false);

        requestQueue = Volley.newRequestQueue(getContext());
        sessionManager = new SessionManager(getContext());
        bottomCardView = (MaterialCardView) rootView.findViewById(R.id.layout_bottom);
        prop_id = getActivity().getIntent().getStringExtra("prop_id");
        type = "Car";
        Log.d(TAG, "onCreateView: prop_id frag: " + prop_id);

        totalSlotsText = (AppCompatTextView) rootView.findViewById(R.id.total_slots);
        availableSlotsText = (AppCompatTextView) rootView.findViewById(R.id.available_slots);
        availableWidthText = (AppCompatTextView) rootView.findViewById(R.id.available_slots_width);
        availableHeightText = (AppCompatTextView) rootView.findViewById(R.id.available_slots_height);
        bookedSlotsText = (AppCompatTextView) rootView.findViewById(R.id.booked_slots);
        bookedSlotsWidth = (AppCompatTextView) rootView.findViewById(R.id.booked_slots_width);
        bookedSlotsHeight = (AppCompatTextView) rootView.findViewById(R.id.booked_slots_height);
        bookSlotButton = (MaterialButton) rootView.findViewById(R.id.book_slot_btn);

        bookSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick();
            }
        });

        load();
        return rootView;
    }

    public void load(){
        String url = ip+"view_slots.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        String[] s=response.split("/");
                        bookedSlotsText.setText(s[0].toString());
                        totalSlotsText.setText(s[1].toString());

                        bookedi= Integer.parseInt(s[0].toString());
                        totali= Integer.parseInt(s[1].toString());
                        availablei=totali-bookedi;
                        availableSlotsText.setText(String.valueOf(availablei));



                        if (bookedSlotsText.getText().equals("1")){
                            bookSlotButton.setVisibility(View.GONE);
//                            bottomCardView.setVisibility(View.VISIBLE);
                        } else {
                            bookSlotButton.setVisibility(View.VISIBLE);
                            bottomCardView.setVisibility(View.GONE);
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
        requestQueue.add(postRequest);
    }

    private void btnClick() {
        Log.d(TAG, "bookslot: uid: " + sessionManager.getId());
        if (availableSlotsText.getText().toString().equals("0")) {
            Toast.makeText(getActivity(), "sorry... No more slots available", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "bookslot: called");
            String url = ip + "bookslot.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
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
                                if (success.equals("success")) {
                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                    bookSlotButton.setEnabled(false);
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
                    params.put("id", prop_id);
                    params.put("type", type);
                    return params;
                }
            };
            requestQueue.add(postRequest);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
