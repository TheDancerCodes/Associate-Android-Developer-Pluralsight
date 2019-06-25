package com.thedancercodes.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CourseInfo> courses; // Field to hold a list of courses
    private final LayoutInflater layoutInflater;

    // Constructor that accepts context & list of courses as a parameter
    // & assigns the context to mContext field.
    public CourseRecyclerAdapter(Context context, List<CourseInfo> courses) {
        this.mContext = context;
        this.courses = courses;

        // To create views from a layout resource, use LayoutInflater class using the context.
        layoutInflater = LayoutInflater.from(mContext);
    }

    // Creates our ViewHolder instances. It also creates the views themselves
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        // Creating the View
        View itemView = layoutInflater.inflate(R.layout.item_course_list, viewGroup, false);

        // Return a new instance of ViewHolder class
        return new ViewHolder(itemView);
    }

    // Associates data for a desired position within a ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        // Get note that corresponds to a particular position
        CourseInfo course = courses.get(position);

        // Get each of our TextViews from the ViewHolder
        viewHolder.textCourse.setText(course.getTitle());
        viewHolder.currentPosition = position;

    }

    // Indicate number of data items we have
    @Override
    public int getItemCount() {
        return courses.size(); // return the size of the courses
    }

    // Holds information for individual views.
    // This ViewHolder is supposed to keep references to any views we are going to set at runtime
    // for each item.
    public class ViewHolder extends RecyclerView.ViewHolder {

        // We make these fields public so that our outer class (CourseRecyclerAdapter), can
        // reference these fields directly
        public final TextView textCourse;

        // Get ViewHolder current position each time its associated with a different set of data
        public int currentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get references to each of the TextViews within our layout
            textCourse = itemView.findViewById(R.id.text_course);

            // Associate a click event handler with the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Snack bar containing course name of selected course
                    Snackbar.make(v, courses.get(currentPosition).getTitle(),
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

}
