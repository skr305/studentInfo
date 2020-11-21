package com.example.demo.mapper;

import com.example.demo.model.Grade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface  GradeMapper {
    List<Grade> getGrade(String id);
}
