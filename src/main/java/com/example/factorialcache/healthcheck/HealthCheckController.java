package com.example.factorialcache.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/probe/healthcheck")
    public String healthCheck() {
        return "OK";
    }
}
