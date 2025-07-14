package team6;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import OMG.*;

/**
 * 게임의 메타 정보를 기록하고 관리하는 클래스입니다.
 */
public class GameLogger {

    // 내부적으로 각 수의 정보를 저장하는 클래스
    private static class MoveInfo {
        int moveNumber;
        String playerName;
        Point move;
        long timeTaken; // ms

        MoveInfo(int moveNumber, String playerName, Point move, long timeTaken) {
            this.moveNumber = moveNumber;
            this.playerName = playerName;
            this.move = move;
            this.timeTaken = timeTaken;
        }

        @Override
        public String toString() {
            return String.format("수 %d: %s가 (%d, %d)에 두었습니다. (소요 시간: %dms)",
                                 moveNumber, playerName, move.x, move.y, timeTaken);
        }
    }

    private int roundNumber;
    private String player1;
    private String player2;
    private List<MoveInfo> moveHistory;
    private long roundStartTime;

    /**
     * 생성자: 새로운 게임 라운드를 위해 로거를 초기화합니다.
     * @param roundNumber 현재 라운드 번호
     * @param player1     플레이어 1의 이름
     * @param player2     플레이어 2의 이름
     */
    public GameLogger(int roundNumber, String player1, String player2) {
        this.roundNumber = roundNumber;
        this.player1 = player1;
        this.player2 = player2;
        this.moveHistory = new ArrayList<>();
        this.roundStartTime = System.currentTimeMillis();
    }

    /**
     * 각 수를 기록합니다.
     * @param moveNumber 몇 번째 수인지
     * @param playerName 플레이어 이름
     * @param move       좌표
     * @param timeTaken  수를 두는 데 걸린 시간
     */
    public void logMove(int moveNumber, String playerName, Point move, long timeTaken) {
        moveHistory.add(new MoveInfo(moveNumber, playerName, move, timeTaken));
    }

    /**
     * 게임 라운드가 끝난 후 요약 정보를 출력합니다.
     * @param result 게임 결과 메시지
     */
    public void displaySummary(String result) {
        long roundEndTime = System.currentTimeMillis();
        long totalTime = roundEndTime - roundStartTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("\n====================== 게임 요약 ======================");
        System.out.println("라운드 번호: " + roundNumber);
        System.out.println("게임 일시: " + sdf.format(new Date(roundStartTime)));
        System.out.printf("총 게임 시간: %.2f초\n", totalTime / 1000.0);
        System.out.println("플레이어 1: " + player1);
        System.out.println("플레이어 2: " + player2);
        System.out.println("결과: " + result);
        System.out.println("\n--- 수순 기록 ---");
        for (MoveInfo info : moveHistory) {
            System.out.println(info);
        }
        System.out.println("====================================================\n");
    }
}