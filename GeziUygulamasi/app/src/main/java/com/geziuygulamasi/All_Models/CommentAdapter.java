package com.geziuygulamasi.All_Models;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.geziuygulamasi.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentAdapterHolder> {
    private final ArrayList<Comment> commentList;
    private final LayoutInflater inflater;
    private final CommentListener commentListener;

    public CommentAdapter(Context context, ArrayList<Comment> commentList, CommentListener commentListener) {
        this.commentList = commentList;
        inflater = (LayoutInflater.from(context));
        this.commentListener = commentListener;
    }

    @NonNull
    @Override
    public CommentAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapterHolder(inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapterHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.authorName.setText(comment.getAuthorName());
        holder.authorName.setVisibility(View.VISIBLE);
        holder.sendComment.setVisibility(View.GONE);
        if(!comment.isEditableComment()){
            holder.itemComment.setVisibility(View.VISIBLE);
            holder.itemCommentEdit.setVisibility(View.GONE);
            holder.itemComment.setText(comment.getComment());
            holder.itemCommentEdit.setText("");
        }
        else{
            holder.itemComment.setVisibility(View.GONE);
            holder.itemCommentEdit.setVisibility(View.VISIBLE);
            holder.itemComment.setText("");
            holder.itemCommentEdit.setText(comment.getComment());
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.itemCommentEdit.getLayoutParams();
            holder.itemCommentEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    holder.itemCommentEdit.setLayoutParams(layoutParams);
                }
            });
            holder.itemCommentEdit.setOnFocusChangeListener((v, hasFocus) -> {
                if(hasFocus){
                    holder.itemCommentEdit.setText("");
                    holder.authorName.setVisibility(View.GONE);
                    holder.sendComment.setVisibility(View.VISIBLE);
                }
            });
            holder.sendComment.setOnClickListener(v -> commentListener.onSendComment(holder.itemCommentEdit.getText().toString().trim()));
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class CommentAdapterHolder extends RecyclerView.ViewHolder {
        private final TextView itemComment;
        private final TextView authorName;
        private final EditText itemCommentEdit;
        private final ImageView sendComment;

        public CommentAdapterHolder(@NonNull View itemView) {
            super(itemView);
            itemComment = itemView.findViewById(R.id.itemComment);
            authorName = itemView.findViewById(R.id.authorName);
            itemCommentEdit = itemView.findViewById(R.id.itemCommentEdit);
            sendComment = itemView.findViewById(R.id.sendComment);
        }
    }

    public interface CommentListener {
        void onSendComment(String comment);
    }
}