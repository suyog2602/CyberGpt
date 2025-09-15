package com.example.cybergpt;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class backendhelper {
    private static final String API_KEY = "YOUR_API_KEY";
    private OkHttpClient client = new OkHttpClient();

    // Analyze answers method
    public void analyzeAnswers(JSONObject answers, Callback callback) {
        String prompt = "Suggest 3 career options based on these answers: " + answers.toString();

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
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY)
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

        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
