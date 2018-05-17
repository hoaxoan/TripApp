package com.winter.dreamhub.api.service.model;

/**
 * Created by hoaxoan on 12/3/2016.
 */

public class AccessToken {

    public final String id;
    public final String login;
    public final String firstname;
    public final String lastname;
    public final String mail;
    public final String api_key;
    public final String status;

    public AccessToken(String id,
                       String login,
                       String firstname,
                       String lastname,
                       String mail,
                       String api_key,
                       String status) {
        this.id = id;
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.api_key = api_key;
        this.status = status;
    }
}