package testteam20;

import OMG.*;
import java.util.*;



public class MyTester2 implements OMG33Testable
{
	public OMG33TestScenario createTestScenario() {

        // no 3-3 case
        int[][] board5 = new int[19][19];
        board5[10][10] = 1;
        board5[10][11] = 1;
        board5[10][12] = 1;

		ArrayList<Point> ForbiddenPoints = new ArrayList<Point>();
		//ForbiddenPoints.add(new Point(5,5));
		ArrayList<Point> PermittedPoints = new ArrayList<Point>();
		PermittedPoints.add(new Point(10,10));
		PermittedPoints.add(new Point(12,11));
		return new OMG33TestScenario(board5, ForbiddenPoints, PermittedPoints, 1);
	}
}