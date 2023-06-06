package com.judy.customdummy.controller;

import com.judy.customdummy.model.DummyRequestVO;
import com.judy.customdummy.service.DummyService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy/api")
public class DummyApiController {

    private final DummyService dummyService;

    @PostMapping("/table")
    public ResponseEntity table() {
//    public ResponseEntity table(@RequestBody DummyRequestVO dummyRequestVO) {
        return ResponseEntity.ok(new DummyRequestVO());
    }

    @PostMapping(value = "/make", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity make(@RequestBody DummyRequestVO dummyRequestVO) throws IOException {
        return dummyService.downloadDummyCSV(dummyRequestVO);
    }

}
