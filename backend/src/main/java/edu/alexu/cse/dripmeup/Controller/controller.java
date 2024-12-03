package edu.alexu.cse.dripmeup.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {

    @CrossOrigin("http://localhost:8080")
    @GetMapping("/{name}")
    public  String test(@PathVariable String name){
        return "Hello " + name;
    }
}
