package com.cts.mfrp.anvay.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cts.mfrp.anvay.entity.User;
import com.cts.mfrp.anvay.repository.UserRepository;
import com.cts.mfrp.anvay.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getStudentById(Long studentId) {
        return userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllStudents() {
        return userRepository.findAll();
    }

    @Override
    public User updateStudent(Long studentId, User user) {
        User existing = getStudentById(studentId);
        if (user.getFirstName() != null) existing.setFirstName(user.getFirstName());
        if (user.getLastName() != null) existing.setLastName(user.getLastName());
        existing.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existing);
    }
}
