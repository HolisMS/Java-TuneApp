package satella.app.servicemoto.fragment;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {
    private String title;
    private String author;
    private String description;
    private String desc_card;
    private String thumbnail;
    private String backdrop;
    private String link;

    public Info() {
    }

    public Info(String title, String author, String description, String desc_card, String thumbnail, String backdrop, String link) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.desc_card = desc_card;
        this.thumbnail = thumbnail;
        this.backdrop = backdrop;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getAuthor() {
        return author;
    }

    public String getDesc_card() {
        return desc_card;
    }

    public String getLink() {
        return link;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.description);
        dest.writeString(this.desc_card);
        dest.writeString(this.thumbnail);
        dest.writeString(this.backdrop);
        dest.writeString(this.link);
    }

    protected Info(Parcel in) {
        this.title = in.readString();
        this.author = in.readString();
        this.description = in.readString();
        this.desc_card = in.readString();
        this.thumbnail = in.readString();
        this.backdrop = in.readString();
        this.link = in.readString();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };
}
