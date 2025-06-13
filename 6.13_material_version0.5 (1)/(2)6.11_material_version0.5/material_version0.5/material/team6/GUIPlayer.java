package team6;

import OMG.OMGPlayable;
import OMG.Point;
import java.util.concurrent.BlockingQueue;

/**
 * GUI 입력을 처리하기 위한 플레이어 클래스. (OMGame 수정을 피하기 위해 static으로 통신)
 */
public class GUIPlayer implements OMGPlayable {

    // GUI와 게임 스레드 간의 통신 채널 (static으로 선언)
    private static BlockingQueue<Point> moveQueue;

    /**
     * OmokGUI가 게임 시작 전에 호출하여 통신 채널을 설정하는 static 메소드
     */
    public static void setMoveQueue(BlockingQueue<Point> queue) {
        GUIPlayer.moveQueue = queue;
    }

    /**
     * OMGame 클래스가 reflection을 통해 생성할 수 있도록 public 기본 생성자 필수
     */
    public GUIPlayer() {
    }

    @Override
    public Point getUserMove(int playermode) {
        if (moveQueue == null) {
            throw new IllegalStateException("GUIPlayer의 통신 큐(Queue)가 설정되지 않았습니다.");
        }
        try {
            // 큐에 좌표가 들어올 때까지 대기 (사용자 클릭 대기)
            return moveQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Point(-1, -1); // 오류 발생 시 기권 처리
        }
    }

    @Override
    public void setBoardSizePlayerMode(int _boardSize, int _playerMode) { }

    @Override
    public String getYourGroupName() {
        return "Human (GUI Player)";
    }

    @Override
    public void rememberOpponentMove(Point oppmove) { }

    @Override
    public void setRoundScore(int _score) { }
}