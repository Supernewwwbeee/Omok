package testteam20;

import OMG.OMGPlayable;
import OMG.Point;
import java.util.Random;
import java.util.Scanner;

public class ConsolePlayer implements OMGPlayable
{
	private int BoardSize = 15;    
	private int myPlayerMode;    

	public ConsolePlayer() {
	}
	/**
     * set the board size for the Omok game.
     * @return 
     */
    public void setBoardSizePlayerMode(int _boardSize, int _playerMode) {
		this.BoardSize = _boardSize;
		this.myPlayerMode = _playerMode;
		System.out.println("[" + this.getYourGroupName() +"] is mode of " + this.myPlayerMode);
	}

	
	/**
     * Return the group name
     * @return the group name
     */
    public String getYourGroupName() {
		return "testteam.ConsolePlayer";
	}
    
    /**
     * Return your move of (X, Y) for the current turn
	 * playermode is whether your position is either blackStone or whiteStone.
     * @return your move for the current round
     */
    public Point getUserMove(int playermode) {
		if (playermode != myPlayerMode)
		{
			System.out.println("[Exception] Critical. Player Mode does not match in " + this.getYourGroupName());
		}
		int ox, oy;
		try {
			Scanner scan = new Scanner(System.in);
			ox = scan.nextInt();
			oy = scan.nextInt();
		} catch (Throwable th) {		
			th.printStackTrace();
			ox = BoardSize/2; //arbitrary setting
			oy = BoardSize/2; //arbitrary setting
		}
		return new Point(ox,oy);
	}
    

	/**
     * Remember the opponent's move in addition to your current move.
     * You might want to use the information to enhance the logic of deciding your move later.
     * @return 
     */
    public void rememberOpponentMove(Point oppmove) {
		//to-do
	}


	public void setRoundScore(int _score) {
		//to-do
	}
}
