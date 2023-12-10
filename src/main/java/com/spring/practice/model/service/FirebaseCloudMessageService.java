package com.spring.practice.model.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.spring.practice.model.FcmDataMessage;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * FCM 알림 메시지 생성
 *
 * @author taeshik.heo
 *
 */
@Component
public class FirebaseCloudMessageService {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseCloudMessageService.class);

    public final ObjectMapper objectMapper;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/springbootfcmtest/messages:send";

    String userToken = "dq5wlQ5YRsG0nQ9qB7UHP5:APA91bGzxJeIncuZRTsn5MA8ynBxn5d5fLcSJw_bowQNZAb0J3S4d0-1fWYHFWI2jPn4fsWDzR-fT8dINv1MRJgcM0R_h-s1yZGgpwCSBxy_oqPployA3ppvKjqp-CiY3FwJR67mRLRU";


    /**
     * FCM에 push 요청을 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
     * @return
     * @throws IOException
     */
    private String getAccessToken() throws IOException {
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


    /**
     * FCM 알림 메시지 생성
     * background 대응을 위해서 data로 전송한다.
     * @param targetToken
     * @param title
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    private String makeDataMessage(String targetToken, String title, String body) throws JsonProcessingException {
//        Notification noti = new FcmMessage.Notification(title, body, null);
        Map<String,String> map = new HashMap<>();
        map.put("title", title);
        map.put("body", body);

        FcmDataMessage.Message message = new FcmDataMessage.Message();
        message.setToken(targetToken);
        message.setData(map);

        FcmDataMessage fcmMessage = new FcmDataMessage(false, message);

        return objectMapper.writeValueAsString(fcmMessage);
    }


    /**
     * targetToken에 해당하는 device로 FCM 푸시 알림 전송
     * @param targetToken
     * @param title
     * @param body
     * @throws IOException
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        logger.info("sendMessageTo 실행");
//        String message = makeMessage(targetToken, title, body);
        String message = makeDataMessage(targetToken, title, body);
        logger.info("message : {}", message);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                // 전송 토큰 추가z
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        logger.info("response : {}", response);



    }


    public FirebaseCloudMessageService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }


}