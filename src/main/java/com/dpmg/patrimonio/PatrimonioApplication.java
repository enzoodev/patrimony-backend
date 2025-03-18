package com.dpmg.patrimonio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class PatrimonioApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatrimonioApplication.class, args);
	}

//	@Bean
//	public Executor taskExecutor() {
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(2);
//		executor.setMaxPoolSize(2);
//		executor.setQueueCapacity(500);
//		executor.setThreadNamePrefix("importar-inventario-");
//		executor.initialize();
//		return executor;
//	}
}
