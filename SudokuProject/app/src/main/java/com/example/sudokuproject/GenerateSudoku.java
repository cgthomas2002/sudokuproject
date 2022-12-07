package com.example.sudokuproject;

import java.lang.*;

public class GenerateSudoku {
    int[][] board;
    int numCells; // number of columns / rows
    int sqrtNumCells; // square root of numCells
    int numUnknowns; // number of unknown numbers

    GenerateSudoku(int N, int K) {
        this.numCells = N;
        this.numUnknowns = K;

        System.out.println(numUnknowns);

        sqrtNumCells = (int) Math.sqrt(N);

        board = new int[N][N];

        fillDiagonal();

        fillRemaining(0, sqrtNumCells);

        createUnknowns();
    }

    void fillDiagonal() {
        for (int i = 0; i < numCells; i = i + sqrtNumCells) {
            fillBox(i, i);
        }
    }

    boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < sqrtNumCells; ++i) {
            for (int j = 0; j < sqrtNumCells; ++j) {
                if (board[rowStart + i][colStart + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    void fillBox(int row,int col) {
        int num;
        for (int i = 0; i < sqrtNumCells; ++i) {
            for (int j = 0; j < sqrtNumCells; ++j) {
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

    boolean checkIfSafe(int i,int j,int num) {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i % sqrtNumCells, j - j % sqrtNumCells, num));
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

        if (i < sqrtNumCells) {
            if (j < sqrtNumCells) {
                j = sqrtNumCells;
            }
        }
        else if (i < numCells - sqrtNumCells) {
            if (j == (int) (i / sqrtNumCells) * sqrtNumCells) {
                j += sqrtNumCells;
            }
        }
        else {
            if (j == numCells - sqrtNumCells) {
                ++i;
                j = 0;
                if (i >= numCells) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= numCells; ++num) {
            if (checkIfSafe(i, j, num)) {
                board[i][j] = num;
                if (fillRemaining(i, j + 1)) {
                    return true;
                }
                board[i][j] = 0;
            }
        }

        return false;
    }

    void createUnknowns() {
        int count = numUnknowns;
        while (count != 0) {
            int cellId = randomGenerator(numCells * numCells) - 1;

            int i = cellId / numCells;
            int j = cellId % numCells;

            if (j != 0) {
                --j;
            }

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