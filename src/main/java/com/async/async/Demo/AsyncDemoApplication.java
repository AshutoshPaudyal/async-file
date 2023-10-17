package com.async.async.Demo;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.async.async.Demo.model.Random;
import com.async.async.Demo.repo.RandomRepo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

@SpringBootApplication
@EnableAsync
public class AsyncDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RandomRepo randomRepo) {
		return args -> {
//			List<Random> all = randomRepo.findAll();
//
//			try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
//				Sheet sheet = workbook.createSheet("My All Work History");
//
//				int rowNumber = 0;
//				Row headerRow = sheet.createRow(rowNumber++);
//				headerRow.createCell(0).setCellValue("Id");
//				headerRow.createCell(1).setCellValue("Name");
//				headerRow.createCell(2).setCellValue("Address");
//
//				for (Random random : all) {
//					Row row = sheet.createRow(rowNumber++);
//
//					row.createCell(0).setCellValue(random.getId());
//					row.createCell(1).setCellValue(random.getName());
//					row.createCell(2).setCellValue(random.getAddress());
//				}
//
//				File excelFile = new File("random.xlsx");
//				try (FileOutputStream fileOut = new FileOutputStream(excelFile)) {
//					workbook.write(fileOut);
//
//					System.out.println(excelFile.getName());
//				}
//			}
		};
	}

}
