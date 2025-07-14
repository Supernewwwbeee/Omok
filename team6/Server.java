package team6; // 패키지 변경

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
import java.net.*;
import OMG.*;

public class Server {
    private static final int PORT = 12345;
    private static final int BOARD_SIZE = 19; 

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("오목 서버가 시작되었습니다. 포트: " + PORT);
            System.out.println("클라이언트의 연결을 기다립니다...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트가 연결되었습니다: " + clientSocket.getInetAddress());
                // 새 클라이언트를 위해 게임 핸들러 스레드 시작
                new GameHandler(clientSocket, BOARD_SIZE).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class GameHandler extends Thread {
    private Socket clientSocket;
    private int boardSize;
    private PrintWriter out;
    private BufferedReader in;

    private char[][] board;

    public GameHandler(Socket socket, int boardSize) {
        this.clientSocket = socket;
        this.boardSize = boardSize;
        this.board = new char[boardSize][boardSize];
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // 플레이어 설정: 클라이언트는 흑돌, 서버 AI는 백돌
            OMGPlayable clientPlayer = new NetworkPlayer("ClientPlayer", in, out);
            OMGPlayable aiPlayer = new Team6Player();
            
            clientPlayer.setBoardSizePlayerMode(boardSize, OMGPlayable.blackStone);
            aiPlayer.setBoardSizePlayerMode(boardSize, OMGPlayable.whiteStone);

            initializeLocalBoard();
            
            int[][] save = new int[boardSize][boardSize];
            boolean[][] chk = new boolean[boardSize][boardSize];
            int turnCount = 1;

            out.println("WELCOME BLACK");
            printBoardToClient();

            while (true) {
                Point move;
                int currentPlayerStone;
                String currentPlayerName;

                // 턴에 따라 플레이어 결정
                if (turnCount % 2 == 1) { // 클라이언트(흑돌) 턴
                    currentPlayerStone = OMGPlayable.blackStone;
                    currentPlayerName = clientPlayer.getYourGroupName();
                    out.println("YOUR_TURN");
                    move = clientPlayer.getUserMove(currentPlayerStone);
                } else { // 서버 AI(백돌) 턴
                    currentPlayerStone = OMGPlayable.whiteStone;
                    currentPlayerName = aiPlayer.getYourGroupName();
                    move = aiPlayer.getUserMove(currentPlayerStone);
                    // AI의 수를 클라이언트에게 알림
                    out.println("OPPONENT_MOVED " + move.getX() + "," + move.getY());
                }
                
                int x = move.getX();
                int y = move.getY();

                // 유효성 검사
                if (x < 0 || x >= boardSize || y < 0 || y >= boardSize || chk[x][y]) {
                    out.println("INVALID_MOVE");
                    continue; // 턴을 다시 진행
                }
                
                // 상대방에게 수 전달
                if(currentPlayerStone == OMGPlayable.blackStone) {
                    aiPlayer.rememberOpponentMove(move);
                } else {
                    clientPlayer.rememberOpponentMove(move);
                }

                // 보드 업데이트
                save[x][y] = currentPlayerStone;
                chk[x][y] = true;
                board[x][y] = (currentPlayerStone == OMGPlayable.blackStone) ? '●' : '○';

                printBoardToClient();

                // 게임 종료 조건 확인
                if (OMGameUtil.checkFinished(save, x, y, currentPlayerStone)) {
                    if (currentPlayerStone == OMGPlayable.blackStone) {
                        out.println("VICTORY");
                    } else {
                        out.println("DEFEAT");
                    }
                    System.out.println(currentPlayerName + " 승리!");
                    break;
                }
                
                // 무승부 확인
                if (turnCount == boardSize * boardSize) {
                    out.println("TIE");
                    System.out.println("무승부!");
                    break;
                }

                turnCount++;
            }

        } catch (IOException e) {
            System.out.println("클라이언트와의 연결이 끊어졌습니다: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void initializeLocalBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = '┼';
            }
        }
    }

    private void printBoardToClient() {
        out.println("BOARD_START");
        for (int i = 0; i < boardSize; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < boardSize; j++) {
                row.append(board[i][j]).append(" ");
            }
            out.println(row.toString().trim());
        }
        out.println("BOARD_END");
    }
}