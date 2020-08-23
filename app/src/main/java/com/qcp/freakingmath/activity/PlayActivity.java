package com.qcp.freakingmath.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.qcp.freakingmath.R;

import java.util.Random;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    public static final String FONT_PATH = "fonts/BADABB__.TTF";
    public static final String SCORE = "score";
    public static final int TIMER = 10;
    private TextView tvScore, tvBestScore, tvQuestion, tvTimer;
    private Button btnTrue, btnFalse;
    private int number1, number2, result, lucky, answerRight, score, timer;
    private int best;
    private Random random;
    private Handler handler;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private boolean isRunning = true;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
        setContentView(R.layout.activity_play);
        score = 0;
        findViewById();
        setTypeface();
        setGameStatus();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.d("qcpp", "Handlemessage:" + timer);
                int what = msg.what;
                if (what == 1) {
                    progressBar.setProgress(timer);
                    tvTimer.setText(timer + "");
                }
            }
        };
        generateQuestion();
        btnFalse.setOnClickListener(this);
        btnTrue.setOnClickListener(this);
    }

    private void setGameStatus() {
        @SuppressLint("StringFormatMatches") String scoreString = getString(R.string.score, score);
        tvScore.setText(scoreString);
        sharedPreferences = getSharedPreferences(SCORE, Context.MODE_PRIVATE);
        best = sharedPreferences.getInt(SCORE, 0);
        @SuppressLint("StringFormatMatches") String strBestScore = getString(R.string.best, best);
        tvBestScore.setText(strBestScore);
    }


    @SuppressLint("HandlerLeak")
    private void generateQuestion() {
        isRunning = true;
        timer = TIMER;
        number1 = random.nextInt(10);
        number2 = random.nextInt(10);
        answerRight = number1 + number2;
        lucky = random.nextInt(2);
        if (lucky == 0) {
            result = answerRight;
        } else if (lucky == 1) {
            result = number1 + number2 + random.nextInt(5);
        } else {
            result = number1 + number2 - random.nextInt(5);
        }
        String question = number1 + " + " + number2 + " = " + result;
        tvQuestion.setText(question);
//        int index;
//        Log.d("qcpp", "Timer1:" + Integer.toString(timer));
//        for (index = 0; index < TIMER + 1; index++) {
//            Log.d("qcpp", "Timer2:" + Integer.toString(timer));
//            Log.d("qcpp", "Timer3:" + Integer.toString(timer));
//        }
        Log.d("qcpp", "Timer1:" + Integer.toString(timer));
        Thread thread = new Thread(PlayActivity.this);
        thread.start();
    }

    private void setTypeface() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tvBestScore.setTypeface(typeface);
        tvScore.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvQuestion.setTypeface(typeface);
        btnTrue.setTypeface(typeface);
        btnFalse.setTypeface(typeface);
    }

    private void findViewById() {
        tvBestScore = findViewById(R.id.tv_best_score);
        tvScore = findViewById(R.id.tv_score);
        tvQuestion = findViewById(R.id.tv_question);
        tvTimer = findViewById(R.id.tv_timer);
        btnTrue = findViewById(R.id.btn_true);
        btnFalse = findViewById(R.id.btn_false);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_true) {
            if (result == answerRight) {
                score++;
                @SuppressLint("StringFormatMatches") String scoreString = getString(R.string.score, score);
                tvScore.setText(scoreString);
                isRunning = false;
                handler.removeCallbacksAndMessages(null);
                handler.removeCallbacks(this);
                handler.removeMessages(1);
                generateQuestion();
            } else {
                gameOver();
            }
        }
        if (view.getId() == R.id.btn_false) {
            if (result != answerRight) {
                score++;
                @SuppressLint("StringFormatMatches") String scoreString = getString(R.string.score, score);
                tvScore.setText(scoreString);
                isRunning = false;
                handler.removeCallbacksAndMessages(null);
                handler.removeCallbacks(this);
                handler.removeMessages(1);
                generateQuestion();
            } else {
                gameOver();
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    private void gameOver() {
        handler.removeCallbacksAndMessages(null);
        handler.removeCallbacks(this);
        handler.removeMessages(1);
        if (score > best) {
            sharedPreferences.edit()
                    .putInt(SCORE, score)
                    .commit();
        }
        Intent intent = new Intent(PlayActivity.this, GameOverActivity.class);
        intent.putExtra(SCORE, score);
        isRunning = false;
        startActivity(intent);
        finish();
    }

    @Override
    public void run() {
        while (timer > 1 && isRunning) {
            timer--;
            handler.sendEmptyMessage(1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (timer <= 1) {
            gameOver();
        }
    }
}