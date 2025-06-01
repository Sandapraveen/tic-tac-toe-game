package com.tictactoe;

public class Player {
    private char symbol;
    private boolean isComputer;

    public Player(char symbol, boolean isComputer) {
        this.symbol = symbol;
        this.isComputer = isComputer;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isComputer() {
        return isComputer;
    }

    public void setComputer(boolean computer) {
        isComputer = computer;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
} 
