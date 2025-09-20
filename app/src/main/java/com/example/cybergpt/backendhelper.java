package com.example.cybergpt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class backendhelper {
    private static final String API_KEY = "AIzaSyAYtA8a744XIdMa1NDWPVSd4JUngJy_JpE";

    // Configure client with longer timeouts
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)   // connection timeout
            .readTimeout(60, TimeUnit.SECONDS)      // server response timeout
            .writeTimeout(30, TimeUnit.SECONDS)     // request body write timeout
            .build();

    // Analyze answers method
    public void analyzeAnswers(JSONObject answers, Callback callback) {
        String prompt = "Suggest 5 career options based on these answers: " + answers.toString();

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

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // Generate roadmap method
    public void generateRoadmap(String career, Callback callback) {
        String prompt = "Generate a step-by-step learning roadmap for becoming a " + career;

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

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
