package com.students.rooh.classes;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements Parcelable {
    public String id, senderID, message;
    public Date date;

    public Message() {
    }

    public Message(String senderID, String message, Date date) {
        this.senderID = senderID;
        this.message = message;
        this.date = date;
    }

    public static final Creator CREATOR = new Creator() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public Message(Parcel in) {
        this.id = in.readString();
        this.senderID = in.readString();
        this.message = in.readString();
        this.date = (Date) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.senderID);
        parcel.writeString(this.message);
        parcel.writeSerializable(this.date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Message{" + '\'' +
                "ID = '" + this.id + '\'' +
                "Message = '" + this.message + '\'' +
                '}';
    }

    public static final String COLLECTION_NAME = "MESSAGE";

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("senderID", this.senderID);
        map.put("message", this.message);
        map.put("date", this.date);

        return map;
    }

    public void documentToObject(DocumentSnapshot document){
        this.id = document.getId();
        this.senderID = document.getString("senderID");
        this.message = document.getString("message");
        this.date = document.getDate("date");
    }

    public void documentToObject(QueryDocumentSnapshot document){
        this.id = document.getId();
        this.senderID = document.getString("senderID");
        this.message = document.getString("message");
        this.date = document.getDate("date");
    }
}
