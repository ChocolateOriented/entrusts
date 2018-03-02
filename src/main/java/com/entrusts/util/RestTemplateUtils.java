package com.entrusts.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by sxu on 2018/2/4.
 */
public class RestTemplateUtils {

    private static RestTemplate restTemplate;

    public static RestTemplate getRestTemplate(){
        if (restTemplate == null){
            restTemplate = ApplicationContextHolder.getBean("getRestTemplate");
        }
        return restTemplate;
    }

    /**
     * Get方法
     *
     * @param url:地址
     * @param returnClassName:返回对象类型,如:String.class
     * @param parameters:parameter参数
     * @return
     */
    public static <T> T get(String url, Class<T> returnClassName, Map<String, Object> parameters){
        if (parameters == null) {
            return getRestTemplate().getForObject(url, returnClassName);
        }
        return getRestTemplate().getForObject(url, returnClassName, parameters);
    }

    /**
     * post请求,包含了路径,返回类型,Header,Parameter
     *
     * @param url:地址
     * @param returnClassName:返回对象类型,如:String.class
     * @param inputHeader
     * @param inputParameter
     * @param jsonBody
     * @return
     */
    public static <T> T post(String url,Class<T> returnClassName,Map<String,Object> inputHeader,Map<String,Object> inputParameter,String jsonBody){
        //请求Header
        HttpHeaders httpHeaders = new HttpHeaders();
        //拼接Header
        if (inputHeader != null) {
            Set<String> keys = inputHeader.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                httpHeaders.add(key, inputHeader.get(key).toString());
            }
        }
        //设置请求的类型及编码
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        httpHeaders.add("Accept", "application/json");
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.ALL);
        httpHeaders.setAccept(acceptableMediaTypes);

        HttpEntity<String> formEntity = new HttpEntity<String>(jsonBody, httpHeaders);
        if (inputParameter==null) {
            return getRestTemplate().postForObject(url, formEntity, returnClassName);
        }
        return getRestTemplate().postForObject(url, formEntity, returnClassName, inputParameter);
    }
}
