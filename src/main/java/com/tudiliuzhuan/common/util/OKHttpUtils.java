package com.tudiliuzhuan.common.util;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author liuwenjie
 */
public class OKHttpUtils {

    private static int connectTimeout = 2;
    private static int writeTimeout = 300;
    private static int readTimeout = 300;

    /**
     * get请求
     *
     * @author liuwenjie
     * @since 2020/5/21
     */
    public static String get(String url) {
        String result = null;
        OkHttpClient client = buildOkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            result = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
        }
        return result;
    }

    /**
     * get请求
     *
     * @author liuwenjie
     * @since 2020/5/21
     */
    public static String get(String url, Map<String, String> params) {
        String result = null;
        OkHttpClient client = buildOkHttpClient();
        Request request = new Request.Builder().url(url + "?" + buildParams(params)).get().build();
        try {
            Response response = client.newCall(request).execute();
            result = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
        }
        return result;
    }

    /**
     * post请求
     *
     * @param url         地址
     * @param content     json数据
     * @param contentType 内容类型
     * @author liuwenjie
     * @since 2020/5/21
     */
    public static String post(String url, String content, String contentType) {
        String result = null;
        OkHttpClient client = buildOkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), content);
        Request request = new Request.Builder().url(url).post(requestBody)
                .addHeader("Content-Type", contentType)
                .build();
        try {
            Response response = client.newCall(request).execute();
            result = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
        }
        return result;
    }


    /**
     * post请求
     *
     * @param url         地址
     * @param content     json数据
     * @param contentType 内容类型
     * @param headers headers
     *
     * @author liuwenjie
     * @since 2020/5/21
     */
    public static String post(String url, String content, String contentType, Map<String, String> headers) {
        String result = null;
        OkHttpClient client = buildOkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), content);

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(requestBody);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            builder.addHeader(e.getKey(), e.getValue());
        }

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            result = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
        }
        return result;
    }

    /**
     * post请求，body非json
     *
     * @param url
     * @param contentType
     * @param headers
     * @param bodys
     * @return
     * @author liuwenjie
     * @since 2020/5/25
     */
    public static String post(String url, String contentType, Map<String, String> headers, Map<String, String> bodys) {
        OkHttpClient client = buildOkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse(contentType), buildParams(bodys));

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.method("POST", body);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            builder.addHeader(e.getKey(), e.getValue());
        }
        Response response = null;
        try {
             response = client.newCall(builder.build()).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static OkHttpClient buildOkHttpClient() {
        return OkHttpClientHolder.INSTANCE.getClientInstance();
    }

    private static String buildParams(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        params.forEach((key, value) -> stringBuilder.append(key + "=" + value + "&"));

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * SSLSocketFactory
     *
     * @author liuwenjie
     * @since 2020/5/27
     */
    private static SSLSocketFactory slSocketFactory(X509TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    private static class LeyueX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 通过枚举获取
     *
     * @author liuwenjie
     * @since 2020/5/27
     */
    public enum OkHttpClientHolder {
        //
        INSTANCE;

        private OkHttpClient clientInstance;

        OkHttpClientHolder() {
            X509TrustManager x509TrustManager = new LeyueX509TrustManager();
            clientInstance = new OkHttpClient.Builder()
                    .sslSocketFactory(slSocketFactory(x509TrustManager), x509TrustManager)
                    .hostnameVerifier(new TrustAllHostnameVerifier())
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                    .build();
        }

        public OkHttpClient getClientInstance() {
            return clientInstance;
        }
    }
}