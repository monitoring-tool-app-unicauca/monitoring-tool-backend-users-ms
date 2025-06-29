package co.edu.unicauca.monitoring.tool.backend.users.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MonitoringToolBackendUsersMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringToolBackendUsersMsApplication.class, args);
	}

}
