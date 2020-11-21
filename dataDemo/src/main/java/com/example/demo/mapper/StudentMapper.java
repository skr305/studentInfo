package com.example.demo.mapper;

import com.example.demo.model.Student;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface StudentMapper {
    Student loadStudentById(String id);
    Student loadStudentByName(String name);
    int updateStudent(Student student);
}
