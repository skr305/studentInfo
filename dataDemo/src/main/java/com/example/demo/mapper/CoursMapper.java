package com.example.demo.mapper;

import com.example.demo.model.Cours;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CoursMapper {
    List<Cours> getCours(String id);
    int addCours(Cours cours);
    int delCours(Cours cours);
}
