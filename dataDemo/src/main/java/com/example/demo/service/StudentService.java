package com.example.demo.service;

import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StudentService implements UserDetailsService {

    @Autowired
    StudentMapper studentMapper;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        System.out.println(name);
        Student student = studentMapper.loadStudentByName(name);
        if(student == null) {
            System.out.println("The student isn't exist");
        }
        return student;
    }

    public Student loadStudentById(String id) {
        return studentMapper.loadStudentById(id);
    }

//    public int upDateStudent(String id, String polit, String socia, String unit) {
//        Student student = new Student();
//        student.setId(id);
//        student.setUnit(unit);
//        student.setPolit(polit);
//        student.setSocia(socia);
//        return studentMapper.updateStudent(student);
//    }
    public int updateStudent(Student student) {
        System.out.println(student.getPolit());
        return studentMapper.updateStudent(student);
    }
}
