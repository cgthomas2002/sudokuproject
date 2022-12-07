package com.example.sudokuproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int numCells = 9; //number of rows/columns
    int numUnknowns = 20; //number of unknown numbers

    String currentDifficulty = "normal";
    Boolean isDark = false;

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
            Toast.makeText(this, "Game over!", Toast.LENGTH_LONG).show();

            /*try {
                Thread.sleep(3000);
            } catch(InterruptedException e) {
                System.out.println("got interrupted!");
            }*/

           // resetBoard();
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

    void setBoardTheme() {
        if (isDark) {

        }
        else {

        }
    }

    void createBoard() {
        GenerateSudoku gen = new GenerateSudoku(numCells, numUnknowns);
        sudokuBoard = gen.getSudoku();
    }

    void resetBoard() {
        if (currentDifficulty == "easy") {
            numUnknowns = 10;
        }
        else if (currentDifficulty == "normal") {
            numUnknowns = 20;
        }
        else if (currentDifficulty == "hard") {
            numUnknowns = 30;
        }
        else if (currentDifficulty == "demo") {
            numUnknowns = 2;
        }
        createBoard();
        setGameState();
    }

    void showHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("difficulty", currentDifficulty);
        intent.putExtra("isDark", isDark);
        settingsResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> settingsResultLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                String newDifficulty = data.getStringExtra("difficulty");
                isDark = data.getBooleanExtra("isDark", false);

                if (!newDifficulty.equals(currentDifficulty)) {
                    currentDifficulty = newDifficulty;
                    resetBoard();
                }

                setBoardTheme();
            }
        }
    });

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

       // savedInstanceState.putString("difficulty", currentDifficulty);

       // savedInstanceState.putBoolean("isDark", isDark);
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

           // currentDifficulty = savedInstanceState.getString("difficulty");
           // isDark = savedInstanceState.getBoolean("isDark");
        }
        else {
            createBoard();

           // currentDifficulty = "normal";
           // isDark = false;
        }

        setGameState();

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            showSettings();
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