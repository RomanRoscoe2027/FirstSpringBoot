package com.example;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/software-engineers")
public class SoftwareEngineerController {
    @GetMapping
    public List<SoftwareEngineer> getEngineers(){
        return List.of(
                new SoftwareEngineer(
                        1,
                        "Roman",
                        "Python, Java, C++, Arduino"
                ),
                new SoftwareEngineer(
                        3,
                        "Joey",
                        "Java, C++, Arduino, ESP32, STM32"
                )
        );
    }
}
