package com.spring.practice.model.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.spring.practice.model.User;
import com.spring.practice.model.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FcmServiceImpl implements FcmService{

    //Firebase객체 초기화 오류로 사용 X
//    @Autowired
//    private FirebaseMessaging firebaseMessaging;

    String fcmUrl = "https://fcm.googleapis.com/v1/projects/springbootfcmtest/messages:send";

    @Autowired
    FirebaseCloudMessageService firebaseCloudMessageService;

    @Override
    public void sendFcm() {
        //testToken
        String testToken = "dq5wlQ5YRsG0nQ9qB7UHP5:APA91bGzxJeIncuZRTsn5MA8ynBxn5d5fLcSJw_bowQNZAb0J3S4d0-1fWYHFWI2jPn4fsWDzR-fT8dINv1MRJgcM0R_h-s1yZGgpwCSBxy_oqPployA3ppvKjqp-CiY3FwJR67mRLRU";
        String testTitle = "테스트 타이틀";
        String testBody = "테스트 바디";

        try{
            sendPushNotification(testToken, testTitle, testBody);
            log.info("fcm 전송 성공");
        }catch (Exception e){
            log.info("fcm 전송 실패");
        }

//        try {
//            firebaseCloudMessageService.sendMessageTo(testToken, testTitle, testBody);
//        }catch (Exception e){
//            log.info("service impl 오류");
//        }


    }


    private static String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        // GoogleApi를 사용하기 위해 oAuth2를 이용해 인증한 대상을 나타내는객체
        GoogleCredentials googleCredentials = GoogleCredentials
                // 서버로부터 받은 service key 파일 활용
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                // 인증하는 서버에서 필요로 하는 권한 지정
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        String token = googleCredentials.getAccessToken().getTokenValue();

        return token;
    }



    //fcm 전송
    public void sendPushNotification(String deviceToken, String title, String body) throws IOException {

//        sendPushNotificationUsingServerKey(deviceToken, title, body);
        sendPushNotificationUsingAccessToken(deviceToken, title, body);
//        sendPushNotificationUsingFirebaseInstance(deviceToken, title, body);
    }



    private void sendPushNotificationUsingServerKey(String deviceToken, String title, String body) {
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "key=AAAAtBGjJ5M:APA91bFN7UxR5Su16HyqFSXPEJhUsB2-jvQXi2NZUmn39s7EuVuxACdls6zwHSBQ2Vqy-NDuLLlOlYcZCriOGzJz_BctSqHAs9ONPeHKcP7bVFD-6XksbEkAr09L57joCpdvHIVwtwcJ");

        Map<String, Object> data = new HashMap<>();
        data.put("to", deviceToken);

        Map<String, String> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);

        data.put("notification", notification);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(fcmUrl, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("FCM message sent successfully!");
        } else {
            System.out.println("Failed to send FCM message. Status code: " + responseEntity.getStatusCode());
        }
    }

        /*
    public void sendPushNotificationUsingFirebaseInstance(String deviceToken, String title, String body) {
        Message message = Message.builder()
                .setToken(deviceToken)
                .putData("title", title)
                .putData("body", body)
                .build();

        try{
//            String response = FirebaseMessaging.getInstance().send(message);
            String response = firebaseMessaging.send(message);
            System.out.println("Successfully sent message: " + response);
            log.info("fcm 성공");
        }catch (Exception e){
            e.printStackTrace();
            log.info("fcm 실패");
        }
    }

     */


    private void sendPushNotificationUsingAccessToken(String deviceToken, String title, String body) throws IOException{

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());  //OAuth 2.0 사용
        headers.set("Authorization", "Bearer " + getAccessToken());


        log.info("json 초기화 전");
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        message.put("token", deviceToken);
        Map<String, String> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);
        message.put("notification", notification);
        data.put("message", message);

        log.info("json 초기화 후");
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity(fcmUrl, requestEntity, String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(fcmUrl, requestEntity, String.class);
        log.info("entity 초기화 후");

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("FCM message sent successfully!");
        } else {
            log.info("Failed to send FCM message. Status code: " + responseEntity.getStatusCode());
        }
    }
}
