package com.example.joiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigInteger;
import java.util.function.Consumer;

@SpringBootApplication
public class JoinerApplication {

	Logger logger = LoggerFactory.getLogger(JoinerApplication.class);

	@Autowired
	RedisHandler redisHandler;

	public static void main(String[] args) {
		SpringApplication.run(JoinerApplication.class, args);
	}

	@Bean
	public Consumer<String> sink() {
		return jobId -> {
			// Check sth
			logger.info("Notification received {}", jobId);

			Integer totalTasks = Integer.parseInt(redisHandler.get(jobId, "total-tasks"));
			Integer doneTasks = Integer.parseInt(redisHandler.get(jobId, "done-tasks"));

			doneTasks++;

			if (totalTasks.compareTo(doneTasks) == 0) {
				// Job terminado
				logger.info("Job finished {}", jobId);
				BigInteger finalResult = BigInteger.ZERO;
				for (int i = 1; i <= totalTasks; i++) {
					String doneTask = redisHandler.get(jobId, String.valueOf(i));
					finalResult = finalResult.add(new BigInteger(doneTask));
				}
				logger.info("Job result {} :)", finalResult);
			}
			else {
				logger.info("Job not finished yet {}", jobId);
				redisHandler.save(jobId, "done-tasks", doneTasks);
			}
		};

	}

}
