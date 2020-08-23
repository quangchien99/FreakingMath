package com.qcp.freakingmath.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qcp.freakingmath.R;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String FONT_PATH = "fonts/BADABB__.TTF";
    private TextView tvGameOver, tvYourScore;
    private Button btnTryAgain, btnHome;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        findViewById();
        setTypeface();
        int score = getIntent().getExtras().getInt(PlayActivity.SCORE);
        sharedPreferences = getSharedPreferences(PlayActivity.SCORE, Context.MODE_PRIVATE);
        String playerName = sharedPreferences.getString("playerBest", "defaultStringIfNothingFound");
        @SuppressLint("StringFormatMatches") String
                strScore = getString(R.string.your_score, playerName + " " + score);
        tvYourScore.setText(strScore);
        btnHome.setOnClickListener(this);
        btnTryAgain.setOnClickListener(this);

    }

    private void setTypeface() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tvGameOver.setTypeface(typeface);
        tvYourScore.setTypeface(typeface);
        btnTryAgain.setTypeface(typeface);
        btnHome.setTypeface(typeface);
    }

    private void findViewById() {
        tvGameOver = findViewById(R.id.tv_game_over);
        tvYourScore = findViewById(R.id.tv_your_score);
        btnTryAgain = findViewById(R.id.btn_try_again);
        btnHome = findViewById(R.id.btn_home);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_try_again) {
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.btn_home) {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}