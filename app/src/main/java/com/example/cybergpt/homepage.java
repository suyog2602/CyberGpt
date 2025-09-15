package com.example.cybergpt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class homepage extends AppCompatActivity {

    private TextView chatView, questionView;
    private EditText inputField;
    private Button sendButton;

    private backendhelper backend;

    private int currentQuestionIndex = 0;
    private JSONObject answers = new JSONObject();

    private String[] questions = {
            "What subjects do you enjoy the most?",
            "Do you prefer working with people, machines, or ideas?",
            "Do you want a creative career or a technical career?",
            "Would you like to study for many years (doctor, engineer) or prefer shorter training (designer, developer)?"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        chatView = findViewById(R.id.chatView);
        questionView = findViewById(R.id.questionView);
        inputField = findViewById(R.id.inputField);
        sendButton = findViewById(R.id.sendButton);

        backend = new backendhelper();

        // Start asking first question
        askNextQuestion();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = inputField.getText().toString().trim();
                if (!answer.isEmpty()) {
                    handleAnswer(answer);
                    inputField.setText("");
                }
            }
        });
    }

    private void askNextQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionView.setText(questions[currentQuestionIndex]);
        } else {
            // All answers collected → analyze
            analyzeAnswers();
        }
    }

    private void handleAnswer(String answer) {
        try {
            answers.put("Q" + (currentQuestionIndex + 1), answer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        chatView.append("\nYou: " + answer);

        currentQuestionIndex++;
        askNextQuestion();
    }

    private void analyzeAnswers() {
        chatView.append("\n\nAnalyzing your answers...");

        backend.analyzeAnswers(answers, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> chatView.append("\nError: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();

                runOnUiThread(() -> {
                    chatView.append("\n\nCareer Options:\n" + resp);

                    // For now, show raw AI response
                    // Next step → parse JSON & show clickable options
                });
            }
        });
    }

    // Example: user selects a career manually (in hackathon you can add a button for each)
    private void generateRoadmap(String career) {
        backend.generateRoadmap(career, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> chatView.append("\nError: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();

                runOnUiThread(() -> {
                    chatView.append("\n\nRoadmap for " + career + ":\n" + resp);
                });
            }
        });
    }
}
