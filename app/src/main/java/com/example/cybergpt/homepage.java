package com.example.cybergpt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class homepage extends AppCompatActivity {

    private EditText inputField;
    private Button sendButton;
    private ImageView imageMenu;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chatmessagemodel> chatMessages;

    DrawerLayout drawerlayout;
    NavigationView navigate;

    private backendhelper backend;

    private int currentQuestionIndex = 0;
    private JSONObject answers = new JSONObject();

    private String[] questions = {
            "What subjects do you enjoy the most?",
            "Do you prefer working with people, machines, or ideas?",
            "Do you want a creative career or a technical career?",
            "Would you like to study for many years (doctor, engineer) or prefer shorter training (designer, developer)?"
    };

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        inputField = findViewById(R.id.inputField);
        sendButton = findViewById(R.id.sendButton);
        recyclerView = findViewById(R.id.recyclerViewChat);
        imageMenu = findViewById(R.id.imageMenu);
        drawerlayout = findViewById(R.id.drawerlayout);
        navigate = findViewById(R.id.navigate);
        backend = new backendhelper();

        // Setup RecyclerView
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Start with first question
        askNextQuestion();

        sendButton.setOnClickListener(v -> {
            String answer = inputField.getText().toString().trim();
            if (!answer.isEmpty()) {
                handleAnswer(answer);
                inputField.setText("");
            }
        });

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle navigation drawer
                if (drawerlayout.isDrawerOpen(navigate)) {
                    drawerlayout.closeDrawer(navigate);
                } else {
                    drawerlayout.openDrawer(navigate);
                }
            }
        });
    }

    private void askNextQuestion() {
        if (currentQuestionIndex < questions.length) {
            addMessage(questions[currentQuestionIndex], false); // Bot message
        } else {
            analyzeAnswers();
        }
    }

    private void handleAnswer(String answer) {
        try {
            answers.put("Q" + (currentQuestionIndex + 1), answer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // User’s answer
        addMessage(answer, true);

        currentQuestionIndex++;
        askNextQuestion();
    }

    private void analyzeAnswers() {
        addMessage("Analyzing your answers...", false);

        backend.analyzeAnswers(answers, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> addMessage("Error: " + e.getMessage(), false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray candidates = jsonObject.getJSONArray("candidates");

                        if (candidates.length() > 0) {
                            JSONObject firstCandidate = candidates.getJSONObject(0);
                            JSONObject content = firstCandidate.getJSONObject("content");
                            JSONArray parts = content.getJSONArray("parts");

                            if (parts.length() > 0) {
                                String text = parts.getJSONObject(0).getString("text");

                                runOnUiThread(() -> {
                                    addMessage("Career Options:\n" + text, false);
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> addMessage("Error parsing response", false));
                    }
                } else {
                    runOnUiThread(() -> addMessage("Error: " + response.code(), false));
                }
            }
        });
    }

    private void generateRoadmap(String career) {
        backend.generateRoadmap(career, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> addMessage("Error: " + e.getMessage(), false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray candidates = jsonObject.getJSONArray("candidates");

                        if (candidates.length() > 0) {
                            JSONObject firstCandidate = candidates.getJSONObject(0);
                            JSONObject content = firstCandidate.getJSONObject("content");
                            JSONArray parts = content.getJSONArray("parts");

                            if (parts.length() > 0) {
                                String text = parts.getJSONObject(0).getString("text");

                                runOnUiThread(() -> {
                                    addMessage("Roadmap for " + career + ":\n" + text, false);
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> addMessage("Error parsing roadmap", false));
                    }
                } else {
                    runOnUiThread(() -> addMessage("Error: " + response.code(), false));
                }
            }
        });
    }

    // Add chat message to RecyclerView
    private void addMessage(String text, boolean isUser) {
        chatMessages.add(new Chatmessagemodel(text, isUser));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerView.scrollToPosition(chatMessages.size() - 1);
    }
}
