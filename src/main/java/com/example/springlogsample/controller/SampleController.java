package com.example.springlogsample.controller;

import com.example.springlogsample.aopsample.anno.Log;
import com.example.springlogsample.aopsample.MethodType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("hello")
    @Log(description = "hello get", methodType = MethodType.OTHER)
    public String hello(){
        return "hello";
    }
}
