package team6;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import OMG.*;
/**
 * MiniMaxPlayer5은 OMGPlayable을 구현한 클래스로, Minimax 알고리즘을 사용하여 오목을 하는 플레이어입니다.
 */
public class Team6Player implements OMGPlayable, OMG33Testable {

    private int BoardSize = 19; // 오목판의 크기
    private int myPlayerMode; // 플레이어 모드 (1: 흑돌, 2: 백돌)
    private int RoundScore = 0; // 라운드 점수
    private Random rn; // 랜덤 객체
    private int[][] board; // 오목판
    private static final int WIN_SCORE = 100_000_000; // 승리 점수
    public static int evaluationCount = 0; // 평가 횟수
    private static final int MINIMAX_TIME_LIMIT = 1200; // Minimax 알고리즘 시간 제한 (1200밀리초)
    private static final int OPTIMAL_POINT_TIME_LIMIT = 200; // calculateOptimalPoint 알고리즘 시간 제한 (200밀리초)
    private static final int MINIMAX_DEPTH = 2; // Minimax 알고리즘 깊이

    /**
     * MiniMaxPlayer5의 생성자
     */
    public Team6Player() {
        rn = new Random();
        rn.setSeed(System.currentTimeMillis());
        initializeBoard();
    }

    /**
     * 오목판을 초기화합니다.
     */
    private void initializeBoard() {
        board = new int[BoardSize][BoardSize];
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                board[i][j] = 0;
            }
        }
    }

    /**
     * 오목판 크기와 플레이어 모드를 설정합니다.
     * @param _boardSize 오목판 크기
     * @param _playerMode 플레이어 모드
     */
    public void setBoardSizePlayerMode(int _boardSize, int _playerMode) {
        this.BoardSize = _boardSize;
        this.myPlayerMode = _playerMode;
        initializeBoard();
    }

    /**
     * 플레이어 그룹 이름을 반환합니다.
     * @return 플레이어 그룹 이름
     */
    public String getYourGroupName() {
        return "testteam.MiniMaxPlayer5";
    }

    /**
     * 사용자의 돌을 놓습니다.
     * @param playermode 플레이어 모드
     * @return 돌을 놓을 위치
     */
    public Point getUserMove(int playermode) {
        if (playermode != myPlayerMode) {
            System.out.println("[Exception] Critical. Player Mode does not match in " + this.getYourGroupName());
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Point pt = null;
        long startTime = System.currentTimeMillis(); // 돌을 놓은 시간 측정

        try {
            // MINIMAX_TIME_LIMIT 안에 Minimax 알고리즘으로 수행
            pt = getBestMoveWithinTimeLimit(executor);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            pt = calculateRandomPoint();
        } finally {
            executor.shutdownNow();
        }

        logMoveDetails(pt, startTime);
        board[pt.getX()][pt.getY()] = (myPlayerMode == 1) ? 1 : 2;
        
        return pt;
    }

    private Point getBestMoveWithinTimeLimit(ExecutorService executor) throws InterruptedException, ExecutionException {
        Point pt;
        Future<Point> minimaxFuture = executor.submit(() -> {
            Point result;
            if (getStoneNum() <= 0) {
                int center = BoardSize / 2;
                result = new Point(center, center);
            } else {
                result = runMinimaxWithThreeCheck(MINIMAX_TIME_LIMIT);
            }
            return result;
        });

        try {
            pt = minimaxFuture.get(MINIMAX_TIME_LIMIT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            minimaxFuture.cancel(true);
            pt = getOptimalPointWithinTimeLimit(executor);
        }
        return pt;
    }

    private Point runMinimaxWithThreeCheck(long timeLimit) {
        long startTime = System.currentTimeMillis();
        Point bestMove = calculateMinimaxMove(MINIMAX_DEPTH);
        int[][] tempBoard = copyBoard(board); // 현재 보드의 복사본 생성
        CheckResult.ThreeCheckResult threeResult = CheckResult.checkThree(tempBoard, bestMove.getX(), bestMove.getY(), myPlayerMode);
        while (threeResult.Three_count >= 2) {
            if (System.currentTimeMillis() - startTime > timeLimit) {
                break;
            }
            tempBoard[bestMove.getX()][bestMove.getY()] = 3; // 의미 없는 돌을 놓은 후 보드 업데이트
            bestMove = calculateMinimaxMove(MINIMAX_DEPTH); // 새로운 최적의 돌 위치 계산
            threeResult = CheckResult.checkThree(tempBoard, bestMove.getX(), bestMove.getY(), myPlayerMode);
        }
        return bestMove;
    }



    private Point getOptimalPointWithinTimeLimit(ExecutorService executor) throws InterruptedException, ExecutionException {
        Point pt = null;
        Future<Point> optimalPointFuture = executor.submit(() -> runOptimalPointWithThreeCheck(OPTIMAL_POINT_TIME_LIMIT));
        try {
            pt = optimalPointFuture.get(OPTIMAL_POINT_TIME_LIMIT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            optimalPointFuture.cancel(true);
            pt = calculateRandomPoint();
        }
        return pt;
    }

    private Point runOptimalPointWithThreeCheck(long timeLimit) {
        long startTime = System.currentTimeMillis();
        Point optimalPoint = calculateOptimalPoint();
        int[][] tempBoard = copyBoard(board); // 현재 보드의 복사본 생성
        CheckResult.ThreeCheckResult threeResult = CheckResult.checkThree(board, optimalPoint.getX(), optimalPoint.getY(), myPlayerMode);
        while (threeResult.Three_count >= 2) {
            if (System.currentTimeMillis() - startTime > timeLimit) {
                break;
            }
            tempBoard[optimalPoint.getX()][optimalPoint.getY()] = 3; // 의미 없는 돌을 놓은 후 보드 업데이트
            optimalPoint = calculateOptimalPoint();
            threeResult = CheckResult.checkThree(board, optimalPoint.getX(), optimalPoint.getY(), myPlayerMode);
        }
        return optimalPoint;
    }
    
    // 보드의 복사본을 만드는 메서드
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    private void logMoveDetails(Point pt, long startTime) {
        long endTime = System.currentTimeMillis(); // 돌을 놓은 시간 측정
        long moveTime = endTime - startTime; // 돌을 놓은 시간 계산

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentTime = sdf.format(currentDate);

        System.out.println("[MinimaxPlayer5] of mode " + myPlayerMode + " makes a pt: " + pt);
        System.out.println("[MiniMaxPlayer5] movetime: " + moveTime + " milliseconds [" + currentTime + "]");
    }

    /**
     * 상대방의 돌을 기억합니다.
     * @param oppmove 상대방의 돌 위치
     */
    public void rememberOpponentMove(Point oppmove) {
        if (oppmove != null) {
            if (oppmove.getX() != -1 && oppmove.getY() != -1) {
                board[oppmove.getX()][oppmove.getY()] = (myPlayerMode == 1) ? 2 : 1;
            }
        }
    }

    /**
     * 라운드 점수를 설정합니다.
     * @param _score 라운드 점수
     */
    public void setRoundScore(int _score) {
        RoundScore += _score;
        System.out.println("[MinimaxPlayer5] round score: " + RoundScore);
    }

    /**
     * 오목판에 놓인 돌의 개수를 반환합니다.
     * @return 오목판에 놓인 돌의 개수
     */
    private int getStoneNum() {
        int count = 0;
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                if (board[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 랜덤한 돌의 위치를 계산합니다.
     * @return 랜덤한 돌의 위치
     */
    private Point calculateRandomPoint() {
        int x, y;
        do {
            x = getRandomMove(0, BoardSize - 1);
            y = getRandomMove(0, BoardSize - 1);
        } while (board[x][y] != 0);
        System.out.println("==> random point (" + x + ", " + y + ")");
        return new Point(x, y);
    }

    /**
     * 랜덤한 수를 생성합니다.
     * @param min 최소값
     * @param max 최대값
     * @return 랜덤한 수
     */
    private int getRandomMove(int min, int max) {
        return rn.nextInt((max - min) + 1) + min;
    }

    /**
     * Minimax 알고리즘을 사용하여 다음 돌의 위치를 계산합니다.
     * @param depth Minimax 알고리즘의 깊이
     * @return 다음 돌의 위치
     */
    public Point calculateMinimaxMove(int depth) {
        Object[] bestMove = minimaxSearchAB(depth, board, true, -1.0, WIN_SCORE);
        return new Point((Integer) bestMove[1], (Integer) bestMove[2]);
    }

    /**
     * Alpha-Beta Pruning을 사용하여 Minimax 알고리즘을 수행합니다.
     * @param depth Minimax 알고리즘의 깊이
     * @param dummyBoard 가상의 오목판
     * @param max 최대 플레이어인지 여부
     * @param alpha Alpha 값
     * @param beta Beta 값
     * @return 최적의 돌의 위치 및 평가 점수
     */
    private static Object[] minimaxSearchAB(int depth, int[][] dummyBoard, boolean max, double alpha, double beta) {
        if (depth == 0) {
            return new Object[]{evaluateBoardForWhite(dummyBoard, !max), null, null};
        }

        ArrayList<int[]> allPossibleMoves = generateMoves(dummyBoard);

        if (allPossibleMoves.size() == 0) {
            return new Object[]{evaluateBoardForWhite(dummyBoard, !max), null, null};
        }

        Object[] bestMove = new Object[3];

        if (max) {
            bestMove[0] = -1.0;
            for (int[] move : allPossibleMoves) {
                addStoneNoGUI(dummyBoard, move[0], move[1], true);
                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, false, alpha, beta);
                removeStoneNoGUI(dummyBoard, move[0], move[1]);

                if ((Double) tempMove[0] > alpha) {
                    alpha = (Double) tempMove[0];
                }
                if ((Double) tempMove[0] >= beta) {
                    return tempMove;
                }

                if ((Double) tempMove[0] > (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        } else {
            bestMove[0] = 100_000_000.0;
            bestMove[1] = allPossibleMoves.get(0)[0];
            bestMove[2] = allPossibleMoves.get(0)[1];

            for (int[] move : allPossibleMoves) {
                addStoneNoGUI(dummyBoard, move[0], move[1], false);
                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, true, alpha, beta);
                removeStoneNoGUI(dummyBoard, move[0], move[1]);

                if ((Double) tempMove[0] < beta) {
                    beta = (Double) tempMove[0];
                }
                if ((Double) tempMove[0] <= alpha) {
                    return tempMove;
                }

                if ((Double) tempMove[0] < (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }

        return bestMove;
    }

    /**
     * 가능한 모든 돌의 위치를 생성합니다.
     * @param board 오목판
     * @return 가능한 모든 돌의 위치
     */
    private static ArrayList<int[]> generateMoves(int[][] board) {
        ArrayList<int[]> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    /**
     * 오목판에 돌을 추가합니다.
     * @param board 오목판
     * @param x X 좌표
     * @param y Y 좌표
     * @param isBlack 흑돌 여부
     */
    private static void addStoneNoGUI(int[][] board, int x, int y, boolean isBlack) {
        board[x][y] = isBlack ? 1 : 2;
    }

    /**
     * 오목판에서 돌을 제거합니다.
     * @param board 오목판
     * @param x X 좌표
     * @param y Y 좌표
     */
    private static void removeStoneNoGUI(int[][] board, int x, int y) {
        board[x][y] = 0;
    }

    /**
     * 흰돌을 위한 오목판 평가를 수행합니다.
     * @param board 오목판
     * @param blacksTurn 현재 턴이 흑돌인지 여부
     * @return 흰돌의 평가 점수
     */
    private static double evaluateBoardForWhite(int[][] board, boolean blacksTurn) {
        evaluationCount++;
        double blackScore = getScore(board, true, blacksTurn);
        double whiteScore = getScore(board, false, blacksTurn);

        if (blackScore == 0) {
            blackScore = 1.0;
        }

        return whiteScore / blackScore;
    }

    /**
     * 점수를 계산합니다.
     * @param board 오목판
     * @param forBlack 흑돌인지 여부
     * @param blacksTurn 현재 턴이 흑돌인지 여부
     * @return 점수
     */
    private static int getScore(int[][] board, boolean forBlack, boolean blacksTurn) {
        int[][] boardMatrix = getBoardMatrix(board);
        return evaluateHorizontal(boardMatrix, forBlack, blacksTurn)
                + evaluateVertical(boardMatrix, forBlack, blacksTurn)
                + evaluateDiagonal(boardMatrix, forBlack, blacksTurn);
    }

    /**
     * 오목판을 0, 1, 2로 변환한 매트릭스를 반환합니다.
     * @param board 오목판
     * @return 변환된 매트릭스
     */
    private static int[][] getBoardMatrix(int[][] board) {
        int[][] matrix = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                matrix[i][j] = board[i][j] == 1 ? 1 : board[i][j] == 2 ? 2 : 0;
            }
        }
        return matrix;
    }

    /**
     * 오목판 행을 평가합니다.
     * @param boardMatrix 오목판 매트릭스
     * @param forBlack 흑돌인지 여부
     * @param playersTurn 현재 턴이 흑돌인지 여부
     * @return 점수
     */
    private static int evaluateHorizontal(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {
        int[] evaluations = {0, 2, 0};
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[0].length; j++) {
                evaluateDirections(boardMatrix, i, j, forBlack, playersTurn, evaluations);
            }
            evaluateDirectionsAfterOnePass(evaluations, forBlack, playersTurn);
        }
        return evaluations[2];
    }

    /**
     * 오목판 열을 평가합니다.
     * @param boardMatrix 오목판 매트릭스
     * @param forBlack 흑돌인지 여부
     * @param playersTurn 현재 턴이 흑돌인지 여부
     * @return 점수
     */
    private static int evaluateVertical(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {
        int[] evaluations = {0, 2, 0};
        for (int j = 0; j < boardMatrix[0].length; j++) {
            for (int i = 0; i < boardMatrix.length; i++) {
                evaluateDirections(boardMatrix, i, j, forBlack, playersTurn, evaluations);
            }
            evaluateDirectionsAfterOnePass(evaluations, forBlack, playersTurn);
        }
        return evaluations[2];
    }

    /**
     * 오목판 대각선을 평가합니다.
     * @param boardMatrix 오목판 매트릭스
     * @param forBlack 흑돌인지 여부
     * @param playersTurn 현재 턴이 흑돌인지 여부
     * @return 점수
     */
    private static int evaluateDiagonal(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {
        int[] evaluations = {0, 2, 0};
        for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
            int iStart = Math.max(0, k - boardMatrix.length + 1);
            int iEnd = Math.min(boardMatrix.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                evaluateDirections(boardMatrix, i, k - i, forBlack, playersTurn, evaluations);
            }
            evaluateDirectionsAfterOnePass(evaluations, forBlack, playersTurn);
        }
        for (int k = 1 - boardMatrix.length; k < boardMatrix.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length - 1);
            for (int i = iStart; i <= iEnd; ++i) {
                evaluateDirections(boardMatrix, i, i - k, forBlack, playersTurn, evaluations);
            }
            evaluateDirectionsAfterOnePass(evaluations, forBlack, playersTurn);
        }
        return evaluations[2];
    }

    /**
     * 오목판 방향을 평가합니다.
     * @param boardMatrix 오목판 매트릭스
     * @param i X 좌표
     * @param j Y 좌표
     * @param isBot 흑돌인지 여부
     * @param botsTurn 현재 턴이 흑돌인지 여부
     * @param eval 평가 점수
     */
    private static void evaluateDirections(int[][] boardMatrix, int i, int j, boolean isBot, boolean botsTurn, int[] eval) {
        if (boardMatrix[i][j] == (isBot ? 2 : 1)) {
            eval[0]++;
        } else if (boardMatrix[i][j] == 0) {
            if (eval[0] > 0) {
                eval[1]--;
                eval[2] += getConsecutiveSetScore(eval[0], eval[1], isBot == botsTurn);
                eval[0] = 0;
            }
            eval[1] = 1;
        } else if (eval[0] > 0) {
            eval[2] += getConsecutiveSetScore(eval[0], eval[1], isBot == botsTurn);
            eval[0] = 0;
            eval[1] = 2;
        } else {
            eval[1] = 2;
        }
    }

    /**
     * 오목판 방향을 평가한 후 점수를 합산합니다.
     * @param eval 평가 점수
     * @param isBot 흑돌인지 여부
     * @param playersTurn 현재 턴이 흑돌인지 여부
     */
    private static void evaluateDirectionsAfterOnePass(int[] eval, boolean isBot, boolean playersTurn) {
        if (eval[0] > 0) {
            eval[2] += getConsecutiveSetScore(eval[0], eval[1], isBot == playersTurn);
        }
        eval[0] = 0;
        eval[1] = 2;
    }

    /**
     * 연속된 돌의 점수를 반환합니다.
     * @param count 돌의 개수
     * @param blocks 블록 수
     * @param currentTurn 현재 턴인지 여부
     * @return 점수
     */
    private static int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
        final int winGuarantee = 1000000;
        if (blocks == 2 && count < 5) {
            return 0;
        }

        switch (count) {
            case 5:
                return WIN_SCORE;
            case 4:
                if (currentTurn) {
                    return winGuarantee;
                } else {
                    return blocks == 0 ? winGuarantee / 4 : 200;
                }
            case 3:
                return blocks == 0 ? (currentTurn ? 50000 : 200) : (currentTurn ? 10 : 5);
            case 2:
                return blocks == 0 ? (currentTurn ? 7 : 5) : 3;
            case 1:
                return 1;
            default:
                return WIN_SCORE * 2;
        }
    }

    /**
     * 최적의 돌 위치를 계산합니다.
     * @return 최적의 돌 위치
     */
    private Point calculateOptimalPoint() {
        int bestX = -1;
        int bestY = -1;
        int maxPoint = -1;

        int ar1 = 100;
        int ar4 = 110;
        int ar2 = 1;

        int dr1 = 110;
        int dr2 = 330;

        for (int y = 0; y < BoardSize; y++) {
            for (int x = 0; x < BoardSize; x++) {
                int[] a1 = new int[8];
                int[] a2 = new int[8];
                int[] a4 = new int[8];
                int[] d1 = new int[8];
                int[] d2 = new int[8];
                int at = 0;
                int df = 0;
                if (board[x][y] != 0) {
                    continue;
                }

                for (int d = 0; d <= 3; d++) {
                    int as1 = 0;
                    int as2 = 0;
                    int as4 = 0;
                    int ds1 = 0;
                    int ds2 = 0;
                    for (int n = 0; n < 2; n++) {
                        int cont = 1;
                        int index = d + 4 * n;
                        while (cont < (BoardSize * BoardSize)) {
                            Point nextPoint = null;
                            switch (index) {
                                case 0:
                                    if (x + cont < BoardSize) {
                                        nextPoint = new Point(x + cont, y);
                                    }
                                    break;
                                case 1:
                                    if (x + cont < BoardSize && y + cont < BoardSize) {
                                        nextPoint = new Point(x + cont, y + cont);
                                    }
                                    break;
                                case 2:
                                    if (y + cont < BoardSize) {
                                        nextPoint = new Point(x, y + cont);
                                    }
                                    break;
                                case 3:
                                    if (x - cont >= 0 && y + cont < BoardSize) {
                                        nextPoint = new Point(x - cont, y + cont);
                                    }
                                    break;
                                case 4:
                                    if (x - cont >= 0) {
                                        nextPoint = new Point(x - cont, y);
                                    }
                                    break;
                                case 5:
                                    if (x - cont >= 0 && y - cont >= 0) {
                                        nextPoint = new Point(x - cont, y - cont);
                                    }
                                    break;
                                case 6:
                                    if (y - cont >= 0) {
                                        nextPoint = new Point(x, y - cont);
                                    }
                                    break;
                                case 7:
                                    if (x + cont < BoardSize && y - cont >= 0) {
                                        nextPoint = new Point(x + cont, y - cont);
                                    }
                                    break;
                            }

                            cont++;

                            if (nextPoint != null) {
                                int nextColor = board[nextPoint.getX()][nextPoint.getY()];
                                if (nextColor == (myPlayerMode == 1 ? 1 : 2)) {
                                    if (a2[index] == 0 && d1[index] == 0) {
                                        a1[index]++;
                                    } else if (a2[index] == 0 && d1[index] > 0) {
                                        d2[index]++;
                                        break;
                                    }
                                } else if (nextColor == 0) {
                                    a2[index]++;
                                } else {
                                    if (a2[index] == 0 && a1[index] == 0) {
                                        d1[index]++;
                                    } else if (a2[index] == 0 && a1[index] > 0) {
                                        a4[index]++;
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                break;
                            }
                        }

                        as1 += a1[index];
                        as2 += a2[index];
                        as4 += a4[index];
                        ds1 += (d1[index] * d1[index] * dr1);
                        ds2 += d2[index];
                    }

                    at += (as1 * as1 * ar1 + as2 * ar2 - as4 * ar4);
                    df += (ds1 - ds2 * dr2);
                }

                if (maxPoint < (at + df)) {
                    maxPoint = at + df;
                    bestX = x;
                    bestY = y;
                }
            }
        }

        return new Point(bestX, bestY);
    }

	@Override
	public OMG33TestScenario createTestScenario() {
		// TODO Auto-generated method stub
		return null;
	}
}
