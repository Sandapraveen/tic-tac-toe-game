package com.tictactoe;

public class Game {
    public char[][] board;
    public char currentPlayer;
    public boolean gameOver;
    private int[][] winningCombo;

    public Game() {
        board = new char[3][3];
        currentPlayer = 'X';
        gameOver = false;
        winningCombo = new int[3][2];
        initializeBoard();
    }

    public void initializeBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = '-';
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] == '-' && !gameOver) {
            board[row][col] = currentPlayer;
            if (checkWin(currentPlayer)) {
                gameOver = true;
                return true;
            } else if (checkDraw()) {
                gameOver = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
            return true;
        }
        return false;
    }

    public void undoMove(int row, int col) {
        // Only undo if the position isn't already empty
        if (board[row][col] != '-') {
            board[row][col] = '-';
            // Switch the player back since we're undoing a move
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            // Since we're undoing, the game is no longer over
            gameOver = false;
            // Reset winning combo as it might have been set
            winningCombo = new int[3][2];
        }
    }

    public boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                winningCombo = new int[][] {{i, 0}, {i, 1}, {i, 2}};
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                winningCombo = new int[][] {{0, j}, {1, j}, {2, j}};
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            winningCombo = new int[][] {{0, 0}, {1, 1}, {2, 2}};
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            winningCombo = new int[][] {{0, 2}, {1, 1}, {2, 0}};
            return true;
        }

        return false;
    }

    public boolean checkDraw() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == '-') return false;
        return true;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int[][] getWinningCombo() {
        return winningCombo;
    }
}