package ru.emelkrist;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {
    // ширина окна
    int boardWidth = 600;
    // высота окна
    int boardHeight = 700;
    // окно
    JFrame frame = new JFrame("Tic-Tac-Toe");
    // метка с информацией
    JLabel textLabel = new JLabel();
    // панель с меткой с информацией
    JPanel textPanel = new JPanel();
    // панель с игровым полем
    JPanel boardPanel = new JPanel();
    // кнопки игрового поля
    JButton[][] board = new JButton[3][3];
    // метка с очками игрока X
    JLabel playerXScoreLabel = new JLabel();
    // кнопка перезапуска игры
    JButton restartBtn = new JButton("Restart");
    // метка с очками игрока О
    JLabel playerOScoreLabel = new JLabel();
    // игровая панель с кнопкой рестарта и очками игроков
    JPanel gamePanel = new JPanel();
    // символ игрока Х
    String playerX = "X";
    // символ игрока О
    String playerO = "O";
    // очки игрока Х
    int playerXScore = 0;
    // очки игрока О
    int playerOScore = 0;
    // игрок с нынешним ходом
    String currentPlayer = playerX;
    // булева переменная окончания игры
    boolean gameOver = false;
    // булева переменная ничьей
    boolean tie = false;
    // количество ходов
    int turns = 0;

    TicTacToe() {
        // настройка главного окна
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        // настройка метки с информацией
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);
        // настройка панели с меткой с информацией
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
        // настройка панели с игровым полем
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);
        // настройка метки с очками игрока Х
        playerXScoreLabel.setBackground(Color.darkGray);
        playerXScoreLabel.setForeground(Color.white);
        playerXScoreLabel.setFont(new Font("Arial", Font.BOLD, 25));
        playerXScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        playerXScoreLabel.setText("X score: 0");
        playerXScoreLabel.setOpaque(true);
        // настройка кнопки рестарта
        restartBtn.setBackground(Color.WHITE);
        restartBtn.setContentAreaFilled(false);
        restartBtn.setOpaque(true);
        restartBtn.setFocusPainted(false);
        // настройка метки с очками игрока О
        playerOScoreLabel.setBackground(Color.darkGray);
        playerOScoreLabel.setForeground(Color.white);
        playerOScoreLabel.setFont(new Font("Arial", Font.BOLD, 25));
        playerOScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        playerOScoreLabel.setText("O score: 0");
        playerOScoreLabel.setOpaque(true);
        // настройка игровой панели
        gamePanel.setLayout(new GridLayout(1, 3));
        gamePanel.setBackground(Color.darkGray);
        gamePanel.add(playerXScoreLabel);
        gamePanel.add(restartBtn);
        gamePanel.add(playerOScoreLabel);
        frame.add(gamePanel, BorderLayout.SOUTH);
        // настройка кнопок игрового поля
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                // добавляем кнопки
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);
                // настраиваем свойства кнопок
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);
                // добавляем слушатели к кнопкам
                tile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;
                        makeMove((JButton) e.getSource());
                    }
                });
            }
        }

        // Слушатель к кнопке перезапуска игры
        restartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
    }

    /**
     * Метод для того, чтобы сделать ход.
     *
     * @param tile поле для хода (куда будет поставлен Х или О)
     */
    private void makeMove(JButton tile) {
        if ("".equals(tile.getText())) {
            // ставим метку в поле
            tile.setText(currentPlayer);
            turns++;
            checkWinner();
            // если игра закончилась НЕ ничьей, то в зависимости от победителя, даем ему одно очко
            if (gameOver) {
                if (!tie) {
                    if (currentPlayer == playerX) {
                        playerXScore++;
                        playerXScoreLabel.setText("X score: " + playerXScore);
                    } else {
                        playerOScore++;
                        playerOScoreLabel.setText("O score: " + playerOScore);
                    }
                }
            } else { // если игра продолжается, меняем игрока на того, кто должен делать новый ход
                currentPlayer = currentPlayer == playerX ? playerO : playerX;
                textLabel.setText(currentPlayer + "'s turn.");
            }
        }
    }

    /**
     * Метод для рестарта игры.
     */
    private void restartGame() {
        // очтистка игровых клеток
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = board[r][c];
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setText("");
            }
        }
        // откатываем основные значения к начальным
        currentPlayer = playerX;
        textLabel.setText("Tic-Tac-Toe");
        gameOver = false;
        tie = false;
        turns = 0;
    }


    /**
     * Метод для проверки наличия победителя.
     */
    private void checkWinner() {
        // проверка по горизонтали
        for (int r = 0; r < 3; r++) {
            if ("".equals(board[r][0].getText())) continue;
            if (board[r][0].getText().equals(board[r][1].getText())
                    && board[r][1].getText().equals(board[r][2].getText())) {
                for (int i = 0; i < 3; i++) {
                    // помечаем 3 победные клетки
                    setWinner(board[r][i]);
                }
                gameOver = true;
                return;
            }
        }

        // проверка по вертикале
        for (int c = 0; c < 3; c++) {
            if ("".equals(board[0][c].getText())) continue;
            if (board[0][c].getText().equals(board[1][c].getText())
                    && board[1][c].getText().equals(board[2][c].getText())) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][c]);
                }
                gameOver = true;
                return;
            }
        }

        // проверка по диагонали
        if (board[0][0].getText().equals(board[1][1].getText()) &&
                board[1][1].getText().equals(board[2][2].getText()) &&
                !"".equals(board[0][0].getText())) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][i]);
            }
            gameOver = true;
            return;
        }

        // проверка по анти-диагонали
        if (board[0][2].getText().equals(board[1][1].getText()) &&
                board[1][1].getText().equals(board[2][0].getText()) &&
                !"".equals(board[0][2].getText())) {
            setWinner(board[0][2]);
            setWinner(board[1][1]);
            setWinner(board[2][0]);
            gameOver = true;
            return;
        }

        /* если нигде не найдено победного сочетания и при этом количество
        совершенных ходов равно максимально возможному, значит ничья */
        if (turns == 9) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    // устанавливаем на поле ничью
                    setTie(board[r][c]);
                }
            }
            gameOver = true;
            tie = true;
        }

    }

    /**
     * Метод для установки для игровой клетки состояние ничьей.
     *
     * @param tile игровая клетка
     */
    private void setTie(JButton tile) {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        textLabel.setText("Tie!");
    }

    /**
     * Метод для установки для игровой клетки состояния победы.
     *
     * @param tile игровая клетка
     */
    private void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " is the winner!");
    }
}
