package com.example.factorialcache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class FactorialCacheController {

    @Value("${factorial.language}")
    private String language;

    @Value("${factorial.api-key}")
    private String apiKey;

    private FactorialCacheService cacheService;
    private FactorialCalculateService calculateService;

    public FactorialCacheController(FactorialCacheService cacheService, FactorialCalculateService calculateService) {
        this.cacheService = cacheService;
        this.calculateService = calculateService;
    }

    @GetMapping("/factorial/{n}")
    public String calculateFactorial(
            @PathVariable("n") int n,
            @RequestParam(value ="key", required = false) String key
            ) {

        if (n>10) {
            if (!apiKey.equals(key)) {
                throw new IncorrectApiKeyException("To calculate factorials greater than 10, you must provide a valid API key");
            }
        }

        BigDecimal result;
        BigDecimal cachedResult = cacheService.cachedFactorial(n);

        if (cachedResult != null) {
            result = cachedResult;
        } else {
            result = calculateService.getCalculatedResult(n);
            cacheService.cacheFactorial(n, result);
        }

        return switch (language) {
            case "ko" -> "팩토리얼 " + n + " 은 " + result + " 입니다.";
            case "en" -> "The factorial of " + n + " is " + result + ".";
            default -> "Unsupported language";
        };
    }
}
