package com.geziuygulamasi.All_Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.geziuygulamasi.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceAdapterHolder> {
    private final Context context;
    private final ArrayList<Place> placeList;
    private final PlacesListener placesListener;
    private final LayoutInflater inflater;

    public PlaceAdapter(Context context, ArrayList<Place> placeList, PlacesListener placesListener) {
        this.context = context;
        this.placeList = placeList;
        this.placesListener = placesListener;
        inflater = (LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public PlaceAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceAdapterHolder(inflater.inflate(R.layout.item_category_and_places, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapterHolder holder, int position) {
        Place place = placeList.get(position);
        holder.itemText.setText(place.getTitle());
        Glide.with(context).load(place.getImage()).placeholder(R.color.mainColor).dontTransform().into(holder.itemImage);
        holder.mainLayout.setOnClickListener(v -> placesListener.onClick(place));
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class PlaceAdapterHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout mainLayout;
        private final RoundedImageView itemImage;
        private final TextView itemText;

        public PlaceAdapterHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemText = itemView.findViewById(R.id.itemText);
        }
    }

    public interface PlacesListener {
        void onClick(Place place);
    }
}