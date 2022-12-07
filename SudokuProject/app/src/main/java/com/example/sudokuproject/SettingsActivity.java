package com.example.sudokuproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    String difficultySelected;
    boolean isDark;

    SwitchCompat themeSwitch;
    RadioGroup radioGroup;
    Button doneButton;

    void onSwitchClick(CompoundButton button, boolean isChecked) {
        isDark = isChecked;
    }

    void onRadioButtonClick(View button) {
        if (button.getId() == R.id.difficulty_easy) {
            difficultySelected = "easy";
        }
        else if (button.getId() == R.id.difficulty_normal) {
            difficultySelected = "normal";
        }
        else if (button.getId() == R.id.difficulty_hard) {
            difficultySelected = "hard";
        }
        else if (button.getId() == R.id.difficulty_demo) {
            difficultySelected = "demo";
        }
    }

    void onDoneButtonClick(View button) {
        Intent intent = new Intent();
        intent.putExtra("difficulty", difficultySelected);
        intent.putExtra("isDark", isDark);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        difficultySelected = intent.getStringExtra("difficulty");
        isDark = intent.getBooleanExtra("isDark", false);

        radioGroup = findViewById(R.id.difficultyGroup);
        themeSwitch = (SwitchCompat) findViewById(R.id.themeSwitch);
        doneButton = (Button) findViewById(R.id.finishSettings);

        themeSwitch.setChecked(isDark);
        if (difficultySelected.equals("easy")) {
            RadioButton b = findViewById(R.id.difficulty_easy);
            b.setChecked(true);
        }
        else if (difficultySelected.equals("normal")) {
            RadioButton b = findViewById(R.id.difficulty_normal);
            b.setChecked(true);
        }
        else if (difficultySelected.equals("hard")) {
            RadioButton b = findViewById(R.id.difficulty_hard);
            b.setChecked(true);
        }
        else if (difficultySelected.equals("demo")) {
            RadioButton b = findViewById(R.id.difficulty_demo);
            b.setChecked(true);
        }

        themeSwitch.setOnCheckedChangeListener(this::onSwitchClick);
        for (int i = 0; i < radioGroup.getChildCount(); ++i) {
            RadioButton button = (RadioButton) radioGroup.getChildAt(i);
            button.setOnClickListener(this::onRadioButtonClick);
        }
        doneButton.setOnClickListener(this::onDoneButtonClick);
    }
}