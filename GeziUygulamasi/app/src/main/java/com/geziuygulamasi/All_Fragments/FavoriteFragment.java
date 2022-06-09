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

import com.geziuygulamasi.All_Models.Comment;
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

public class FavoriteFragment extends RootFragment {
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Place> placeList;
    private ArrayList<Place> favoriteList;
    private ArrayList<Comment> commentList;
    private PlaceAdapter placeAdapter;
    private FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView();
        return view;
    }

    private void initView() {
        activity = requireActivity();
        activity.setTitle(getString(R.string.app_name));
        Functions.showLoadingDialog(activity);

        placeList = new ArrayList<>();
        favoriteList = new ArrayList<>();
        commentList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        database = FirebaseDatabase.getInstance();
        DatabaseReference comments = database.getReference("comments");
        comments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteList.clear();
                placeList.clear();
                commentList.clear();
                getPlaces();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Object placeIdObject = dataSnapshot.child("placeId").getValue();
                    Object rateObject = dataSnapshot.child("rate").getValue();

                    if(placeIdObject != null && rateObject != null){
                        int rate =  Integer.parseInt(rateObject.toString());
                        if(rate > 0)
                            commentList.add(new Comment("", "", rate, Integer.parseInt(placeIdObject.toString()),false));
                    }
                }

                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPlaces(){
        DatabaseReference places = database.getReference("places");
        places.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteList.clear();
                placeList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Object categoryIdObject = dataSnapshot.child("categoryId").getValue();
                    Object contentObject = dataSnapshot.child("content").getValue();
                    Object idObject = dataSnapshot.child("id").getValue();
                    Object imageObject = dataSnapshot.child("image").getValue();
                    Object priceObject = dataSnapshot.child("price").getValue();
                    Object titleObject = dataSnapshot.child("title").getValue();
                    Object workingHourObject = dataSnapshot.child("workingHour").getValue();

                    if(categoryIdObject != null && contentObject != null && idObject != null && imageObject != null && priceObject != null && titleObject != null && workingHourObject != null)
                        placeList.add(new Place(Integer.parseInt(categoryIdObject.toString()), Integer.parseInt(idObject.toString()), contentObject.toString(), imageObject.toString(), priceObject.toString(), titleObject.toString(), workingHourObject.toString()));
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
        if(placeList.size() == 0 || commentList.size() == 0) return;
        Functions.cancelLoadingDialog();
        for (Place place : placeList) {
            int totalUser = 0;
            int totalRate = 0;
            for(Comment comment : commentList){
                if(place.getId() == comment.getPlaceId()){
                    totalUser++;
                    totalRate += comment.getRate();
                }
            }

            if(totalUser > 0){
                int averageRate = totalRate / totalUser;
                if(averageRate >= 4)
                    favoriteList.add(place);
            }
        }
        if(placeAdapter == null){
            placeAdapter = new PlaceAdapter(activity, favoriteList, Functions::openPlaceDetailsPage);
            recyclerView.setAdapter(placeAdapter);
        }
        else
            placeAdapter.notifyDataSetChanged();
    }
}
