package ecom.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecom.test.request.limiter.RequestLimit;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/system")
@NoArgsConstructor
public class SystemController {

    @RequestLimit
    @GetMapping("/health")
    public void health() {
        log.debug("Requested health check");
    }
}
