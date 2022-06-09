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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterHolder> {
    private final Context context;
    private final ArrayList<Category> categoryList;
    private final CategoryListener categoryListener;
    private final LayoutInflater inflater;

    public CategoryAdapter(Context context, ArrayList<Category> categoryList, CategoryListener categoryListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryListener = categoryListener;
        inflater = (LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public CategoryAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryAdapterHolder(inflater.inflate(R.layout.item_category_and_places, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.itemText.setText(category.getCategoryText());
        Glide.with(context).load(category.getCategoryImage()).placeholder(R.color.mainColor).dontTransform().into(holder.itemImage);
        holder.mainLayout.setOnClickListener(v -> categoryListener.onClick(category));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class CategoryAdapterHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout mainLayout;
        private final RoundedImageView itemImage;
        private final TextView itemText;

        public CategoryAdapterHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemText = itemView.findViewById(R.id.itemText);
        }
    }

    public interface CategoryListener {
        void onClick(Category category);
    }
}