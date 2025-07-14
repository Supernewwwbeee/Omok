package team6;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner consoleScanner = new Scanner(System.in)
        ) {
            System.out.println("서버에 연결되었습니다.");
            
            // 서버로부터 메시지를 수신하고 처리하는 리스너 스레드
            Thread listenerThread = new Thread(() -> {
                try {
                    String fromServer;
                    while ((fromServer = in.readLine()) != null) {
                        if (fromServer.equals("BOARD_START")) {
                            System.out.println("\n===== 현재 오목판 =====");
                            while (!(fromServer = in.readLine()).equals("BOARD_END")) {
                                System.out.println(fromServer);
                            }
                             System.out.println("=====================");
                        } else if (fromServer.equals("YOUR_TURN")) {
                            System.out.print("당신의 차례입니다. 좌표를 입력하세요 (예: 7 7) > ");
                        } else if (fromServer.startsWith("OPPONENT_MOVED")) {
                            System.out.println("상대(AI)가 수를 두었습니다: " + fromServer.substring(15));
                        } else if (fromServer.equals("VICTORY")) {
                            System.out.println("\n*** 축하합니다! 승리하셨습니다! ***");
                            break;
                        } else if (fromServer.equals("DEFEAT")) {
                            System.out.println("\n--- 아쉽지만 패배하셨습니다. ---");
                            break;
                        } else if (fromServer.equals("TIE")) {
                            System.out.println("\n--- 무승부입니다. ---");
                            break;
                        } else if (fromServer.equals("INVALID_MOVE")) {
                            System.out.println("잘못된 수입니다. 다시 입력해주세요.");
                        } else {
                            System.out.println("서버: " + fromServer);
                        }
                    }
                } catch (IOException e) {
                     System.out.println("서버와의 연결이 끊어졌습니다.");
                }
            });
            listenerThread.start();
            
            // 사용자 입력을 서버로 전송
            while (listenerThread.isAlive()) {
                if (consoleScanner.hasNextLine()) {
                    String userInput = consoleScanner.nextLine();
                    String[] parts = userInput.trim().split("\\s+");
                    if (parts.length == 2) {
                        try {
                            int x = Integer.parseInt(parts[0]);
                            int y = Integer.parseInt(parts[1]);
                            out.println("MOVE " + x + "," + y);
                        } catch (NumberFormatException e) {
                            System.out.println("숫자 형식으로 좌표를 입력해주세요 (예: 7 7).");
                        }
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("서버를 찾을 수 없습니다: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("서버에 연결할 수 없습니다: " + e.getMessage());
        }
    }
}