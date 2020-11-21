package com.example.demo.service;

import com.example.demo.mapper.CoursMapper;
import com.example.demo.model.Cours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CoursService {
    @Autowired
    CoursMapper coursMapper;

    public int addCours(Cours cours) {
        return coursMapper.addCours(cours);
    }

    public int delCours(Cours cours) {
        return coursMapper.delCours(cours);
    }

    public List<Cours> getCours(String id) {
        return coursMapper.getCours(id);
    }

}
