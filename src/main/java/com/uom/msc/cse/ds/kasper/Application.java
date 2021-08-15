package com.uom.msc.cse.ds.kasper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync

@EnableAutoConfiguration
//@ComponentScan(basePackages={"com.baeldung.crud"})
//@EnableJpaRepositories(basePackages="com.baeldung.crud.repositories")
//@EnableTransactionManagement
//@EntityScan(basePackages="com.baeldung.crud.entities")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);







	}


}

