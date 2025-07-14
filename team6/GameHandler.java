//package team6; // 패키지 변경
//
//import java.io.*;
//import java.net.Socket;
//import OMG.OMGPlayable;   // OMG 패키지의 클래스를 import
//import OMG.OMGameUtil;    // OMG 패키지의 클래스를 import
//import OMG.Point;         // OMG 패키지의 클래스를 import
//import team6.Team6Player; // 서버 측 AI
//
//public class GameHandler extends Thread {
//    private Socket socket;
//    private PrintWriter out;
//    private BufferedReader in;
//    
//    private final int BOARD_SIZE = 19;
//    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
//    private OMGPlayable aiPlayer;
//
//    public GameHandler(Socket socket) {
//        this.socket = socket;
//        this.aiPlayer = new Team6Player(); // AI 플레이어 생성
//        this.aiPlayer.setBoardSizePlayerMode(BOARD_SIZE, OMGPlayable.whiteStone);
//    }
//
//    public void run() {
//        try {
//            out = new PrintWriter(socket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            out.println("WELCOME! 당신은 흑돌(1)입니다.");
//            
//            while (true) {
//                // 1. 클라이언트(인간)의 수를 받음
//                String clientMoveStr = in.readLine();
//                if (clientMoveStr == null || clientMoveStr.startsWith("QUIT")) break;
//                
//                String[] parts = clientMoveStr.split(":");
//                if (parts[0].equals("MOVE")) {
//                    String[] coords = parts[1].split(",");
//                    int x = Integer.parseInt(coords[0]);
//                    int y = Integer.parseInt(coords[1]);
//
//                    board[x][y] = OMGPlayable.blackStone;
//                    aiPlayer.rememberOpponentMove(new Point(x,y));
//
//                    // 승리 확인
//                    if (OMGameUtil.checkFinished(board, x, y, OMGPlayable.blackStone)) {
//                        out.println("WIN: 당신이 이겼습니다!");
//                        break;
//                    }
//
//                    // 2. AI의 수를 계산하여 클라이언트에 전송
//                    Point aiMove = aiPlayer.getUserMove(OMGPlayable.whiteStone);
//                    board[aiMove.getX()][aiMove.getY()] = OMGPlayable.whiteStone;
//                    out.println("OPPONENT_MOVE:" + aiMove.getX() + "," + aiMove.getY());
//
//                    // AI 승리 확인
//                    if (OMGameUtil.checkFinished(board, aiMove.getX(), aiMove.getY(), OMGPlayable.whiteStone)) {
//                        out.println("LOSE: AI가 이겼습니다.");
//                        break;
//                    }
//                }
//            }
//
//        } catch (IOException e) {
//            System.out.println("클라이언트와 연결이 끊어졌습니다.");
//        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {}
//        }
//    }
//}