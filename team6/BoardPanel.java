package team6; // 패키지 변경

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import OMG.OMGPlayable; // OMG 패키지의 클래스를 import
import OMG.Point;       // OMG 패키지의 클래스를 import

/**
 * 오목판을 그리고 사용자 입력을 처리하는 패널 클래스입니다.
 */
public class BoardPanel extends JPanel {
    private final int BOARD_SIZE;
    private final int CELL_SIZE = 30; // 각 칸의 크기
    private final int PADDING = 30;   // 보드 가장자리 여백
    private int[][] boardState;       // 현재 보드 상태 (0: 빈칸, 1: 흑돌, 2: 백돌)
    private OmokGUI gameController;   // GUI 컨트롤러 참조

    public BoardPanel(int boardSize, OmokGUI controller) {
        this.BOARD_SIZE = boardSize;
        this.gameController = controller;
        this.boardState = new int[BOARD_SIZE][BOARD_SIZE];
        
        setBackground(new Color(239, 214, 153)); // 오목판 배경색

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 사용자가 클릭했을 때 컨트롤러에 알림
                int col = (e.getX() - PADDING + CELL_SIZE / 2) / CELL_SIZE;
                int row = (e.getY() - PADDING + CELL_SIZE / 2) / CELL_SIZE;
                gameController.handleHumanMove(new Point(row, col));
            }
        });
    }

    // 새로운 보드 상태로 업데이트
    public void updateBoard(int[][] newState) {
        this.boardState = newState;
        repaint(); // 패널을 다시 그리도록 요청
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 오목판 줄 그리기
        for (int i = 0; i < BOARD_SIZE; i++) {
            g.drawLine(PADDING, PADDING + i * CELL_SIZE, PADDING + (BOARD_SIZE - 1) * CELL_SIZE, PADDING + i * CELL_SIZE);
            g.drawLine(PADDING + i * CELL_SIZE, PADDING, PADDING + i * CELL_SIZE, PADDING + (BOARD_SIZE - 1) * CELL_SIZE);
        }

        // 돌 그리기
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (boardState[row][col] == OMGPlayable.blackStone) {
                    g.setColor(Color.BLACK);
                    g.fillOval(PADDING + col * CELL_SIZE - CELL_SIZE / 2, PADDING + row * CELL_SIZE - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
                } else if (boardState[row][col] == OMGPlayable.whiteStone) {
                    g.setColor(Color.WHITE);
                    g.fillOval(PADDING + col * CELL_SIZE - CELL_SIZE / 2, PADDING + row * CELL_SIZE - CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}