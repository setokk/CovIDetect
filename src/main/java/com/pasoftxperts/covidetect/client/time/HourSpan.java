/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents the hours at which a lecture of a course took place in a day.
 | Each HourSpan has a start hour, an end hour and a course that takes places at that time.
 |
 |
 | Design:
 | - A lecture's starting hour and ending hour should be between [8:00, 22:00] -> [START_HOUR_BOUND, END_HOUR_BOUND].
 | - A lecture's duration cannot be more than 3 hours.
 |
*/

package com.pasoftxperts.covidetect.client.time;

import com.pasoftxperts.covidetect.client.course.Course;

import java.io.Serializable;

public class HourSpan implements Serializable
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
            throw new IllegalArgumentException("lecture duration must not be greater than 3 or lower than 1 and " +
                    "hours must be between [8:00 - 22:00]");

        this.startHour = startHour;
        this.endHour = endHour;
        this.course = course;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public static boolean hourOutOfBounds(int hour)
    {
        return ((hour < START_HOUR_BOUND) || (hour > END_HOUR_BOUND));
    }
}