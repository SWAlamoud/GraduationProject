package com.students.rooh.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.students.rooh.classes.enums.UserType;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {
    public String uid, firstName, lastName, email;
    public UserType userType;

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(){
    }

    public User(Parcel in) {
        this.uid = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.documentID = in.readString();
        this.userType = UserType.fromInteger(in.readInt());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.uid);
        parcel.writeString(this.firstName);
        parcel.writeString(this.lastName);
        parcel.writeString(this.email);
        parcel.writeString(this.documentID);
        parcel.writeInt(UserType.toInteger(this.userType));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" + '\'' +
                "uid = '" + this.uid + '\'' +
                "email = '" + this.email + '\'' +
                '}';
    }

    public String documentID;
    public static final String COLLECTION_NAME = "USER";

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("uid", this.uid);
        map.put("firstName", this.firstName);
        map.put("lastName", this.lastName);
        map.put("email", this.email);
        map.put("userType", UserType.toInteger(this.userType));

        return map;
    }

    public void documentToObject(DocumentSnapshot document){
        this.documentID = document.getId();
        this.uid = document.getString("uid");
        this.firstName = document.getString("firstName");
        this.lastName = document.getString("lastName");
        this.email = document.getString("email");
        this.userType = UserType.fromInteger(document.getLong("userType").intValue());
    }

    public void documentToObject(QueryDocumentSnapshot document){
        this.documentID = document.getId();
        this.uid = document.getString("uid");
        this.firstName = document.getString("firstName");
        this.lastName = document.getString("lastName");
        this.email = document.getString("email");
        this.userType = UserType.fromInteger(document.getLong("userType").intValue());
    }
}
