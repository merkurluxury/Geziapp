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

import com.geziuygulamasi.All_Activities.MainActivity;
import com.geziuygulamasi.All_Models.Category;
import com.geziuygulamasi.All_Models.CategoryAdapter;
import com.geziuygulamasi.All_Utils.Fragment.RootFragment;
import com.geziuygulamasi.All_Utils.Functions;
import com.geziuygulamasi.All_Utils.Variables;
import com.geziuygulamasi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryFragment extends RootFragment {
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Category> categoryList;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        initView();
        return view;
    }

    private void initView() {
        activity = getActivity();
        Functions.showLoadingDialog(activity);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        categoryList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categories = database.getReference("categories");
        categories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                Functions.cancelLoadingDialog();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Object categoryId = dataSnapshot.child("id").getValue();
                    Object categoryImage = dataSnapshot.child("image").getValue();
                    Object categoryText = dataSnapshot.child("text").getValue();

                    if(categoryId != null && categoryImage != null && categoryText != null)
                        categoryList.add(new Category(Integer.parseInt(categoryId.toString()), categoryImage.toString(), categoryText.toString()));
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
        if(categoryAdapter == null){
            categoryAdapter = new CategoryAdapter(activity, categoryList, this::openPlacePage);
            recyclerView.setAdapter(categoryAdapter);
        }
        else
            categoryAdapter.notifyDataSetChanged();
    }

    private void openPlacePage(Category category){
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        PlaceFragment placeFragment = new PlaceFragment();
        placeFragment.setArguments(bundle);
        MainActivity.fragmentManager.beginTransaction().add(R.id.container, placeFragment).addToBackStack("place_" + category.getCategoryId()).commit();
        Variables.selectedCategory = category.getCategoryText();
    }
}