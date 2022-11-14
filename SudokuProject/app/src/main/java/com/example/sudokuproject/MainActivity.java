package com.example.sudokuproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private class Cell {
        int val;
        boolean fixed;
        Button button;

        public Cell(int startVal, Context context) {
            val = startVal;
            fixed = (val != 0 && true) || (false);
            button = new Button(context);
            if (fixed) {
                button.setText(String.valueOf(val));
            }
            else {
                button.setText("?");
            }
        }
    }

    Cell[][] table;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        table = new Cell[9][9];
        String startStr = "";

        TableLayout layout = new TableLayout(this);

        for (int n = 0; n < 81; ++n) { //generate random numbers for each cell in the 9x9 grid
            int num = rand.nextInt(10); //random number between 0-9
            startStr += num;
        }

        for (int i = 0; i < 9; ++i) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < 9; ++j) {
                char c = startStr.charAt(i * 9 + j);
                table[i][j] = new Cell(Character.getNumericValue(c), this);
                row.addView(table[i][j].button);
            }
            layout.addView(row);
        }
        setContentView(layout);
    }
}