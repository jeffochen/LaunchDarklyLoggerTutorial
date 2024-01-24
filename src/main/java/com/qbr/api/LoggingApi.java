package com.qbr.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/logger")
public class LoggingApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{name}",
            produces = { "application/json" }
    )
    ResponseEntity<String> log(@PathVariable("name") String name) {
        log.debug("Request with name {}", name);
        return ResponseEntity.ok("Hello "+ name);
    }

}
