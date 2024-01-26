package com.hodik.performance.test.app.service;

import com.hodik.performance.test.app.config.Config;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {
    @PostMapping("/update/{num}")
    public void updateProp(@PathVariable("num") int num) {
        Config.REQUEST_AMOUNT = num;
    }
}
