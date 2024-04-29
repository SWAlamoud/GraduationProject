package com.students.rooh.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.students.rooh.classes.enums.RequestStatus;

import java.util.HashMap;
import java.util.Map;

public class Request implements Parcelable {
    public String id, guardianId, doctorId, guardianName;
    public RequestStatus status;

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public Request(){
    }

    public Request(Parcel in) {
        this.id = in.readString();
        this.guardianId = in.readString();
        this.doctorId = in.readString();
        this.guardianName = in.readString();
        this.status = RequestStatus.fromInteger(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.guardianId);
        parcel.writeString(this.doctorId);
        parcel.writeString(this.guardianName);
        parcel.writeInt(RequestStatus.toInteger(this.status));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "Request{" + '\'' +
                "id = '" + this.id + '\'' +
                "guardianId = '" + this.guardianId + '\'' +
                "doctorId = '" + this.doctorId + '\'' +
                "status = '" + this.status + '\'' +
                '}';
    }

    public static final String COLLECTION_NAME = "REQUEST";

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("guardianId", this.guardianId);
        map.put("doctorId", this.doctorId);
        map.put("guardianName", this.guardianName);
        map.put("status", RequestStatus.toInteger(this.status));

        return map;
    }

    public void documentToObject(DocumentSnapshot document){
        this.id = document.getId();
        this.guardianId = document.getString("guardianId");
        this.doctorId = document.getString("doctorId");
        this.guardianName = document.getString("guardianName");
        this.status = RequestStatus.fromInteger(document.getLong("status").intValue());
    }

    public void documentToObject(QueryDocumentSnapshot document){
        this.id = document.getId();
        this.guardianId = document.getString("guardianId");
        this.doctorId = document.getString("doctorId");
        this.guardianName = document.getString("guardianName");
        this.status = RequestStatus.fromInteger(document.getLong("status").intValue());
    }
}
