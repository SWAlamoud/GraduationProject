package com.students.rooh.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Child implements Parcelable {
    public String id, guardianId, name, healthStatus, otherGuardianName, otherGuardianPhone;
    public int birthMonth, birthYear;
    public boolean drowning;

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public Child(){
    }

    public Child(Parcel in) {
        this.id = in.readString();
        this.guardianId = in.readString();
        this.name = in.readString();
        this.healthStatus = in.readString();
        this.otherGuardianName = in.readString();
        this.otherGuardianPhone = in.readString();
        this.birthMonth = in.readInt();
        this.birthYear = in.readInt();
        this.drowning = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.guardianId);
        parcel.writeString(this.name);
        parcel.writeString(this.healthStatus);
        parcel.writeString(this.otherGuardianName);
        parcel.writeString(this.otherGuardianPhone);
        parcel.writeInt(this.birthMonth);
        parcel.writeInt(this.birthYear);
        parcel.writeInt(this.drowning ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "Child{" + '\'' +
                "id = '" + this.id + '\'' +
                "name = '" + this.name + '\'' +
                "drowning = '" + this.drowning + '\'' +
                '}';
    }

    public static final String COLLECTION_NAME = "CHILD";

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("guardianId", this.guardianId);
        map.put("name", this.name);
        map.put("healthStatus", this.healthStatus);
        map.put("otherGuardianName", this.otherGuardianName);
        map.put("otherGuardianPhone", this.otherGuardianPhone);
        map.put("birthMonth", this.birthMonth);
        map.put("birthYear", this.birthYear);
        map.put("drowning", this.drowning);

        return map;
    }

    public void documentToObject(DocumentSnapshot document){
        this.id = document.getId();
        this.guardianId = document.getString("guardianId");
        this.name = document.getString("name");
        this.healthStatus = document.getString("healthStatus");
        this.otherGuardianName = document.getString("otherGuardianName");
        this.otherGuardianPhone = document.getString("otherGuardianPhone");
        this.birthMonth = document.getLong("birthMonth").intValue();
        this.birthYear = document.getLong("birthYear").intValue();
        this.drowning = document.getBoolean("drowning");
    }

    public void documentToObject(QueryDocumentSnapshot document){
        this.id = document.getId();
        this.guardianId = document.getString("guardianId");
        this.name = document.getString("name");
        this.healthStatus = document.getString("healthStatus");
        this.otherGuardianName = document.getString("otherGuardianName");
        this.otherGuardianPhone = document.getString("otherGuardianPhone");
        this.birthMonth = document.getLong("birthMonth").intValue();
        this.birthYear = document.getLong("birthYear").intValue();
        this.drowning = document.getBoolean("drowning");
    }
}
