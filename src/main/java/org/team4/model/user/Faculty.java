package org.team4.model.user;

import java.util.ArrayList;

public class Faculty extends User {
    private long facultyID;
    private ArrayList<String> courses;

    public Faculty(String email, String password, String name, String type, long facultyID, ArrayList<String> courses) {
        super(email, password, name, type);
        this.facultyID = facultyID;
        this.courses = courses;
    }


    public long getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(long facultyID) {
        this.facultyID = facultyID;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }


}
