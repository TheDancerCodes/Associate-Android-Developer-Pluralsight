package com.thedancercodes.notekeeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<NoteInfo> notes; // Field to hold a list of notes
    private final LayoutInflater layoutInflater;

    // Constructor that accepts context & list of notes as a parameter
    // & assigns the context to mContext field.
    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        this.mContext = context;
        this.notes = notes;

        // To create views from a layout resource, use LayoutInflater class using the context.
        layoutInflater = LayoutInflater.from(mContext);
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

    }

    // Indicate number of data items we have
    @Override
    public int getItemCount() {
        return notes.size(); // return the size of the notes
    }

    // Holds information for individual views.
    // This ViewHolder is supposed to keep references to any views we are going to set at runtime
    // for each item.
    public class ViewHolder extends RecyclerView.ViewHolder {

        // We make these fields public so that our outer class (NoteRecyclerAdapter), can
        // reference these fields directly
        public final TextView textCourse;
        public final TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get references to each of the TextViews within our layout
            textCourse = itemView.findViewById(R.id.text_course);
            textTitle = itemView.findViewById(R.id.text_title);
        }
    }

}
