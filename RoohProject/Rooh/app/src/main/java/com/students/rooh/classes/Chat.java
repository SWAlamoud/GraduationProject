package com.students.rooh.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Chat implements Parcelable {
    public String id, guardianId, doctorId, guardianName, doctorName;
    public String otherName;

    public Chat(){
    }

    public Chat(String guardianId, String doctorId, String guardianName, String doctorName) {
        this.guardianId = guardianId;
        this.doctorId = doctorId;
        this.guardianName = guardianName;
        this.doctorName = doctorName;
    }

    public static final Creator CREATOR = new Creator() {
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public Chat(Parcel in){
        this.id = in.readString();
        this.guardianId = in.readString();
        this.doctorId = in.readString();
        this.guardianName = in.readString();
        this.doctorName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.guardianId);
        parcel.writeString(this.doctorId);
        parcel.writeString(this.guardianName);
        parcel.writeString(this.doctorName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Chat{" + '\'' +
                "ID = '" + this.id + '\'' +
                "guardianId = '" + this.guardianId + '\'' +
                "doctorId = '" + this.doctorId + '\'' +
                '}';
    }

    public static final String COLLECTION_NAME = "CHAT";

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("guardianId", this.guardianId);
        map.put("doctorId", this.doctorId);
        map.put("guardianName", this.guardianName);
        map.put("doctorName", this.doctorName);

        return map;
    }

    public void documentToObject(DocumentSnapshot document){
        this.id = document.getId();
        this.guardianId = document.getString("guardianId");
        this.doctorId = document.getString("doctorId");
        this.guardianName = document.getString("guardianName");
        this.doctorName = document.getString("doctorName");
    }

    public void documentToObject(QueryDocumentSnapshot document){
        this.id = document.getId();
        this.guardianId = document.getString("guardianId");
        this.doctorId = document.getString("doctorId");
        this.guardianName = document.getString("guardianName");
        this.doctorName = document.getString("doctorName");
    }
}
