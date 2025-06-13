package OMG;

import java.util.Arrays; // 필요한 경우 추가

public class OMGameUtil2 {

    // 방향을 나타내는 Enum 정의
    private enum Direction {
        HORIZONTAL(0, 1),   // 가로 (y축 변화)
        VERTICAL(1, 0),     // 세로 (x축 변화)
        DIAGONAL_R_DOWN(1, 1), // 우하향 대각선
        DIAGONAL_R_UP(1, -1);  // 우상향 대각선 (원래 코드의 좌하-우상과 동일한 라인)

        final int dx;
        final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    /**
     * 돌과 빈칸의 개수를 세는 내부 클래스 (또는 별도 파일의 Record/Class)
     */
    private static class SearchResult {
        int stones = 0;
        int blanks = 0;
    }

    /**
     * 특정 방향으로 돌과 빈칸을 탐색하는 메서드
     *
     * @param data      게임 보드
     * @param chX       기준 x 좌표
     * @param chY       기준 y 좌표
     * @param turn      현재 플레이어
     * @param dx        x축 변화량
     * @param dy        y축 변화량
     * @param maxIndex  보드 최대 인덱스
     * @return SearchResult (돌 개수, 빈칸 개수)
     */
    private static SearchResult countStonesAndBlanks(int[][] data, int chX, int chY, int turn,
                                                     int dx, int dy, int maxIndex) {
        SearchResult result = new SearchResult();
        int tempX = chX + dx;
        int tempY = chY + dy;

        // 첫 번째 돌 그룹
        while (isValid(tempX, tempY, maxIndex) && data[tempX][tempY] == turn) {
            result.stones++;
            tempX += dx;
            tempY += dy;
        }
        // 첫 번째 빈칸 그룹
        if (isValid(tempX, tempY, maxIndex) && data[tempX][tempY] == 0) {
            result.blanks++;
            tempX += dx;
            tempY += dy;
        }
        // 두 번째 돌 그룹 (첫 번째 빈칸 너머)
        while (isValid(tempX, tempY, maxIndex) && data[tempX][tempY] == turn) {
            result.stones++;
            tempX += dx;
            tempY += dy;
        }
        // 두 번째 빈칸 그룹
        if (isValid(tempX, tempY, maxIndex) && data[tempX][tempY] == 0) {
            result.blanks++;
             tempX += dx; 
             tempY += dy; 
        }
        return result;
    }

    /**
     * 좌표 유효성 검사
     */
    private static boolean isValid(int x, int y, int maxIndex) {
        return x >= OMGame.STARTINDEX && x <= maxIndex && y >= OMGame.STARTINDEX && y <= maxIndex;
    }

    /**
     * This function will be moved to OMGameUtil.java when finalized.
     *
     * @param turn  the mode either black (OMGPlayable.blackStone, 1) or white (OMGPlayable.whiteStone, 2)
     * @return true    meaning the move (chX, chY) is forbidden
     * false   meaning the move (chX, chY) is permitted
     */
    public static boolean checkThreeThree(int[][] data, int chX, int chY, int turn) {
        int threeCount = 0;
        // 각 방향에서 "3"이 형성되었는지 기록 (디버깅용)
        // boolean[] directionThree = new boolean[Direction.values().length]; 
        // OMGame.STARTINDEX는 0으로 가정. 실제 값에 따라 조정 필요.
        int maxIndex = data[0].length - 1; 

        OMGame.log_verbose("[verbose] chX:" + chX + ", chY: " + chY + ", turn:" + turn);

        // 가장자리 조건은 원본과 동일하게 유지 (논리적으로는 대부분 true가 됨)
        if (!(chX == OMGame.STARTINDEX && chX == maxIndex && chY == OMGame.STARTINDEX && chY == maxIndex)) {
            int dirIdx = 0; // 디버깅용 인덱스
            for (Direction dir : Direction.values()) {
                SearchResult result1 = countStonesAndBlanks(data, chX, chY, turn, dir.dx, dir.dy, maxIndex);
                SearchResult result2 = countStonesAndBlanks(data, chX, chY, turn, -dir.dx, -dir.dy, maxIndex); // 반대 방향

                int dir_stones = result1.stones;
                int r_dir_stones = result2.stones;
                int dir_blanks = result1.blanks;
                int r_dir_blanks = result2.blanks;

                int stonesum = dir_stones + r_dir_stones;

                if (stonesum == 2) { // 현재 놓은 돌과 합쳐 3개가 되는 경우
                    if (dir_blanks + r_dir_blanks >= 3) {
                        boolean isThreeThisDirection = false;
                        
                        
                        if (dir_stones == 2) { // OO. 형태 (O: 기존돌, .: 현재돌)
                            isThreeThisDirection = true;
                            if ((dir_blanks == 1) && (r_dir_blanks == 2)) {
                                if (isValid(chX - (dir_stones + dir_blanks) * dir.dx, chY - (dir_stones + dir_blanks) * dir.dy, maxIndex) &&
                                    data[chX - (dir_stones + dir_blanks) * dir.dx][chY - (dir_stones + dir_blanks) * dir.dy] == turn) {
                                    isThreeThisDirection = false;
                                }
                            }
                        } else if (dir_stones == 1 && r_dir_stones == 1) { // O.O 형태
                            isThreeThisDirection = true;
                            if (dir_blanks == 1) {
                                if (isValid(chX - (dir_stones + dir_blanks) * dir.dx, chY - (dir_stones + dir_blanks) * dir.dy, maxIndex) &&
                                    data[chX - (dir_stones + dir_blanks) * dir.dx][chY - (dir_stones + dir_blanks) * dir.dy] == turn) {
                                    isThreeThisDirection = false;
                                }
                            } else if (r_dir_blanks == 1) { // 반대편 체크는 반대방향으로
                                if (isValid(chX + (r_dir_stones + r_dir_blanks) * dir.dx, chY + (r_dir_stones + r_dir_blanks) * dir.dy, maxIndex) &&
                                    data[chX + (r_dir_stones + r_dir_blanks) * dir.dx][chY + (r_dir_stones + r_dir_blanks) * dir.dy] == turn) {
                                    isThreeThisDirection = false;
                                }
                            }
                        } else if (r_dir_stones == 2) { // .OO 형태
                            isThreeThisDirection = true;
                            if (dir_blanks == 2 && r_dir_blanks == 1) { // 반대편 체크는 반대방향으로
                                if (isValid(chX + (r_dir_stones + r_dir_blanks) * dir.dx, chY + (r_dir_stones + r_dir_blanks) * dir.dy, maxIndex) &&
                                    data[chX + (r_dir_stones + r_dir_blanks) * dir.dx][chY + (r_dir_stones + r_dir_blanks) * dir.dy] == turn) {
                                    isThreeThisDirection = false;
                                }
                            }
                        }
                        
                        if(isThreeThisDirection){
                            threeCount++;
                            // directionThree[dirIdx] = true; // 디버깅용
                            
                            System.out.println(dir.name() + " 방향에서 3 형성"); 
                        }
                    }
                }
                dirIdx++;
            }
        }

        if (turn==1&&threeCount >= 2) {
            // 상세 로그는 여기서 한 번에 처리하거나, 위에서 각 방향별로 처리
            System.out.println("3-3 발생!");
        }

        return turn==1&&threeCount >= 2;
    }
}