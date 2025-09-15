package com.example.cybergpt;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class backendhelper {
    private static final String GEMINI_API_KEY = "AIzaSyC-3uqhJWVRbgWb0GeGHwJCd-dqoYIbwQk";
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + GEMINI_API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    public void analyzeAnswers(JSONObject answers, Callback callback) {
        try {
            String prompt = "You are a career counselor. Based on these answers suggest careers in JSON:\n"
                    + answers.toString();

            JSONObject bodyJson = new JSONObject();
            bodyJson.put("contents", new org.json.JSONArray()
                    .put(new JSONObject()
                            .put("parts", new org.json.JSONArray()
                                    .put(new JSONObject().put("text", prompt)))));

            RequestBody body = RequestBody.create(
                    bodyJson.toString(), MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(GEMINI_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void generateRoadmap(String career, Callback callback) {
        String prompt = "Generate a step-by-step learning roadmap for becoming a successful "
                + career + ". Provide clear stages, required skills, and resources.";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("contents", new JSONArray()
                    .put(new JSONObject()
                            .put("parts", new JSONArray()
                                    .put(new JSONObject().put("text", prompt))
                            )));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + GEMINI_API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }


}

