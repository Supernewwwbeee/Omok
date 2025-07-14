package team6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import OMG.*;

public class NetworkPlayer implements OMGPlayable {
    private String name;
    private BufferedReader in;
    private PrintWriter out;
    private int boardSize;

    public NetworkPlayer(String name, BufferedReader in, PrintWriter out) {
        this.name = name;
        this.in = in;
        this.out = out;
    }

    @Override
    public String getYourGroupName() {
        return this.name;
    }

    @Override
    public void setBoardSizePlayerMode(int boardSize, int playerMode) {
        this.boardSize = boardSize;
    }

    @Override
    public Point getUserMove(int playerMode) {
        try {
            // 클라이언트로부터 "MOVE x,y" 형식의 메시지를 기다림
            String line = in.readLine();
            if (line != null && line.startsWith("MOVE")) {
                String[] parts = line.substring(5).split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                return new Point(x, y);
            }
        } catch (IOException e) {
            System.out.println("NetworkPlayer: 클라이언트로부터 수신 오류 - " + e.getMessage());
        }
        // 오류 발생 시 유효하지 않은 좌표 반환
        return new Point(-1, -1);
    }

    @Override
    public void rememberOpponentMove(Point move) {
        // 이 메소드는 서버의 GameHandler가 직접 클라이언트로 메시지를 보내므로 비워둠
    }

    @Override
    public void setRoundScore(int score) {
        // 서버에서는 점수를 별도로 관리하지 않음
    }
}
