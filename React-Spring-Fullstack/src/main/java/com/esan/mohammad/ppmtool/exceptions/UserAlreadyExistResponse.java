package com.esan.mohammad.ppmtool.exceptions;

public class UserAlreadyExistResponse {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserAlreadyExistResponse(String username) {

        this.username = username;
    }

}
