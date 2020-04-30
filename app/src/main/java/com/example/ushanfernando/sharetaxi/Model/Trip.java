package com.example.ushanfernando.sharetaxi.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Trip implements Parcelable {

    private int mId;
    private String mType;
    private String mStart;
    private String mDestination;
    private String mNamePosted;
    private int mIdPosted;
    private String mTime;
    private String mDate;
    private String mTimePosted;
    private LatLng mStartLatLng;

    protected Trip(Parcel in) {
        mId = in.readInt();
        mType = in.readString();
        mStart = in.readString();
        mDestination = in.readString();
        mNamePosted = in.readString();
        mIdPosted = in.readInt();
        mTime = in.readString();
        mDate = in.readString();
        mTimePosted = in.readString();
        mStartLatLng = in.readParcelable(LatLng.class.getClassLoader());
    }

   public Trip(){


    }
    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public LatLng getStartLatLng() {
        return mStartLatLng;
    }

    public void setStartLatLng(LatLng startLatLng) {
        mStartLatLng = startLatLng;
    }

    public String getTimePosted() {
        return mTimePosted;
    }

    public void setTimePosted(String timePosted) {
        mTimePosted = timePosted;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        mStart = start;

    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public String getNamePosted() {
        return mNamePosted;
    }

    public void setNamePosted(String namePosted) {
        mNamePosted = namePosted;
    }

    public int getIdPosted() {
        return mIdPosted;
    }

    public void setIdPosted(int idPosted) {
        mIdPosted = idPosted;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mType);
        dest.writeString(mStart);
        dest.writeString(mDestination);
        dest.writeString(mNamePosted);
        dest.writeInt(mIdPosted);
        dest.writeString(mTime);
        dest.writeString(mDate);
        dest.writeString(mTimePosted);
        dest.writeParcelable(mStartLatLng, flags);
    }



    @Override
    public String toString() {
        return "Trip{" +
                "mId=" + mId +
                ", mType='" + mType + '\'' +
                ", mStart='" + mStart + '\'' +
                ", mDestination='" + mDestination + '\'' +
                ", mNamePosted='" + mNamePosted + '\'' +
                ", mIdPosted=" + mIdPosted +
                ", mTime='" + mTime + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mTimePosted='" + mTimePosted + '\'' +
                '}';
    }


}
