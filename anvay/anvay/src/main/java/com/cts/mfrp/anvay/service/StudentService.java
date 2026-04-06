package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.User;

public interface StudentService {
    User getStudentById(Integer studentId);
    List<User> getAllStudents();
    User updateStudent(Integer studentId, User user);
}
