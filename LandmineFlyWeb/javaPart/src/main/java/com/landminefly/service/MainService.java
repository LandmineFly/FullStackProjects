package com.landminefly.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MainService {
    public String getWeather(String districtId, String dataType) {

        final String ak = "GZrjaNhG5WrcxhlfzczhxLd5773i08Rl";
        final String baseUrl = "https://api.map.baidu.com/weather/v1/";
        // 设置baseUrl，构建WebClient
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

                        // 发送get请求
        return webClient.get()
                        // 设置参数
                        .uri(uriBuilder -> {
                            uriBuilder.queryParam("district_id", districtId);
                            uriBuilder.queryParam("data_type", dataType);
                            uriBuilder.queryParam("ak", ak);
                            return uriBuilder.build();
                        })
                        // 执行请求，获取响应
                        .retrieve()
                        // 将返回的响应体反序列化为 String
                        .bodyToMono(String.class)
                        // 阻塞当前线程，直到响应完成，并返回实际结果
                        .block();
    }
}

