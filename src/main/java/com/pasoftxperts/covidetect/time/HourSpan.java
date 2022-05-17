package com.pasoftxperts.covidetect.time;

import com.pasoftxperts.covidetect.course.Course;

public class HourSpan
{
    // Hours must be between [8:00 - 22:00].
    // Lecture duration must not be greater than 3 (1 <= duration <= 3).
    public static final int START_HOUR_BOUND = 8;
    public static final int END_HOUR_BOUND = 22;

    private int startHour;
    private int endHour;
    private Course course;

    public HourSpan(int startHour, int endHour, Course course)
    {
        int duration = endHour - startHour;

        if (((duration > 3) || (duration < 1))
                || hourOutOfBounds(startHour)
                || hourOutOfBounds(endHour))
            throw new IllegalArgumentException("lecture duration must not be greater than 3 and " +
                    "hours must be between [8:00 - 22:00]");

        this.startHour = startHour;
        this.endHour = endHour;
        this.course = course;
    }

    public int getStartHour() { return startHour; }

    public int getEndHour() { return endHour; }

    public Course getCourse() { return course; }

    public void setCourse(Course course) { this.course = course; }

    public static boolean hourOutOfBounds(int hour)
    {
        return ((hour < START_HOUR_BOUND) || (hour > END_HOUR_BOUND));
    }
}