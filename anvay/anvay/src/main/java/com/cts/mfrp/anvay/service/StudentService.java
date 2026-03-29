package com.cts.mfrp.anvay.service;

import java.util.List;

import com.cts.mfrp.anvay.entity.User;

public interface StudentService {
    User getStudentById(Long studentId);
    List<User> getAllStudents();
    User updateStudent(Long studentId, User user);
}
