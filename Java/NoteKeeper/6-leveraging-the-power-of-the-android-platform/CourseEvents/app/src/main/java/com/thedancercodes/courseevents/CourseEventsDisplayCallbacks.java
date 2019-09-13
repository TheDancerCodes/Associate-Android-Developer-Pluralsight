package com.thedancercodes.courseevents;

/**
 * The interface the Broadcast Receiver will use to pass information about the received Broadcast
 */
interface CourseEventsDisplayCallbacks {
    void onEventReceived(String courseId, String courseMessage);
}
