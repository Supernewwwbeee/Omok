package team6;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import OMG.OMGame;
import OMG.OMGPlayable;
import OMG.Point;

public class OmokGUI extends JFrame {
    private final int BOARD_SIZE = 19;
    private BoardPanel boardPanel;
    private JLabel statusLabel;
    private JTextArea logArea;
    private final BlockingQueue<Point> humanMoveQueue = new LinkedBlockingQueue<>(1);

    /**
     * [수정] 생성자에서 플레이어의 돌 색상을 인자로 받습니다.
     * @param humanPlayerMode 사용자가 선택한 돌 (1: 흑돌, 2: 백돌)
     */
    public OmokGUI(int humanPlayerMode) {
        setupUI();
        redirectSystemStreams();
        startGameThread(humanPlayerMode); // 사용자의 선택을 게임 스레드에 전달
    }

    private void setupUI() {
        setTitle("오목 게임 (OMGame 원본 사용)");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(BOARD_SIZE, this);
        statusLabel = new JLabel("게임을 시작합니다...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setPreferredSize(new Dimension(250, 0));

        add(logScrollPane, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * [수정] 게임 시작 스레드가 사용자의 돌 선택을 인자로 받도록 변경
     * @param humanPlayerMode 사용자가 선택한 돌
     */
    private void startGameThread(int humanPlayerMode) {
        GUIPlayer.setMoveQueue(humanMoveQueue);

        Thread gameThread = new Thread(() -> {
            OMGame omokGame = new OMGame(BOARD_SIZE);

            // 사용자의 선택에 따라 플레이어 순서를 결정하여 초기화
            if (humanPlayerMode == OMGPlayable.blackStone) {
                SwingUtilities.invokeLater(() -> logArea.append("당신이 흑돌, AI가 백돌입니다.\n"));
                omokGame.initializePlayer("team6.GUIPlayer", "team6.Team6Player");
            } else {
                SwingUtilities.invokeLater(() -> logArea.append("AI가 흑돌, 당신이 백돌입니다.\n"));
                omokGame.initializePlayer("team6.Team6Player", "team6.GUIPlayer");
            }
            
            // OMGame의 게임 루프 시작
            omokGame.play(0);
        });
        gameThread.start();
    }

    public void handleHumanMove(Point move) {
        if (humanMoveQueue.isEmpty()) {
            humanMoveQueue.offer(move);
        }
    }

    private void redirectSystemStreams() {
        Thread listenerThread = new Thread(() -> {
            try {
                PipedInputStream pipedInputStream = new PipedInputStream();
                PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
                System.setOut(new PrintStream(pipedOutputStream, true));

                BufferedReader reader = new BufferedReader(new InputStreamReader(pipedInputStream));
                List<String> boardLinesBuffer = new ArrayList<>();
                boolean isBoardReading = false;

                String line;
                while ((line = reader.readLine()) != null) {
                    final String currentLine = line;

                    if (line.contains("┌") || (isBoardReading && line.matches("^[|├└].*"))) {
                        if (line.contains("┌")) {
                            isBoardReading = true;
                            boardLinesBuffer.clear();
                        }
                        boardLinesBuffer.add(line);
                        if (line.contains("└")) {
                            isBoardReading = false;
                            updateBoardFromText(boardLinesBuffer);
                        }
                    } else {
                        if (line.contains("차례입니다") || line.contains("승리") || line.contains("무승부")) {
                            SwingUtilities.invokeLater(() -> statusLabel.setText(currentLine));
                        }
                        SwingUtilities.invokeLater(() -> {
                            logArea.append(currentLine + "\n");
                            logArea.setCaretPosition(logArea.getDocument().getLength());
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void updateBoardFromText(List<String> boardLines) {
        int[][] newBoardState = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < boardLines.size() && i < BOARD_SIZE; i++) {
            String row = boardLines.get(i);
            for (int j = 0; j < row.length() && j < BOARD_SIZE; j++) {
                char c = row.charAt(j);
                if (c == '●') {
                    newBoardState[i][j] = 1;
                } else if (c == '○') {
                    newBoardState[i][j] = 2;
                } else {
                    newBoardState[i][j] = 0;
                }
            }
        }
        SwingUtilities.invokeLater(() -> boardPanel.updateBoard(newBoardState));
    }

    /**
     * [수정] main 메소드가 프로그램의 시작점이 되어 돌 선택을 먼저 처리합니다.
     */
    public static void main(String[] args) {
        // GUI 시작 전, 돌 선택 다이얼로그를 먼저 띄움
        Object[] options = {"흑돌 (먼저 두기)", "백돌 (나중에 두기)"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "플레이할 돌을 선택하세요:",
            "돌 선택",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        // 사용자가 창을 닫으면 프로그램 종료
        if (choice == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        // 선택 결과에 따라 플레이어 모드 결정 (흑돌: 1, 백돌: 2)
        final int humanPlayerMode = (choice == 0) ? OMGPlayable.blackStone : OMGPlayable.whiteStone;

        // 사용자의 선택을 바탕으로 GUI 생성
        SwingUtilities.invokeLater(() -> {
            OmokGUI game = new OmokGUI(humanPlayerMode);
            game.setVisible(true);
        });
    }
}