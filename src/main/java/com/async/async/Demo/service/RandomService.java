package com.async.async.Demo.service;

import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.IOException;

public interface RandomService {
    String generatePreSignedUrlDownloadUrl() throws IOException, InterruptedException;
    String generatePreSignedUploadUrl();
    @Async
    void exportFileToExcel(String preSignedUrl) throws IOException, InterruptedException;
    void uploadFileToAWSS3(String preSignedUrl, File file) throws IOException;

}
