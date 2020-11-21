package com.example.demo.service;

import com.example.demo.mapper.GradeMapper;
import com.example.demo.model.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GradeService {
    @Autowired
    GradeMapper gradeMapper;

    public List<Grade> getGrade(String id) {
        return gradeMapper.getGrade(id);
    }
}
