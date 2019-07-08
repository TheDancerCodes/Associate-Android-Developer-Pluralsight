package com.thedancercodes.notekeeper;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private Cursor mCursor;
    private final LayoutInflater layoutInflater;

    // Constructor that accepts context & cursor as a parameter
    // & assigns the context to mContext field.
    public NoteRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

        // To create views from a layout resource, use LayoutInflater class using the context.
        layoutInflater = LayoutInflater.from(mContext);

        // To get the values from a Cursor, we need the positions of the columns of interest.
        populateColumnPositions();
    }

    private void populateColumnPositions() {

        // Check whether Cursor is null
        if (mCursor == null)
            return;

        // Else; Get column indexes from mCursor

    }

    // Change over to a new Cursor & close any existing Cursor associated with the Adapter
    public void changeCursor(Cursor cursor) {

        // Check for existing Cursor & close it
        if (mCursor != null)
            mCursor.close();

        // Assign received Cursor to mCursor field
        mCursor = cursor;

        // Order Columns by index
        populateColumnPositions();

        // Notify RecyclerView of data change using the Base Class Method
        notifyDataSetChanged();
    }

    // Creates our ViewHolder instances. It also creates the views themselves
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        // Creating the View
        View itemView = layoutInflater.inflate(R.layout.item_note_list, viewGroup, false);

        // Return a new instance of ViewHolder class
        return new ViewHolder(itemView);
    }

    // Associates data for a desired position within a ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        // Get note that corresponds to a particular position
        NoteInfo note = notes.get(position);

        // Get each of our TextViews from the ViewHolder
        viewHolder.textCourse.setText(note.getCourse().getTitle());
        viewHolder.textTitle.setText(note.getTitle());
        viewHolder.mId = note.getId(); // ID of note currently being displayed

    }

    // Indicate number of data items we have
    @Override
    public int getItemCount() {
        return notes.size(); // return the size of the notes
    }

    // Holds information for individual views currently displayed in the RecyclerView.
    // This ViewHolder is supposed to keep references to any views we are going to set at runtime
    // for each item.
    public class ViewHolder extends RecyclerView.ViewHolder {

        // We make these fields public so that our outer class (NoteRecyclerAdapter), can
        // reference these fields directly
        public final TextView textCourse;
        public final TextView textTitle;

        // Get ViewHolder current id each time its associated with a different set of data
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get references to each of the TextViews within our layout
            textCourse = itemView.findViewById(R.id.text_course);
            textTitle = itemView.findViewById(R.id.text_title);

            // Associate a click event handler with the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Show NoteActivity for whatever the current position is.
                    Intent intent = new Intent(mContext, NoteActivity.class);

                    // Set the Extra for the NotePosition
                    intent.putExtra(NoteActivity.NOTE_ID, mId);

                    // Take context, call startActivity & pass in intent
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
