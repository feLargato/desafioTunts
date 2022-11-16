package com.desafio.controller;
import com.desafio.model.Student;

import java.util.ArrayList;
import java.util.List;

public class ClassRoom {

    private List<Student> classRoom;

    public ClassRoom() {

    }

    public List<Student> getClassRoom() {
        return classRoom;
    }

    public void buildClassRoom(List<List<Object>> response) {
        classRoom = new ArrayList<>();
        for(List row : response) {
            Student student = new Student(row.get(0), row.get(1), row.get(2), row.get(3),
                    row.get(4), row.get(5));
            this.classRoom.add(student);
        }
    }
}
