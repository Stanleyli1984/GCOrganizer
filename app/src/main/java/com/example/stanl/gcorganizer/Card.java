package com.example.stanl.gcorganizer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stanl on 7/15/2016.
 */
public class Card implements Parcelable {
    public Integer _id;
    public Integer exp_month;
    public Integer exp_year;
    public String store_name;
    public String card_number;
    public String pin;
    public Integer store_id;
    public byte[] thumb;
    public String pic_path1;
    public String pic_path2;

    public Card(Parcel in) {
        super();
        readFromParcel(in);
    }

    public Card() {
        super();
    }


    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(store_name);
        dest.writeString(card_number);
    }

    private void readFromParcel(Parcel in) {
        _id = in.readInt();
        store_name = in.readString();
        card_number  = in.readString();
    }

    public int describeContents() {
        return 0;
    }

}
