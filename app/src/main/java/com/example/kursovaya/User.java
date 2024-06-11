package com.example.kursovaya;

public class User {
    private String email;
    private String userId;
    private boolean blocked;

    public User(String email, String userId) {
        this.email = email;
        this.userId = userId;
        this.blocked = false;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
