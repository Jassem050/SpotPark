package amits.parking.com.parking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddedPlacesAdapter extends RecyclerView.Adapter<AddedPlacesAdapter.MyAddedPlacesViewHolder> {

    private Context context;
    private List<AddedPlaces> addedPlacesList;

    public static class MyAddedPlacesViewHolder extends RecyclerView.ViewHolder {
        private TextView addressText, placeText, carSlotsText, bikeSlotsText;

        public MyAddedPlacesViewHolder(View view){
            super(view);
            addressText = (TextView) view.findViewById(R.id.address_text);
            placeText = (TextView) view.findViewById(R.id.place_text);
            carSlotsText = (TextView) view.findViewById(R.id.car_slot_text);
            bikeSlotsText = (TextView) view.findViewById(R.id.bike_slot_text);
        }
    }

    public AddedPlacesAdapter(Context context, List<AddedPlaces> addedPlacesList){
        this.context = context;
        this.addedPlacesList = addedPlacesList;
    }

    @NonNull
    @Override
    public MyAddedPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.added_places_item, viewGroup, false);
        return new MyAddedPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAddedPlacesViewHolder myAddedPlacesViewHolder, int i) {
        AddedPlaces addedPlaces = addedPlacesList.get(i);

        myAddedPlacesViewHolder.addressText.setText(addedPlaces.getAddress());
        myAddedPlacesViewHolder.placeText.setText(addedPlaces.getPlace());
        myAddedPlacesViewHolder.carSlotsText.setText(addedPlaces.getCarSlots());
        myAddedPlacesViewHolder.bikeSlotsText.setText(addedPlaces.getBikeSlots());
    }

    @Override
    public int getItemCount() {
        return addedPlacesList.size();
    }


}
