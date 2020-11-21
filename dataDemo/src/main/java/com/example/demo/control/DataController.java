package com.example.demo.control;

import com.example.demo.Result;
import com.example.demo.model.Cours;
import com.example.demo.model.Grade;
import com.example.demo.model.Student;
import com.example.demo.service.CoursService;
import com.example.demo.service.GradeService;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DataController {
    @Autowired
    StudentService studentService;
    @Autowired
    CoursService coursService;
    @Autowired
    GradeService gradeService;

    @PostMapping("/loadStudentById")
    @ResponseBody
    @CrossOrigin
    public Student findStudentById(@RequestBody Student reqStu) {
        Student repStu = studentService.loadStudentById(reqStu.getId());
        return repStu;
    }

    @PostMapping("/updateStudent")
    @ResponseBody
    @CrossOrigin
    public int updateStudent(@RequestBody Student reqStu) {
        return studentService.updateStudent(reqStu);
    }

    @PostMapping("/getCours")
    @ResponseBody
    @CrossOrigin
    public int[] getCours(@RequestBody String id) {
        List<Cours> list = coursService.getCours(id);
        int[] result = new int[list.size()];

        for(int i=0; i<list.size(); i++) {
            result[i] = Integer.parseInt(list.get(i).cours);
        }

        return result;
    }


    @PostMapping("/delCours")
    @ResponseBody
    @CrossOrigin
    public int delCours(@RequestBody Cours cours) {
        System.out.println(cours.cours + "  " + cours.id);
        return coursService.delCours(cours);
    }

    @PostMapping("/addCours")
    @ResponseBody
    @CrossOrigin
    public int addCours(@RequestBody Cours cours) {
        return coursService.addCours(cours);
    }

    @PostMapping("/getGrade")
    @ResponseBody
    @CrossOrigin
    public List<Grade> getGrade(@RequestBody String id) {
        return gradeService.getGrade(id);
    }
}
