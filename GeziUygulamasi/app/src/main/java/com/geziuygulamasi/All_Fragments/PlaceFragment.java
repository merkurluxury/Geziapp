package com.geziuygulamasi.All_Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.geziuygulamasi.All_Models.Category;
import com.geziuygulamasi.All_Models.Place;
import com.geziuygulamasi.All_Models.PlaceAdapter;
import com.geziuygulamasi.All_Utils.Fragment.RootFragment;
import com.geziuygulamasi.All_Utils.Functions;
import com.geziuygulamasi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceFragment extends RootFragment {
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Place> placeList;
    private PlaceAdapter placeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places, container, false);
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = requireArguments();
        Category category = (Category) bundle.getSerializable("category");
        activity = requireActivity();
        activity.setTitle(category.getCategoryText());
        Functions.showLoadingDialog(activity);

        placeList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference places = database.getReference("places");
        places.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                placeList.clear();
                Functions.cancelLoadingDialog();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Object categoryIdObject = dataSnapshot.child("categoryId").getValue();
                    Object contentObject = dataSnapshot.child("content").getValue();
                    Object idObject = dataSnapshot.child("id").getValue();
                    Object imageObject = dataSnapshot.child("image").getValue();
                    Object priceObject = dataSnapshot.child("price").getValue();
                    Object titleObject = dataSnapshot.child("title").getValue();
                    Object workingHourObject = dataSnapshot.child("workingHour").getValue();

                    if(categoryIdObject != null && contentObject != null && idObject != null && imageObject != null && priceObject != null && titleObject != null && workingHourObject != null){
                        int categoryId = Integer.parseInt(categoryIdObject.toString());
                        if(category.getCategoryId() == categoryId)
                            placeList.add(new Place(categoryId, Integer.parseInt(idObject.toString()), contentObject.toString(), imageObject.toString(), priceObject.toString(), titleObject.toString(), workingHourObject.toString()));
                    }
                }

                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initAdapter(){
        if(placeAdapter == null){
            placeAdapter = new PlaceAdapter(activity, placeList, Functions::openPlaceDetailsPage);
            recyclerView.setAdapter(placeAdapter);
        }
        else
            placeAdapter.notifyDataSetChanged();
    }
}