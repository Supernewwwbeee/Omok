package OMG;

import java.util.*;

public class OMG33TestScenario
{
	public int[][] board;
	public ArrayList<Point> ForbiddenPoints;
	public ArrayList<Point> PermittedPoints;
	public int turn; //either black (OMGPlayable.blackStone, 1) or white (OMGPlayable.whiteStone, 2)
	public OMG33TestScenario(int[][] _b, ArrayList<Point> _f, ArrayList<Point> _p) {
		this(_b, _f, _p, OMGPlayable.blackStone);
	}

	public OMG33TestScenario(int[][] _b, ArrayList<Point> _f, ArrayList<Point> _p, int _turn) {
		board = _b;
		ForbiddenPoints = _f;
		PermittedPoints = _p;
		turn = _turn;
	}
}