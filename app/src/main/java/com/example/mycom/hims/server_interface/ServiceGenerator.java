package com.example.mycom.hims.server_interface;

import com.example.mycom.hims.common.NetDefine;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class ServiceGenerator implements NetDefine{
    private static String token = "";
    private static HttpLoggingInterceptor logging ;

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASIC_PATH)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-mm-dd HH:mm:ss").create()));

    private static boolean isFile =false;
    public static <S> S createService(Class<S> serviceClass) {
        if (logging == null) {
            logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        isFile =false;
        return createService(serviceClass, token);
    }

    public static <S> S createService(Class<S> serviceClass,boolean bool) {
        if (logging == null) {
            logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        isFile = bool;
        return createService(serviceClass, token);
    }

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.interceptors().clear();
            httpClient.interceptors().add(logging);
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Basic " + authToken)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                    if(isFile) {
                        requestBuilder.header("Content-Type","multipart/form-data");
                    }


                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }



//    public static <S> S createService(Class<S> serviceClass, String username, String password) {
//        if (username != null && password != null) {
//            String credentials = username + ":" + password;
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//            httpClient.interceptors().clear();
//            httpClient.interceptors().add(new Interceptor() {
//                @Override
//                public Response intercept(Interceptor.Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Authorization", basic)
//                    .header("Accept", "applicaton/json")
//                    .method(original.method(), original.body());
//
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            });
//        }
//
//        Retrofit retrofit = builder.client(httpClient).build();
//        return retrofit.create(serviceClass);
//    }
}