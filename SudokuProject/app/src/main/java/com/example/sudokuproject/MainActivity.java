package com.example.sudokuproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    int N = 9; //number of rows/columns
    int K = 20; //number of unknown numbers

    GenerateSudoku gen = new GenerateSudoku(N, K);
    int[][] sudokuBoard = gen.getSudoku();

    private class Cell {
        int val;
        boolean fixed;
        Button button;

        public Cell(int startVal, Context context, int[] coords) {
            val = startVal;
            fixed = (val != 0 && true) || (false);
            button = new Button(context);
            if (fixed) {
                button.setText(String.valueOf(val));
                button.setBackgroundColor(Color.GREEN);
            }
            else {
                button.setBackgroundColor(Color.CYAN);
                button.setText("?");
            }
            button.setOnClickListener(view -> {
                if (fixed) return;
                ++val;
                if (val > 9) {
                    val = 1;
                }
                button.setText(String.valueOf(val));
                sudokuBoard[coords[0]][coords[1]] = val;
                if (isGameOver(sudokuBoard)) {
                    System.out.println("Game over!");
                }
            });
        }
    }

    boolean isInRange(int[][] board) {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if (board[i][j] <= 0 || board[i][j] > 9) {
                    return false;
                }
            }
        }
        return true;
    }

     boolean isGameOver(int[][] board) {
        if (!isInRange(board)) {
            return false;
        }

        boolean[] unique = new boolean[N + 1];

        for(int i = 0; i < N; i++) {
            Arrays.fill(unique, false);

            for(int j = 0; j < N; j++) {
                int Z = board[i][j];

                if (unique[Z]) {
                    return false;
                }
                unique[Z] = true;
            }
        }

        for(int i = 0; i < N; i++) {
            Arrays.fill(unique, false);

            for(int j = 0; j < N; j++) {
                int Z = board[j][i];

                if (unique[Z]) {
                    return false;
                }
                unique[Z] = true;
            }
        }

        for(int i = 0; i < N - 2; i += 3) {
            for(int j = 0; j < N - 2; j += 3) {
                Arrays.fill(unique, false);

                for(int k = 0; k < 3; k++) {
                    for(int l = 0; l < 3; l++) {
                        int X = i + k;
                        int Y = j + l;
                        int Z = board[X][Y];

                        if (unique[Z]) {
                            return false;
                        }
                        unique[Z] = true;
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Cell[][] table = new Cell[9][9];

        TableLayout tableLayout = new TableLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);

        for (int i = 0; i < sudokuBoard.length; ++i) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < sudokuBoard[i].length; ++j) {
                int[] coords = new int[2];
                coords[0] = i; coords[1] = j;
                table[i][j] = new Cell(sudokuBoard[i][j], this, coords);
                row.addView(table[i][j].button);
            }
            tableLayout.addView(row);
        }
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);

        linearLayout.addView(tableLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(linearLayout);
    }
}