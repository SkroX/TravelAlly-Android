package com.github.skrox.travelally.data.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.linkedin.android.spyglass.mentions.Mentionable;

import org.jetbrains.annotations.NotNull;

/**
 * Model representing a person.
 */
public class UserMentionable implements Mentionable {
    public static final Parcelable.Creator<UserMentionable> CREATOR
            = new Parcelable.Creator<UserMentionable>() {
        public UserMentionable createFromParcel(Parcel in) {
            return new UserMentionable(in);
        }

        public UserMentionable[] newArray(int size) {
            return new UserMentionable[size];
        }
    };
    private final String id;
    private final String mFirstName;
    private final String mLastName;
    private final String mPictureURL;

    public UserMentionable(String id, String mFirstName, String mLastName, String mPictureURL) {
        this.id = id;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPictureURL = mPictureURL;
    }

    public UserMentionable(Parcel in) {
        id = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mPictureURL = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    // --------------------------------------------------
    // Mentionable/Suggestible Implementation
    // --------------------------------------------------

    public String getLastName() {
        return mLastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getPictureURL() {
        return mPictureURL;
    }

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return getFullName();
            case PARTIAL:
                String[] words = getFullName().split(" ");
                return (words.length > 1) ? words[0] : "";
            case NONE:
            default:
                return "";
        }
    }

    @NonNull
    @Override
    public MentionDeleteStyle getDeleteStyle() {
        // People support partial deletion
        // i.e. "John Doe" -> DEL -> "John" -> DEL -> ""
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return Integer.parseInt(getId());
    }

    @NotNull
    @Override
    public String getSuggestiblePrimaryText() {
        return getFullName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mPictureURL);
    }

    // --------------------------------------------------
    // PersonLoader Class (loads people from JSON file)
    // --------------------------------------------------

   /* public static class PersonLoader extends MentionsLoader<UserMentionable> {
        private static final String TAG = PersonLoader.class.getSimpleName();

        public PersonLoader(Resources res) {
            super(res, R.raw.people);
        }

        @Override
        public UserMentionable[] loadData(JSONArray arr) {
            UserMentionable[] data = new UserMentionable[arr.length()];
            try {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String first = obj.getString("first");
                    String last = obj.getString("last");
                    String url = obj.getString("picture");
                    data[i] = new UserMentionable(first, last, url);
                }
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception while parsing person JSONArray", e);
            }

            return data;
        }

        // Modified to return suggestions based on both first and last name
        @Override
        public List<UserMentionable> getSuggestions(QueryToken queryToken) {
            String[] namePrefixes = queryToken.getKeywords().toLowerCase().split(" ");
            List<UserMentionable> suggestions = new ArrayList<>();
            if (mData != null) {
                for (UserMentionable suggestion : mData) {
                    String firstName = suggestion.getFirstName().toLowerCase();
                    String lastName = suggestion.getLastName().toLowerCase();
                    if (namePrefixes.length == 2) {
                        if (firstName.startsWith(namePrefixes[0]) && lastName.startsWith(namePrefixes[1])) {
                            suggestions.add(suggestion);
                        }
                    } else {
                        if (firstName.startsWith(namePrefixes[0]) || lastName.startsWith(namePrefixes[0])) {
                            suggestions.add(suggestion);
                        }
                    }
                }
            }
            return suggestions;
        }
    }*/
}
