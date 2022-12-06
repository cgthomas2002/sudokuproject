package com.example.sudokuproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    int numCells = 9; //number of rows/columns
    int numUnknowns = 2; //number of unknown numbers

    int selectedNum;

    TableLayout tableLayout;
    LinearLayout linearLayout;
    LinearLayout selectedButtonsLayout;

    int[][] sudokuBoard;

    void onSelectedClick(View view) {
        resetSelectedNums();

        Button button = (Button) view;

        int val = selectedButtonsLayout.indexOfChild(view) + 1;

        selectedNum = val;

        button.setBackground(getResources().getDrawable(R.drawable.unknowncellbackground));
    }

    void resetSelectedNums() {
        selectedNum = 0;

        for (int i = 0; i < selectedButtonsLayout.getChildCount(); ++i) {
            Button button = (Button) selectedButtonsLayout.getChildAt(i);

            button.setBackground(getResources().getDrawable(R.drawable.selectbackground));
        }
    }

    void onCellClick(View view) {
        if (selectedNum == 0) {
            return;
        }

        TableRow row = (TableRow) view.getParent();
        Button button = (Button) view;

        int i = tableLayout.indexOfChild(row);
        int j = row.indexOfChild(button);

        sudokuBoard[i][j] = selectedNum;
        button.setText(String.valueOf(selectedNum));

        resetSelectedNums();

        if (isGameOver(sudokuBoard)) {
            resetBoard();
        }
    }

    void setGameState() {
        for (int i = 0; i < tableLayout.getChildCount(); ++i) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); ++j) {
                Button button = (Button) row.getChildAt(j);

                int val = sudokuBoard[i][j];
                boolean fixed = (val != 0 && true) || false;

                if (!fixed) {
                    button.setText("");
                    button.setBackground(getResources().getDrawable(R.drawable.unknowncellbackground));
                    button.setOnClickListener(this::onCellClick);
                }
                else {
                    button.setText(String.valueOf(val));
                    button.setBackground(getResources().getDrawable(R.drawable.cellbackground));
                    button.setOnClickListener(null);
                }
            }
        }
    }

    void createBoard() {
        GenerateSudoku gen = new GenerateSudoku(numCells, numUnknowns);
        sudokuBoard = gen.getSudoku();
    }

    void resetBoard() {
        createBoard();
        setGameState();
    }

    void showHelp() {

    }

    boolean isInRange(int[][] board) {
        for(int i = 0; i < numCells; ++i) {
            for(int j = 0; j < numCells; ++j) {
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

        boolean[] unique = new boolean[numCells + 1];

        for(int i = 0; i < numCells; ++i) {
            Arrays.fill(unique, false);

            for(int j = 0; j < numCells; ++j) {
                int Z = board[i][j];

                if (unique[Z]) {
                    return false;
                }
                unique[Z] = true;
            }
        }

        for(int i = 0; i < numCells; ++i) {
            Arrays.fill(unique, false);

            for(int j = 0; j < numCells; ++j) {
                int Z = board[j][i];

                if (unique[Z]) {
                    return false;
                }
                unique[Z] = true;
            }
        }

        for(int i = 0; i < numCells - 2; i += 3) {
            for(int j = 0; j < numCells - 2; j += 3) {
                Arrays.fill(unique, false);

                for(int k = 0; k < 3; ++k) {
                    for(int l = 0; l < 3; ++l) {
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

    public int[] flattenMatrix(int[][] matrix) {
        int n = (int) Math.pow(matrix.length, 2);

        int[] values = new int[n];
        int count = 0;
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                values[count] = matrix[i][j];
                ++count;
            }
        }

        return values;
    }

    public int[][] unflattenArray(int[] array) {
        int n = (int) Math.sqrt(array.length);

        int[][] matrix = new int[n][n];

        for (int k = 0; k < array.length; ++k) {
            int i = k / n;
            int j = k % n;

            matrix[i][j] = array[k];
        }

        return matrix;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        int[] values = flattenMatrix(sudokuBoard);
        savedInstanceState.putIntArray("savedBoard", values);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        linearLayout = findViewById(R.id.linearLayout);
        selectedButtonsLayout = findViewById(R.id.selectButtonsGroup);

        if (savedInstanceState != null) {
            int[] savedBoard = savedInstanceState.getIntArray("savedBoard");
            sudokuBoard = unflattenArray(savedBoard);
        }
        else {
            createBoard();
        }

        setGameState();

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {

        });

        Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(view -> {
            showHelp();
        });

        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(view -> {
            resetBoard();
        });

        for (int i = 0; i < selectedButtonsLayout.getChildCount(); ++i) {
            Button button = (Button) selectedButtonsLayout.getChildAt(i);

            button.setOnClickListener(this::onSelectedClick);
        }
    }
}