package team6;

import OMG.*;
import java.util.*;



public class Team6Tester1 implements OMG33Testable
{
	public OMG33TestScenario createTestScenario() {

        // 3-3 exists
		int[][] board = new int[19][19];
        // A 지점
        board[1][3] = 1; // 흑돌
        board[2][3] = 1; // 흑돌
        board[3][2] = 1; // 흑돌
        board[3][4] = 1; // 흑돌
        
        // B 지점
        board[9][1] = 1; // 흑돌
        board[10][2] = 1; // 흑돌
        board[9][4] = 1; // 흑돌
        board[10][4] = 1; // 흑돌
        
        // C 지점
        board[6][9] = 1; // 흑돌
        board[3][12] = 1; // 흑돌
        board[5][12] = 1; // 흑돌
        board[6][13] = 1; // 흑돌
        
        // D 지점
        board[9][10] = 1; // 흑돌
        board[12][10] = 1; // 흑돌
        board[11][12] = 1; // 흑돌
        board[11][13] = 1; // 흑돌
        
    	// 1번 지점, true가 출력되어야 한다
        board[1][1] = 1; // 흑돌
        board[4][1] = 1; // 흑돌
        board[2][3] = 1; // 흑돌
        board[2][4] = 1; // 흑돌
        
        // 2번 지점, true가 출력되어야 한다         
        board[10][3] = 1; // 흑돌
        board[11][3] = 1; // 흑돌
        board[9][4] = 1; // 흑돌
        board[10][5] = 1; // 흑돌
        
        // 3번 지점, true가 출력되어야 한다
        board[10][9] = 1; // 흑돌
        board[11][10] = 1; // 흑돌
        board[6][7] = 1; // 흑돌
        board[7][7] = 1; // 흑돌
        
        // 4번 지점, true가 출력되어야 한다
        board[12][9] = 1; // 흑돌
        board[11][8] = 1; // 흑돌
        board[11][10] = 1; // 흑돌
        board[10][9] = 1; // 흑돌
        board[10][5] = 1; // 흑돌
        board[9][4] = 1; // 흑돌
        
        // 5번 지점, false가 출력되어야 한다
        
        board[5][5] = 1; // 흑돌        
        board[5][6] = 1; // 흑돌
        board[6][7] = 1; // 흑돌
        board[7][7] = 1; // 흑돌
        board[5][9] = 1; // 흑돌
        
        // 6번 지점, false가 출력되어야 한다
        
        board[0][8] = 2; // 백돌
        board[1][8] = 2; // 백돌
        board[2][8] = 2; // 백돌
        board[3][8] = 2; // 백돌
        board[1][10] = 1; // 흑돌
        board[1][12] = 1; // 흑돌
        board[3][11] = 1; // 흑돌
        board[4][11] = 1; // 흑돌
        
        
        // 7번 지점, false가 출력되어야 한다
        
        
        board[14][11] = 2; // 백돌
        board[14][9] = 2; // 백돌
        board[14][12] = 2; // 백돌
        board[14][13] = 2; // 백돌
        board[0][14] = 2; // 백돌
        board[1][14] = 2; // 백돌
        board[2][14] = 2; // 백돌
        board[3][14] = 2; // 백돌
        board[5][14] = 2; // 백돌
        board[6][14] = 2; // 백돌
        board[7][14] = 2; // 백돌
        board[8][14] = 2; // 백돌
        board[10][14] = 2; // 백돌
        board[11][14] = 2; // 백돌
        board[12][14] = 2; // 백돌
        board[13][14] = 2; // 백돌
        board[8][9] = 2; // 백돌
        


		ArrayList<Point> ForbiddenPoints = new ArrayList<Point>();
		ForbiddenPoints.add(new Point(3,3));
		ForbiddenPoints.add(new Point(12,4));
		ForbiddenPoints.add(new Point(4,11));
		ForbiddenPoints.add(new Point(11,10));
		ForbiddenPoints.add(new Point(2,1));
		ForbiddenPoints.add(new Point(8,3));
		ForbiddenPoints.add(new Point(8,7));
		ForbiddenPoints.add(new Point(12,7));
		ArrayList<Point> PermittedPoints = new ArrayList<Point>();
		PermittedPoints.add(new Point(5,7));
		PermittedPoints.add(new Point(1,11));
		PermittedPoints.add(new Point(11,9));

		return new OMG33TestScenario(board, ForbiddenPoints, PermittedPoints, 1);
	}
}

