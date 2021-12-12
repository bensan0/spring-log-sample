package com.example.springlogsample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebLog implements Serializable {

    // 操作描述
    private String description;

    // 操作時間
    private Long startTime;

    // 消耗時間
    private Integer timeCost;

    // URL
    private String url;

    // URI
    private String uri;

    // 請求型別
    private String httpMethod;

    // IP地址
    private String ipAddress;

    // 請求引數
    private Object params;

    // 請求返回的結果
    private Object result;

    // 操作型別
    private String methodType;
}
