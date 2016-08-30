package com.yinghangjiaclient.util;

import com.orhanobut.logger.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by oubingjian on 2015/8/11.
 */

public class HttpUtil {
    //申明base url
    public static final String BASE_URL = "http://119.29.135.223:8000/";

    //通过url获得HttpGet对象
    public static HttpGet getHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        return request;
    }

    //通过url获得HttpPost对象
    public static HttpPost getHttpPost(String url) {
        HttpPost request = new HttpPost(url);
        return request;
    }

    //通过url获得HttpPut对象
    public static HttpPut getHttpPut(String url) {
        HttpPut request = new HttpPut(url);
        return request;
    }

    //通过url获得HttpDelete对象
    public static HttpDelete getHttpDelete(String url) {
        HttpDelete request = new HttpDelete(url);
        return request;
    }

    //通过httpGet 获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpGet request) throws IOException{
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpResponse response = new DefaultHttpClient(httpParameters).execute(request);
        return response;
    }

    //通过httpPost 获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpPost request) throws IOException{
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpResponse response = new DefaultHttpClient(httpParameters).execute(request);
        return response;
    }

    //HttpPut 获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpPut request) throws IOException{
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpResponse response = new DefaultHttpClient(httpParameters).execute(request);
        return response;
    }

    //HttpDelete 获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpDelete request) throws IOException{
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpResponse response = new DefaultHttpClient(httpParameters).execute(request);
        return response;
    }

    //通过url 发送post 请求，返回请求结果
    public static String queryStringForPost(String url, List<NameValuePair> para) {
        HttpPost request = HttpUtil.getHttpPost(url);
        ///----------
        request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        //--------------
        String result = null;
        try {
            request.setEntity(new UrlEncodedFormEntity(para, HTTP.UTF_8));
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        }
        return null;
    }

    //通过url 发送Put 请求，返回请求结果
    public static String queryStringForPut(String url, List<NameValuePair> para) {
        HttpPut request = HttpUtil.getHttpPut(url);
        ///----------
        request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        //--------------
        String result = null;
        try {
            request.setEntity(new UrlEncodedFormEntity(para, HTTP.UTF_8));
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        }
        return null;
    }

    //通过url 发送get 请求，返回请求结果
    public static String queryStringForGet(String url) {
        HttpGet request = HttpUtil.getHttpGet(url);
        String result = null;
        try {
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            Logger.e(e.getMessage());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            Logger.e(e.getMessage());
            return result;
        }
        return null;
    }

    //通过url 发送delete 请求，返回请求结果
    public static String queryStringForDelete(String url) {
        HttpDelete request = HttpUtil.getHttpDelete(url);
        String result = null;
        try {
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            Logger.e(e.getMessage());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            Logger.e(e.getMessage());
            return result;
        }
        return null;
    }

}
