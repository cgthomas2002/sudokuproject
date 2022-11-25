package com.example.sudokuproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GenerateSudoku gen = new GenerateSudoku();

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

        int[][] sudokuLayout = gen.GetSudoku();

        for (int i = 0; i < sudokuLayout.length; ++i) {
            TableRow row = new TableRow(this);
            for (int j = i; j < sudokuLayout[i].length; ++j) {
                char c = startStr.charAt(i * 9 + j);
                table[i][j] = new Cell(Character.getNumericValue(c), this);
                row.addView(table[i][j].button);
            }
            layout.addView(row);
        }
        setContentView(layout);
    }
}