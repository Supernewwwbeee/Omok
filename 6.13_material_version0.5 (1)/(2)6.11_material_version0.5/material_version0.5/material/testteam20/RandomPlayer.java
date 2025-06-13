package testteam20;

import OMG.OMGPlayable;
import OMG.Point;
import java.util.Random;

public class RandomPlayer implements OMGPlayable
{
	private int BoardSize = 15;    
	private int myPlayerMode;    
	private Random rn;

	public RandomPlayer() {
			rn = new Random();
			rn.setSeed(System.currentTimeMillis());
	}
	/**
     * set the board size for the Omok game.
     * @return 
     */
    public void setBoardSizePlayerMode(int _boardSize, int _playerMode) {
		this.BoardSize = _boardSize;
		this.myPlayerMode = _playerMode;
		//System.out.println("[" + this.getYourGroupName() +"] is mode of " + this.myPlayerMode);
	}

	private int getRandomMove(int min, int max) {
        return rn.nextInt((max - min) + 1) + min; // Generates a number between lower and upper (inclusive)			
	}

	
	/**
     * Return the group name
     * @return the group name
     */
    public String getYourGroupName() {
		return "testteam.RandomPlayer";
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
		Point pt = new Point( getRandomMove(0, BoardSize-1), getRandomMove(0, BoardSize-1));
		System.out.println("[RandomPlayer] of mode " + myPlayerMode + " makes a pt: " +pt );
		return pt;
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
