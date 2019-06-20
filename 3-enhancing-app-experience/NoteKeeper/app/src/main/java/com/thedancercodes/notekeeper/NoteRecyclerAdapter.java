package com.thedancercodes.notekeeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater layoutInflater;

    // Constructor that accepts context as a parameter & assigns it to mContext field.
    public NoteRecyclerAdapter(Context context) {
        this.mContext = context;

        // To create views from a layout resource, use LayoutInflater class using the context.
        layoutInflater = LayoutInflater.from(mContext);
    }

    // Creates our ViewHolder instances. It also creates the views themselves
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    // Associates data with our views
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    // Indicate number of data items we have
    @Override
    public int getItemCount() {
        return 0;
    }

    // Holds information for individual views
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
