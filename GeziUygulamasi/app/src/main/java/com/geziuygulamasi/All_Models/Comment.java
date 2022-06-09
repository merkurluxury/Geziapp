package com.geziuygulamasi.All_Models;

public class Comment {
    private final String comment, authorName;
    private final int rate, placeId;
    private final boolean isEditableComment;

    public Comment(String comment, String authorName, int rate, int placeId, boolean isEditableComment) {
        this.comment = comment;
        this.authorName = authorName;
        this.rate = rate;
        this.placeId = placeId;
        this.isEditableComment = isEditableComment;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getRate() {
        return rate;
    }

    public int getPlaceId() {
        return placeId;
    }

    public boolean isEditableComment() {
        return isEditableComment;
    }
}
