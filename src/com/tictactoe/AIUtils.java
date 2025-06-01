package com.tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameU  extends JFrame {
    private Game game;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JComboBox<String> difficultyBox;
    private int playerXScore = 0;
    private int playerOScore = 0;
    private JLabel scoreLabel;

    public GameUI() {
        game = new Game();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        createMenuBar();
        createTopPanel();
        createGameGrid();
        createBottomPanel();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save Game");
        saveItem.addActionListener(e -> saveGame());
        JMenuItem loadItem = new JMenuItem("Load Game");
        loadItem.addActionListener(e -> loadGame());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(statusLabel, BorderLayout.CENTER);

        difficultyBox = new JComboBox<>(new String[]{"Easy", "Hard"});
        topPanel.add(difficultyBox, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    private void createGameGrid() {
        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton("-");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 36));
                buttons[i][j].setFocusPainted(false);
                gridPanel.add(buttons[i][j]);
                buttons[i][j].addActionListener(e -> {
                    if (!game.isGameOver() && game.getCell(row, col) == '-') {
                        playSound("click.wav");
                        game.makeMove(row, col);
                        updateBoard();

                        if (!game.isGameOver() && game.getCurrentPlayer() == 'O') {
                            performAIMove();
                        }
                    }
                });
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        scoreLabel = new JLabel("Score - X: " + playerXScore + " | O: " + playerOScore, SwingConstants.CENTER);
        bottomPanel.add(scoreLabel, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());
        bottomPanel.add(resetButton, BorderLayout.EAST);

        JButton resetScoreButton = new JButton("Reset Score");
        resetScoreButton.addActionListener(e -> resetScore());
        bottomPanel.add(resetScoreButton, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(String.valueOf(game.getCell(i, j)));
                buttons[i][j].setEnabled(!game.isGameOver() && game.getCell(i, j) == '-');
            }
        }

        if (game.isGameOver()) {
            if (game.checkWin('X')) {
                highlightWinningCombo();
                statusLabel.setText("Player X wins!");
                playerXScore++;
                playSound("win.wav");
            } else if (game.checkWin('O')) {
                highlightWinningCombo();
                statusLabel.setText("Player O wins!");
                playerOScore++;
                playSound("win.wav");
            } else {
                statusLabel.setText("It's a draw!");
                playSound("draw.wav");
            }
            scoreLabel.setText("Score - X: " + playerXScore + " | O: " + playerOScore);
        } else {
            statusLabel.setText("Player " + game.getCurrentPlayer() + "'s turn");
        }
    }

    private void highlightWinningCombo() {
        int[][] combo = game.getWinningCombo();
        if (combo != null) {
            for (int[] pos : combo) {
                buttons[pos[0]][pos[1]].setBackground(Color.GREEN);
            }
        }
    }

    private void resetGame() {
        game = new Game();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("-");
                buttons[i][j].setBackground(null);
                buttons[i][j].setEnabled(true);
            }
        }
        statusLabel.setText("Player X's turn");
    }

    private void resetScore() {
        playerXScore = 0;
        playerOScore = 0;
        scoreLabel.setText("Score - X: 0 | O: 0");
    }

    private void performAIMove() {
        int[] move;
        String difficulty = (String) difficultyBox.getSelectedItem();
        if ("Easy".equals(difficulty)) {
            move = AIUtils.getRandomMove(game);
        } else {
            move = AIUtils.getBestMove(game, 'O'); // Pass the AI player character
        }
        if (move != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.makeMove(move[0], move[1]);
            playSound("click.wav");
            updateBoard();
        }
    }

    private void playSound(String filename) {
        try {
            File soundFile = new File(filename);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception ex) {
            System.out.println("Error playing sound: " + ex.getMessage());
        }
    }

    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("gamestate.ser"))) {
            out.writeObject(game);
            JOptionPane.showMessageDialog(this, "Game saved successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage());
        }
    }

    private void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("gamestate.ser"))) {
            game = (Game) in.readObject();
            updateBoard();
            JOptionPane.showMessageDialog(this, "Game loaded successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No saved game found.");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading game: Invalid save file.");
        }
    }

    public static void main(String[] args) {
        new GameUI();
    }
}