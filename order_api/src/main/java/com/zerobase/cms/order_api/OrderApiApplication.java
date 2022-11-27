package com.zerobase.cms.order_api;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@ServletComponentScan
//@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
public class OrderApiApplication {

	@Bean
	public JPAQueryFactory queryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}
	public static void main(String[] args) {
		SpringApplication.run(OrderApiApplication.class, args);
	}

}
