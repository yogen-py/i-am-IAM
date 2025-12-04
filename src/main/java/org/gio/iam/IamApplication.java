package org.gio.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class IamApplication {

	public static void main(String[] args) {
		// FIX: Force the application to use UTC to avoid "Asia/Calcutta" errors in Postgres
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(IamApplication.class, args);
	}
}