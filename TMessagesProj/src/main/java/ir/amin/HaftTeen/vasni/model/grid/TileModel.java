package ir.amin.HaftTeen.vasni.model.grid;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import ir.amin.HaftTeen.vasni.utils.grid.AsymmetricItem;

public class TileModel implements AsymmetricItem {

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<TileModel> CREATOR = new Parcelable.Creator<TileModel>() {
        @Override
        public TileModel createFromParcel(@NonNull Parcel in) {
            return new TileModel(in);
        }

        @Override
        @NonNull
        public TileModel[] newArray(int size) {
            return new TileModel[size];
        }
    };
    private int itemId;
    private int position;
    private String title;
    private int columnSpan;
    private int rowSpan;
    private int isFree;
    private int clickable;
    private String icon;
    private String background;
    private int eventId;
    private String event;
    private String eventData;

    public TileModel() {
        this(1, 1, 0);
    }

    public TileModel(int columnSpan, int rowSpan, int position) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
    }

    public TileModel(Parcel in) {
        readFromParcel(in);
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getClickable() {
        return clickable;
    }

    public void setClickable(int clickable) {
        this.clickable = clickable;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    @Override
    public int getColumnSpan() {
        return columnSpan;
    }

    public void setColumnSpan(int columnSpan) {
        this.columnSpan = columnSpan;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
    }
}
