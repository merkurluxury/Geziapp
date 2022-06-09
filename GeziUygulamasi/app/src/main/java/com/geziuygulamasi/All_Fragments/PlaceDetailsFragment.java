package com.geziuygulamasi.All_Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.geziuygulamasi.All_Models.Comment;
import com.geziuygulamasi.All_Models.CommentAdapter;
import com.geziuygulamasi.All_Models.Place;
import com.geziuygulamasi.All_Utils.Fragment.RootFragment;
import com.geziuygulamasi.All_Utils.Functions;
import com.geziuygulamasi.All_Utils.Variables;
import com.geziuygulamasi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.techery.properratingbar.ProperRatingBar;

public class PlaceDetailsFragment extends RootFragment {
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Comment> allCommentList, commentList;
    private CommentAdapter commentAdapter;
    private TextView placeAverage;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3;
    private ValueEventListener valueEventListener, valueEventListener2, valueEventListener3;
    private int placeId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_place_details, container, false);
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = requireArguments();
        Place place = (Place) bundle.getSerializable("place");
        activity = requireActivity();
        activity.setTitle(place.getTitle());
        allCommentList = new ArrayList<>();
        commentList = new ArrayList<>();
        Functions.showLoadingDialog(activity);
        placeId = place.getId();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("comments");
        databaseReference2 = databaseReference.child(Variables.userEmailFirebase + "_" + placeId);
        databaseReference3 = databaseReference2;

        RoundedImageView placeImage = view.findViewById(R.id.placeImage);
        TextView placePrice = view.findViewById(R.id.placePrice);
        placeAverage = view.findViewById(R.id.placeAverage);
        TextView placeWorkingHour = view.findViewById(R.id.placeWorkingHour);
        TextView placeContent = view.findViewById(R.id.placeContent);
        ProperRatingBar properRatingBar = view.findViewById(R.id.properRatingBar);
        getRating(rate -> {
            properRatingBar.setVisibility(View.VISIBLE);
            properRatingBar.setRating(rate);
            properRatingBar.setListener(properRatingBar1 -> sendRating(properRatingBar1.getRating()));
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true));

        Glide.with(activity).load(place.getImage()).into(placeImage);
        placePrice.setText(getString(R.string.place_price, place.getPrice()));
        placeWorkingHour.setText(getString(R.string.working_hour, place.getWorkingHour()));
        placeContent.setText(place.getContent());

        getFirebaseData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initAdapter(){
        hashAverage();
        if(commentAdapter == null){
            commentAdapter = new CommentAdapter(activity, commentList, comment -> {
                if(activity.getString(R.string.add_comment).equals(comment)){
                    Toast.makeText(activity, activity.getString(R.string.please_change_comment), Toast.LENGTH_LONG).show();
                    return;
                }
               getRating(rate -> sendComment(comment, rate));
            });
            recyclerView.setAdapter(commentAdapter);
        }
        else
            commentAdapter.notifyDataSetChanged();
    }

    private void getRating(RateListener rateListener){
        valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference2.removeEventListener(valueEventListener2);
                Object rateObject = snapshot.child("rate").getValue();
                rateListener.onRate(rateObject == null ? 0 : Integer.parseInt(rateObject.toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference2.addValueEventListener(valueEventListener2);
    }

    private void sendComment(String comment, double rate){
        Functions.showLoadingDialog(activity);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("authorName", Variables.userName);
        hashMap.put("comment", comment);
        hashMap.put("placeId", placeId);
        hashMap.put("rate", rate);
        databaseReference2.setValue(hashMap).addOnCompleteListener(task -> {
            Functions.hideSoftKeyboard((AppCompatActivity) activity);
            Toast.makeText(activity, activity.getString(R.string.success), Toast.LENGTH_LONG).show();
            getFirebaseData();
        });
    }

    private void sendRating(float rating){
        valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference3.removeEventListener(valueEventListener3);
                Object commentObject = snapshot.child("comment").getValue();
                sendComment(commentObject == null ? "" : commentObject.toString(), rating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference3.addValueEventListener(valueEventListener3);
    }

    private void getFirebaseData(){
        if(valueEventListener != null) databaseReference.removeEventListener(valueEventListener);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allCommentList.clear();
                commentList.clear();
                Functions.cancelLoadingDialog();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Object commentObject = dataSnapshot.child("comment").getValue();
                    Object placeIdObject = dataSnapshot.child("placeId").getValue();
                    Object rateObject = dataSnapshot.child("rate").getValue();
                    Object authorNameObject = dataSnapshot.child("authorName").getValue();

                    if(commentObject != null && placeIdObject != null && rateObject != null && authorNameObject != null){
                        String commentString = commentObject.toString().trim();
                        if(placeId == Integer.parseInt(placeIdObject.toString())){
                            Comment comment = new Comment(commentString, authorNameObject.toString(), Integer.parseInt(rateObject.toString()), placeId, false);
                            if(!commentString.equals(""))
                                commentList.add(comment);
                            allCommentList.add(comment);
                        }
                    }
                }
                commentList.add(new Comment(activity.getString(R.string.add_comment), Variables.userName, 0, placeId,true));

                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void hashAverage(){
        int totalRate = 0;
        int totalUser = 0;
        for (Comment comment : allCommentList) {
            if(comment.getRate() > 0){
                totalUser++;
                totalRate += comment.getRate();
            }
        }

        if(totalUser > 0){
            placeAverage.setVisibility(View.VISIBLE);
            placeAverage.setText(activity.getString(R.string.average_rate, String.valueOf(totalRate / totalUser)));
        }
        else
            placeAverage.setVisibility(View.INVISIBLE);
    }

    private interface RateListener {
        void onRate(int rate);
    }
}