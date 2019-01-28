package uk.co.bluebrickstudios.ppmprov2;

import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;

public class MyEventDay extends EventDay implements Parcelable {

    private String mNote;
    private int mInspectionID;

    MyEventDay(Calendar day, int imageResource, String note, int inspection_id) {
        super(day, imageResource);
        mNote = note;
        mInspectionID = inspection_id;
    }
    String getNote() {
        return mNote;
    }
    int getInspectionID() {
        return mInspectionID;
    }
    private MyEventDay(Parcel in) {
        super((Calendar) in.readSerializable(), in.readInt());
        mNote = in.readString();
    }
    public static final Creator<MyEventDay> CREATOR = new Creator<MyEventDay>() {
        @Override
        public MyEventDay createFromParcel(Parcel in) {
            return new MyEventDay(in);
        }
        @Override
        public MyEventDay[] newArray(int size) {
            return new MyEventDay[size];
        }
    };
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(getCalendar());
        parcel.writeInt(getImageResource());
        parcel.writeString(mNote);
        parcel.writeInt(mInspectionID);
    }
    @Override
    public int describeContents() {
        return 0;
    }
}
