package com.gzella.coinMarketSupervisor;

import com.gzella.coinMarketSupervisor.business.exceptions.UserAlreadyExistsException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class CoinMarketSupervisorApplication {

	public static void main(String[] args) throws InterruptedException, UserAlreadyExistsException, IOException {
		SpringApplication.run(CoinMarketSupervisorApplication.class, args);
	}
}