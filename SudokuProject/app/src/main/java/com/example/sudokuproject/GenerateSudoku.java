package com.example.sudokuproject;

//ALGORITHM CREATED BY GeeksForGeeks

import java.lang.*;

public class GenerateSudoku {
    int[][] board;
    int numCells; // number of columns / rows
    int SRN; // square root of N
    int numUnknowns; // No. Of missing digits

    GenerateSudoku(int N, int K) {
        this.numCells = N;
        this.numUnknowns = K;

        SRN = (int) Math.sqrt(N);

        board = new int[N][N];

        fillDiagonal();

        fillRemaining(0, SRN);

        removeKDigits();
    }

    void fillDiagonal() {
        for (int i = 0; i < numCells; i = i + SRN) {
            fillBox(i, i);
        }
    }

    boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < SRN; ++i) {
            for (int j = 0; j < SRN; ++j) {
                if (board[rowStart + i][colStart + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    void fillBox(int row,int col) {
        int num;
        for (int i = 0; i < SRN; ++i) {
            for (int j = 0; j < SRN; ++j) {
                do {
                    num = randomGenerator(numCells);
                }
                while (!unUsedInBox(row, col, num));

                board[row + i][col + j] = num;
            }
        }
    }

    int randomGenerator(int num) {
        return (int) Math.floor((Math.random() * num + 1));
    }

    boolean CheckIfSafe(int i,int j,int num) {
        return (unUsedInRow(i, num) && unUsedInCol(j, num) && unUsedInBox(i - i % SRN, j - j % SRN, num));
    }

    boolean unUsedInRow(int i, int num) {
        for (int j = 0; j < numCells; ++j) {
            if (board[i][j] == num) {
                return false;
            }
        }
        return true;
    }

    boolean unUsedInCol(int j,int num) {
        for (int i = 0; i < numCells; ++i) {
            if (board[i][j] == num) {
                return false;
            }
        }
        return true;
    }

    boolean fillRemaining(int i, int j) {
        if (j >= numCells && i < numCells - 1) {
            ++i;
            j = 0;
        }

        if (i >= numCells && j >= numCells) {
            return true;
        }

        if (i < SRN) {
            if (j < SRN) {
                j = SRN;
            }
        }
        else if (i < numCells - SRN) {
            if (j == (int) (i / SRN) * SRN) {
                j += SRN;
            }
        }
        else {
            if (j == numCells - SRN) {
                ++i;
                j = 0;
                if (i >= numCells) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= numCells; ++num) {
            if (CheckIfSafe(i, j, num)) {
                board[i][j] = num;
                if (fillRemaining(i, j + 1)) {
                    return true;
                }
                board[i][j] = 0;
            }
        }

        return false;
    }

    void removeKDigits() {
        int count = numUnknowns;
        while (count != 0) {
            int cellId = randomGenerator(numCells * numCells) - 1;

            int i = cellId / numCells;
            int j = cellId % numCells;
            if (j != 0)
                --j;

            if (board[i][j] != 0) {
                --count;
                board[i][j] = 0;
            }
        }
    }

    public int[][] getSudoku() {
        return board;
    }
}