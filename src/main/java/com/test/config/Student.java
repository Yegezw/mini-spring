package com.test.config;

public class Student {

    private int stuId;
    private String stuName;

    public Student() {
    }

    public Student(int stuId, String stuName) {
        this.stuId = stuId;
        this.stuName = stuName;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    @Override
    public String toString() {
        return String.format("Student{stuId = %d, stuName = %s}", stuId, stuName);
    }
}
