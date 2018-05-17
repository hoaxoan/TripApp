package com.winter.dreamhub.api.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.winter.dreamhub.api.auth.BasicAuthInterceptor;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.User;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hoaxoan on 12/3/2016.
 */

public class WinterPrefs {

    private static final String WINTER_PREF = "WINTER_PREF";
    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String KEY_AUTHORIZATION = "KEY_AUTHORIZATION";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_USER = "KEY_USER";

    private static volatile WinterPrefs singleton;
    private WinterService api;
    private final SharedPreferences prefs;

    private String accessToken;
    private String authorization;
    private long userId;
    private User user;
    private boolean isLoggedIn = false;

    public static WinterPrefs get(Context context) {
        if (singleton == null) {
            synchronized (WinterPrefs.class) {
                singleton = new WinterPrefs(context);
            }
        }
        return singleton;
    }


    public WinterPrefs(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(WINTER_PREF, Context.MODE_PRIVATE);
        accessToken = prefs.getString(KEY_ACCESS_TOKEN, null);
        authorization = prefs.getString(KEY_AUTHORIZATION, null);
        isLoggedIn = !TextUtils.isEmpty(accessToken);
        if (isLoggedIn) {
            userId = prefs.getLong(KEY_USER_ID, 0);
            String userStr = prefs.getString(KEY_USER, "");
            user = User.toUser(userStr);
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void logOut() {
        prefs.edit().putString(KEY_ACCESS_TOKEN, null).apply();
        prefs.edit().putString(KEY_AUTHORIZATION, null).apply();
        prefs.edit().putLong(KEY_USER_ID, 0).apply();
        prefs.edit().putString(KEY_USER, "").apply();

        accessToken = null;
        authorization = null;
        isLoggedIn = false;
        userId = 0;
        user = null;
    }


    public User getUser() {
        return this.user;
    }

    public void setAccessToken(String accessToken) {
        if (!TextUtils.isEmpty(accessToken)) {
            this.accessToken = accessToken;
            isLoggedIn = true;
            prefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
            createApi();
        }
    }

    public void setLoggedInUser(User user) {
        if (user != null) {
            this.userId = user.id;
            this.user = user;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(KEY_USER_ID, userId);
            editor.putString(KEY_USER, User.toJSON(user));
            editor.apply();
        }
    }

    public WinterService getApi() {
        if (api == null) createApi();
        return api;
    }

    private void createApi() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(authorization, accessToken))
                .build();
        api = new Retrofit.Builder()
                .baseUrl(WinterService.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WinterService.class);
    }
}
