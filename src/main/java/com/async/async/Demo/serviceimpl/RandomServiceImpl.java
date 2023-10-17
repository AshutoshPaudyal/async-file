package com.async.async.Demo.serviceimpl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.async.async.Demo.aws.BUCKETS;
import com.async.async.Demo.model.Random;
import com.async.async.Demo.repo.RandomRepo;
import com.async.async.Demo.service.RandomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RandomServiceImpl implements RandomService {

    private final RandomRepo randomRepo;

    private final AmazonS3 amazonS3;

    //File name
    private static final String objectKey = "1234.xlsx";

    @Override
    public String generatePreSignedUrlDownloadUrl(){

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKETS.FILE_TEST_BUCKET.value, objectKey);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        Date expiration = calendar.getTime();
        generatePresignedUrlRequest.setExpiration(expiration);
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    @Override
    public String generatePreSignedUploadUrl(){
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += Duration.ofHours(1).toMillis();
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKETS.FILE_TEST_BUCKET.value, objectKey)
                .withMethod(com.amazonaws.HttpMethod.PUT).withExpiration(expiration);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Async("asyncTaskExecutor")
    @Override
    public void exportFileToExcel(String preSignedUrl) throws IOException, InterruptedException {

        List<Random> all = randomRepo.findAll();
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Random Excel");
            int rowNumber = 0;
            Row headerRow = sheet.createRow(rowNumber++);
            headerRow.createCell(0).setCellValue("Id");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Address");

            for (Random random : all) {
                Row row = sheet.createRow(rowNumber++);
                row.createCell(0).setCellValue(random.getId());
                row.createCell(1).setCellValue(random.getName());
                row.createCell(2).setCellValue(random.getAddress());
            }
            File file = new File(objectKey);
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
            uploadFileToAWSS3(preSignedUrl,file);
        }
    }
    @Async("asyncTaskExecutor")
    @Override
    public void uploadFileToAWSS3(String preSignedUrl, File file) throws IOException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(
                        RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()
                ).build();
        HttpPut put = new HttpPut(preSignedUrl);
        HttpEntity entity = EntityBuilder.create()
                .setFile(file)
                .build();
        put.setEntity(entity);
        put.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        HttpResponse response = httpClient.execute(put);

        if (response.getStatusLine().getStatusCode() == 200) {
            log.info("File uploaded successfully at destination.");
        } else {
            log.info("Error occurred while uploading file.");
            log.info("Response Code: " + response.getStatusLine().getStatusCode());
            log.info("Response Content: " + EntityUtils.toString(response.getEntity()));
        }
    }

}
