package com.example.softeng306_application.entity;

import android.os.Parcel;

public class Asian extends Category {

    // Non-argument constructor to enable auto conversion of the Firebase documents to Asian objects
    public Asian() {
        this.borderColour = "#BCE1B3";
        this.audioFileName = "asian";
        this.categoryType =  "ASIAN";
    }

    @Override
    public String getBorderColour() {
        return borderColour;
    }
    @Override
    public String getAudioFileName() {
        return audioFileName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    protected Asian(Parcel in) {
        borderColour = in.readString();
        audioFileName = in.readString();
        categoryType = in.readString();
    }
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Asian createFromParcel(Parcel in) {
            return new Asian(in);
        }

        @Override
        public Asian[] newArray(int size) {
            return new Asian[size];
        }
    };

}
