package com.flowforge.auth.dto.response;

public class ProfileResponse {
    private String username;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  ProfileResponse(String username, String email){
        this.email = email;
        this.username = username;
    }
}
