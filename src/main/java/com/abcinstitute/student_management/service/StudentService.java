package com.abcinstitute.student_management.service;

import com.abcinstitute.student_management.model.Student;
import com.abcinstitute.student_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // CREATE – Register new student
    public boolean registerStudent(Student student) {
        if (studentRepository.existsByUsername(student.getUsername())) {
            return false; // username already taken
        }
        studentRepository.save(student);
        return true;
    }

    // READ ALL – Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // READ ONE – Find by username
    public Optional<Student> findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    // Authenticate login
    public boolean authenticate(String username, String password) {
        return studentRepository
                .findByUsernameAndPassword(username, password)
                .isPresent();
    }

    // UPDATE – Update student
    public boolean updateStudent(Student updatedStudent) {
        Optional<Student> existing =
                studentRepository.findByUsername(updatedStudent.getUsername());
        if (existing.isPresent()) {
            Student s = existing.get();
            s.setEmail(updatedStudent.getEmail());
            s.setPhone(updatedStudent.getPhone());
            s.setFullName(updatedStudent.getFullName());
            if (updatedStudent.getPassword() != null
                    && !updatedStudent.getPassword().isEmpty()) {
                s.setPassword(updatedStudent.getPassword());
            }
            studentRepository.save(s);
            return true;
        }
        return false;
    }

    // DELETE – Remove student
    public boolean deleteStudent(String username) {
        Optional<Student> student =
                studentRepository.findByUsername(username);
        if (student.isPresent()) {
            studentRepository.delete(student.get());
            return true;
        }
        return false;
    }
}