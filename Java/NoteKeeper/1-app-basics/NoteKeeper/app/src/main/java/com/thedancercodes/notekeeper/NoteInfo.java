package com.thedancercodes.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Take NoteInfo and pass it as an Extra in an Intent from one Activity to another.
 */
public final class NoteInfo implements Parcelable {
    private CourseInfo mCourse;
    private String mTitle;
    private String mText;

    public NoteInfo(CourseInfo course, String title, String text) {
        mCourse = course;
        mTitle = title;
        mText = text;
    }

    private NoteInfo(Parcel source) {

        // Read back values from Parcel
        mCourse = source.readParcelable(CourseInfo.class.getClassLoader());
        mTitle = source.readString();
        mText = source.readString();
    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    private String getCompareKey() {
        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    /**
     * Use this to populate the list items for each note.
     *
     * @return concatenated string of mCourse, mTitle & mText
     */
    @Override
    public String toString() {
        return getCompareKey();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // writeToParcel is responsible to write the member information for the type instance
        // into the parcel.

        // Course is a reference type & is going to have to be parcelable.
        dest.writeParcelable(mCourse, 0);

        // Writing members.
        dest.writeString(mTitle);
        dest.writeString(mText);
    }

    // Ensure that our content can be recreated from a Parcel.
    public static final Parcelable.Creator<NoteInfo> CREATOR =
            new Parcelable.Creator<NoteInfo>() {


                /**
                 * Create a new instance of the Parcelable class, instantiating it
                 * from the given Parcel whose data had previously been written by
                 * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
                 *
                 * @param source The Parcel to read the object's data from.
                 * @return Returns a new instance of the Parcelable class.
                 */
                @Override
                public NoteInfo createFromParcel(Parcel source) {

                    // Create a new instance of our type & add set all the values inside it

                    // NOTE: Whenever you are setting the values from createFromParcel(), they need
                    // to be the same order that you wrote the values in writeToParcel()

                    // This is because there's no real identifiers here, values are simply written in
                    // & read back out in order.
                    return new NoteInfo(source);
                }


                /**
                 * Create a new array of the Parcelable class.
                 *
                 * @param size Size of the array.
                 * @return Returns an array of the Parcelable class, with every entry
                 * initialized to null.
                 */
                @Override
                public NoteInfo[] newArray(int size) {
                    return new NoteInfo[size];
                }
            };

}
