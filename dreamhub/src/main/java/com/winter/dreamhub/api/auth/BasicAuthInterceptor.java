package com.winter.dreamhub.api.auth;

import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hoaxoan on 12/3/2016.
 */

public class BasicAuthInterceptor implements Interceptor {

    private final String authorization;
    private final String accessToken;

    public BasicAuthInterceptor(String authorization, String accessToken) {
        this.authorization = authorization;
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .method(original.method(), original.body());

        if (!TextUtils.isEmpty(accessToken)) {
            requestBuilder.header("Authorization", "Bearer " + accessToken);
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);

    }
}