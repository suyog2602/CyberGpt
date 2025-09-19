package com.example.cybergpt;

public class Chatmessagemodel {
    private String message;
    private boolean isUser;

    public Chatmessagemodel(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() { return message; }
    public boolean  isUser() { return isUser; }


}
