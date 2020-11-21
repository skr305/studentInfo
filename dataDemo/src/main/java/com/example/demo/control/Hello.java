package com.example.demo.control;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin!";
    }

    @GetMapping("/admin/db/hello")
    public String admin2() {
        return "/admin/db/hello";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user!";
    }

    @GetMapping("/db/hello")
    public String dba() {
        return "hello dba!";
    }


    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "hello2";
    }

    @GetMapping("/hello3")
    public String hello3() {
        return "dba";
    }
}