package com.hzn.lib;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;

/**
 * 过度动画选项
 */
public class BinTransitionOptions {

    private Activity activity;
    private View[] views;
    private ArrayList<ViewAttrs> attrs;

    public BinTransitionOptions(Activity activity, View[] views) {
        this.activity = activity;
        this.views = views;
    }


    public static BinTransitionOptions makeTransitionOptions(Activity activity, View... views) {
        return new BinTransitionOptions(activity, views);
    }

    public void update() {
        if (null == views)
            return;

        attrs = new ArrayList<>();
        for (View v : views) {
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            attrs.add(new ViewAttrs(
                    v.getId(),
                    location[0],
                    location[1],
                    v.getWidth(),
                    v.getHeight()
            ));
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public ArrayList<ViewAttrs> getAttrs() {
        return attrs;
    }

    public static class ViewAttrs implements Parcelable {
        public int id;
        public float startX;
        public float startY;
        public float width;
        public float height;

        public ViewAttrs(int id, float startX, float startY, float width, float height) {
            this.id = id;
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
        }

        // Parcelable
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeFloat(this.startX);
            dest.writeFloat(this.startY);
            dest.writeFloat(this.width);
            dest.writeFloat(this.height);
        }

        protected ViewAttrs(Parcel in) {
            this.id = in.readInt();
            this.startX = in.readFloat();
            this.startY = in.readFloat();
            this.width = in.readFloat();
            this.height = in.readFloat();
        }

        public static final Parcelable.Creator<ViewAttrs> CREATOR = new Parcelable.Creator<ViewAttrs>() {
            @Override
            public ViewAttrs createFromParcel(Parcel source) {
                return new ViewAttrs(source);
            }

            @Override
            public ViewAttrs[] newArray(int size) {
                return new ViewAttrs[size];
            }
        };
    }
}
