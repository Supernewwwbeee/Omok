package team6; // 패키지 변경

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class OmokClient {
    private static final int BOARD_SIZE = 19;
    private static int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    
    // 터미널에 보드 그리기
    private static void printBoard() {
        System.out.print("  ");
        for(int i=0; i<BOARD_SIZE; i++) System.out.printf("%2d", i);
        System.out.println();
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%2d", i);
            for (int j = 0; j < BOARD_SIZE; j++) {
                char c = '.';
                if (board[i][j] == 1) c = '●';
                if (board[i][j] == 2) c = '○';
                System.out.print(" " + c);
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        String hostname = "localhost"; // 서버 주소
        int port = 12345;               // 서버 포트

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner sc = new Scanner(System.in)) {

            System.out.println("서버에 접속했습니다.");
            System.out.println("서버 메시지: " + in.readLine()); // Welcome 메시지

            String fromServer;
            while (true) {
                printBoard();
                System.out.print("당신의 차례입니다. 좌표(x y) 입력 > ");
                int x = sc.nextInt();
                int y = sc.nextInt();

                board[x][y] = 1; // 흑돌
                out.println("MOVE:" + x + "," + y);

                fromServer = in.readLine();
                if(fromServer.startsWith("WIN")) {
                    printBoard();
                    System.out.println(fromServer.split(":")[1]);
                    break;
                }
                
                // 상대 수 처리
                if (fromServer.startsWith("OPPONENT_MOVE")) {
                    String[] coords = fromServer.split(":")[1].split(",");
                    int opX = Integer.parseInt(coords[0]);
                    int opY = Integer.parseInt(coords[1]);
                    board[opX][opY] = 2; // 백돌
                    System.out.printf("AI가 (%d, %d)에 두었습니다.\n", opX, opY);
                }
                
                fromServer = in.readLine();
                 if(fromServer.startsWith("LOSE")) {
                    printBoard();
                    System.out.println(fromServer.split(":")[1]);
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println(hostname + "에 접속할 수 없습니다.");
        }
    }
}