package com.async.async.Demo.controller;

import com.async.async.Demo.service.RandomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/random")
public class RandomController {

    private final RandomService randomService;

    @GetMapping("preSignedUrl")
    private ResponseEntity<?> getPreSignedUrl() throws IOException, InterruptedException {
        String url = randomService.generatePreSignedUrlDownloadUrl();
        String upload = randomService.generatePreSignedUploadUrl();
        randomService.exportFileToExcel(upload);
        return ResponseEntity.ok(url);
    }

}
