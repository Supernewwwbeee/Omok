package team6; // 패키지 변경

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class OmokServer {
    public static void main(String[] args) {
        int port = 12345; // 사용할 포트 번호
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("오목 서버(team6)가 시작되었습니다. 포트: " + port);
            while (true) {
                // 클라이언트 접속 대기
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트 접속: " + clientSocket.getInetAddress());
                // 각 클라이언트에 대해 별도의 게임 핸들러 스레드 시작
                new GameHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}