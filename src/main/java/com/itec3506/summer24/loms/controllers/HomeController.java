package com.itec3506.summer24.loms.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @ResponseBody
    @GetMapping("/ping")
    public ResponseEntity<HashMap<String, Object>> pingRequest() {
        HashMap<String, Object> resp = new HashMap<>();

        resp.put("message", "Server is online!");
        resp.put("status", HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
