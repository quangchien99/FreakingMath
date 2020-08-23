package com.qcp.freakingmath.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qcp.freakingmath.R;

public class MainActivity extends AppCompatActivity {
    public static final String FONT_PATH = "fonts/BADABB__.TTF";
    private TextView tvBestScore, tvGameName;
    private Button btnPlay, btnMore;
    private SharedPreferences sharedPreferences;
    private EditText edtPlayerName;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        setTypeface();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPlayerName.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Waring")
                            .setMessage("Please fill in your name to start !")
                            .setCancelable(true)
                            .show();
                } else {
                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    sharedPreferences = getSharedPreferences(PlayActivity.SCORE, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("playerBest", edtPlayerName.getText().toString()).commit();
                    Log.d("qcpp", edtPlayerName.getText().toString() + "check");
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences(PlayActivity.SCORE, Context.MODE_PRIVATE);
        int best = sharedPreferences.getInt(PlayActivity.SCORE, 0);
        String nameBest = sharedPreferences.getString("playerBest", "Error");
        @SuppressLint("StringFormatMatches") String strBestScore = getString(R.string.best_score, nameBest + " " + best);
        tvBestScore.setText(strBestScore);
    }

    private void setTypeface() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tvBestScore.setTypeface(typeface);
        tvGameName.setTypeface(typeface);
        btnMore.setTypeface(typeface);
        btnPlay.setTypeface(typeface);
        edtPlayerName.setTypeface(typeface);
    }

    private void findViewById() {
        tvBestScore = findViewById(R.id.tv_best_score);
        tvGameName = findViewById(R.id.tv_game_name);
        btnPlay = findViewById(R.id.btn_play);
        btnMore = findViewById(R.id.btn_more);
        edtPlayerName = findViewById(R.id.edt_player_name);
    }
}