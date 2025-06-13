package testteam20;

import OMG.*;
import java.util.*;



public class MyTester1 implements OMG33Testable
{
	public OMG33TestScenario createTestScenario() {

        // 3-3 exists
		int[][] board2 = new int[19][19];
        board2[5][5] = 1; //need to set it? it is okay anyway
        board2[6][6] = 1;
        board2[4][4] = 1;
        board2[4][6] = 1;
        board2[6][4] = 1;

		ArrayList<Point> ForbiddenPoints = new ArrayList<Point>();
		ForbiddenPoints.add(new Point(5,5));
		ArrayList<Point> PermittedPoints = new ArrayList<Point>();
		PermittedPoints.add(new Point(1,1));
		PermittedPoints.add(new Point(1,3));
		return new OMG33TestScenario(board2, ForbiddenPoints, PermittedPoints, 1);
	}
}

