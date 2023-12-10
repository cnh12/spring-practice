package com.spring.practice;

//import org.mybatis.spring.annotation.MapperScan;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@Configuration
@SpringBootApplication
@Slf4j
//@MapperScan("com.spring.practice.model.mapper")
public class Application {

	private static boolean firebaseAppInitialized = false;

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}
	
	//fcm 보내는 FirebaseInstance 초기화 - 오류로 인한 사용 X
	/*
	@Bean
	FirebaseApp firebaseApp() throws IOException{
		
		log.info("firebaseApp 실행");

		if(!firebaseAppInitialized){
			GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
					new ClassPathResource("springbootfcmtest-firebase-adminsdk-6jj5x-2dadb17b0f.json").getInputStream());

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(googleCredentials)
					.build();

			firebaseAppInitialized = true;
			return FirebaseApp.initializeApp(options);

		}
		else{
			return FirebaseApp.getInstance();
		}

	}

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException{

		return FirebaseMessaging.getInstance(firebaseApp());

	}

	 */


}
