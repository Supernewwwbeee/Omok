package OMG;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author KC Lee
 */
public interface OMGPlayable {
    
    public final static int blackStone=1;//
    public final static int whiteStone=2;//


	public final static int SCORE_LOSE=-1;//
	public final static int SCORE_EVEN=0;//
	public final static int SCORE_WIN=1;//
	public final static int SCORE_EXIT=-10;//

    /**
     * set the board size for the Omok game and the player mode.
	 * the player mode is either blackStone(1) or whiteStone(2).
     * @return 
     */
    public void setBoardSizePlayerMode(int _boardSize, int _playerMode);

	
	/**
     * Return the group name
     * @return the group name
     */
    public String getYourGroupName();
    
    /**
     * Return your move of (X, Y) for the current turn
	 * the range of X or Y should be equal to or greater than STARTINDEX and equal to or less than board size -1
	 * STARTINDEX <= X <= (board size-1) and STARTINDEX <= X <= (board size-1)
	 * playermode is whether your position is either blackStone or whiteStone.
     * @return your move for the current round
     */
    public Point getUserMove(int playermode);
    

	/**
     * Remember the opponent's move in addition to your current move.
     * You might want to use the information to enhance the logic of deciding your move later.
     * @return 
     */
    public void rememberOpponentMove(Point oppmove);
	
	/**
	* set the score of a round: the score is one of SCORE_LOSE, SCORE_EVEN, and SCORE_WIN
	*/
	public void setRoundScore(int _score);
}


