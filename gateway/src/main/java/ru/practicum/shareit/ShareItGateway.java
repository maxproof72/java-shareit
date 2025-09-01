package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class ShareItGateway {

	public static final String SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
	public static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);

	public static void main(String[] args) {
		SpringApplication.run(ShareItGateway.class, args);
	}

}
