package OMG;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OMGame {

	private static String player1fullname;
	private static String player2fullname;

	
	
	private static Properties prop;
	private static boolean verbose = false;
	private static boolean verboseBasic = true;
	private static int HowManyRuns = 1;

	private static PrintStream resultsWriter = null;
	
	private static int GlobalBoardSize_First = 21;
	private int BoardSize = 21;    
	protected final static int STARTINDEX = 0;

    private OMGPlayable omgPlayer1;
    private OMGPlayable omgPlayer2;

	private char[][] board;  // BoardSize*BoardSize 
	private int[][] save; //
	private boolean[][] chk; //
	

	public OMGame(int _BoardSize) {
		BoardSize = _BoardSize;        
		board = new char[BoardSize][BoardSize];// 
		save=new int[BoardSize][BoardSize];//
		chk = new boolean[BoardSize][BoardSize];//
	}
	
	private void initializeBoard() {
        // 초기 보드판의 각 칸을 설정
        for (int x = 0; x < BoardSize; x++) {
            for (int y = 0; y < BoardSize; y++) {
                if (x == 0 && y == 0) { // 좌상단 모서리
                    board[x][y] = '┌';
                }
                else if (x == (BoardSize-1) && y == (BoardSize-1)){ // 우하단 모서리
                    board[x][y] = '┘';
                }
                else if (x == (BoardSize-1) && y == 0) { // 좌하단 모서리
                    board[x][y] = '└';
                }
                else if (x == 0 && y == (BoardSize-1)) { // 우상단 모서리
                    board[x][y] = '┐';
                }
                else if (x == 0) { // 윗변
                    board[x][y] = '┬';
                }
                else if (x == (BoardSize-1)) { // 아랫변
                    board[x][y] = '┴';
                }
                else if (y == 0) { // 왼쪽변
                    board[x][y] = '├';
                }
                else if (y == (BoardSize-1)) { // 오른쪽변
                    board[x][y] = '┤';
                }
                else {
                    board[x][y] = '┼'; // 내부 교차점
                }
            }
        }
	}

	protected void showBoard() {
		if (verboseBasic) {
					for (int i = 0; i < BoardSize; i++) {
						for (int j = 0; j < BoardSize; j++) {
							System.out.print(board[i][j]);
						}
						System.out.println();
					}
		}
	}

	protected static void showData(int[][] data) {
		if (verboseBasic) {
			for (int i=0; i< data.length ;i++ )
			{
				for (int j=0;j< data[0].length ;j++ )
				{
					System.out.print(" " + data[i][j]);
				}
				System.out.println();
			}
		}
	}

	protected static void log_verbose(String message) {
		if (verbose)
		{
			System.out.println(message);
		}
	}
	
	protected static void log_verboseBasic(String message) {
		if (verboseBasic)
		{
			System.out.println(message);
		}
	}

	protected static void log_results(String message) {		
		resultsWriter.println(message);		
		resultsWriter.flush();
	}



	public void initializePlayer(String _player1fullname, String _player2fullname) {
        OMGPlayable rspPlayer1;
        OMGPlayable rspPlayer2;
               
        try {
            omgPlayer1 = createOMGPlayable(_player1fullname);
            omgPlayer2 = createOMGPlayable(_player2fullname);
			omgPlayer1.setBoardSizePlayerMode(this.BoardSize, OMGPlayable.blackStone);
			omgPlayer2.setBoardSizePlayerMode(this.BoardSize, OMGPlayable.whiteStone);
        } catch (Exception e) {
            System.out.println("Cannot perform the test!!!");
            e.printStackTrace();            
            return;
        } 
	}

	
	public String play(int _mode) 
	{
        
        //countBefore
        int countBefore=1;

		// ----------------------------------------------------
		// ----------------------------------------------------
		int ox = -1; // 
		int oy = -1; // 

		//
		int xBefore = -1;
		int yBefore = -1;



		log_verboseBasic("          Omok Game        ");
		log_verboseBasic(" ");

		log_verbose("[verbose] this.BoardSize="+BoardSize + ", _mode:" + _mode);

		//
		if (_mode % 2 == 0) {
			initializePlayer(player1fullname, player2fullname);
		} else {
			initializePlayer(player2fullname, player1fullname);
		}
		

		//
		initializeBoard();


		//
		int count = 1;// 
		String gameResult;
		String p1Name = (_mode % 2 == 0) ? player1fullname : player2fullname;
		String p2Name = (_mode % 2 == 0) ? player2fullname : player1fullname;
		GameLogger logger = new GameLogger(_mode, p1Name, p2Name);
		GAMEOVER:
		while (true) {


			if (ox != -1 && oy != -1 && !chk[ox][oy]) {
				xBefore = ox;
				yBefore = oy;

				if (count % 2 == 1) {
					board[ox][oy] = '●';
					save[ox][oy]=OMGPlayable.blackStone;
					//System.out.println("\n blackStone \n");
				}
				else if (count % 2 == 0) {
					board[ox][oy] = '○';
					save[ox][oy]=OMGPlayable.whiteStone;
					//System.out.println("\n whiteStone \n");
				}
				
				boolean finished = OMGameUtil.checkFinished(save, ox, oy, save[ox][oy]);
				if (finished)
				{
					if (count % 2 == 1) {
						log_verboseBasic(omgPlayer1.getYourGroupName() + " of mode " + save[ox][oy] + " won");
						omgPlayer1.setRoundScore(OMGPlayable.SCORE_WIN);
						omgPlayer2.setRoundScore(OMGPlayable.SCORE_LOSE);
						gameResult = omgPlayer1.getYourGroupName() + ",1,:" + OMGPlayable.SCORE_WIN + ";" + omgPlayer2.getYourGroupName() + ",2,:" + OMGPlayable.SCORE_LOSE;
					} else {
						log_verboseBasic(omgPlayer2.getYourGroupName() + " of mode " + save[ox][oy] + " won");
						omgPlayer2.setRoundScore(OMGPlayable.SCORE_WIN);
						omgPlayer1.setRoundScore(OMGPlayable.SCORE_LOSE);
						gameResult = omgPlayer1.getYourGroupName() + ",1,:" + OMGPlayable.SCORE_LOSE + ";" + omgPlayer2.getYourGroupName() + ",2,:" + OMGPlayable.SCORE_WIN;
					}
					log_verboseBasic("GAMEOVER~~");
					
					break GAMEOVER;		
				}

				boolean isThreeThree = OMGameUtil.checkThreeThree(save, ox, oy, save[ox][oy]);
				if (finished) {
					if (count % 2 == 1) {
						gameResult = omgPlayer1.getYourGroupName() + ",1,:" + OMGPlayable.SCORE_EXIT + ";" + omgPlayer2.getYourGroupName() + ",2,:" + OMGPlayable.SCORE_EVEN;
					} else {
						gameResult = omgPlayer1.getYourGroupName() + ",1,:" + OMGPlayable.SCORE_EVEN + ";" + omgPlayer2.getYourGroupName() + ",2,:" + OMGPlayable.SCORE_EXIT;
					}

					log_verboseBasic("GAMEOVER~~");
					break GAMEOVER;
				}

				chk[ox][oy] = true;                            
				count++;
				countBefore=count;


				log_verboseBasic("Previous move : [" + xBefore + "][" + yBefore + "]\n");
			} else if (countBefore>1 && count>1) {
				if (chk[ox][oy])
				{
					log_verboseBasic("\n Previous move (" + ox + ", " + oy + ") is proposed. Enter again.");
				} else {
					System.out.println("\n ...what to do?...");
				}
			}

			// 
			showBoard();

			// 
			if (count == ((BoardSize-STARTINDEX)*(BoardSize-STARTINDEX))+1)
			{
				omgPlayer1.setRoundScore(OMGPlayable.SCORE_EVEN);
				omgPlayer2.setRoundScore(OMGPlayable.SCORE_EVEN);
				gameResult = omgPlayer1.getYourGroupName() + ",1,:" + OMGPlayable.SCORE_EVEN + ";" + omgPlayer2.getYourGroupName() + ",2,:" + OMGPlayable.SCORE_EVEN;
				log_verboseBasic("The board is full.");
				log_verboseBasic("GAMEOVER~~");
				break GAMEOVER;				
			}

			long moveStartTime = System.currentTimeMillis();
			//��ǥ �Է�
			do {
				log_verboseBasic("\nTo Quit:-1 y");
				String currentTurnPlayerName;
				if (count % 2 == 1) {
					log_verboseBasic("흑돌 차례");
					currentTurnPlayerName = omgPlayer1.getYourGroupName();
				} else {
					log_verboseBasic("백돌 차례");
					currentTurnPlayerName = omgPlayer2.getYourGroupName();
				}
				log_verboseBasic(" x sp y> ");

				Point newMove;
				if (count % 2 == 1) {
					newMove = omgPlayer1.getUserMove(OMGPlayable.blackStone);
					omgPlayer2.rememberOpponentMove(newMove);
				} else {
					newMove = omgPlayer2.getUserMove(OMGPlayable.whiteStone);
					omgPlayer1.rememberOpponentMove(newMove);
				}
				ox = newMove.getX();
				oy = newMove.getY();
				// 수를 둔 후 로그 기록
				long moveEndTime = System.currentTimeMillis();
				logger.logMove(count, currentTurnPlayerName, newMove, moveEndTime - moveStartTime);

				if (ox == -1) {
					if (count % 2 == 1) {
						gameResult = omgPlayer1.getYourGroupName() + ",1,:" + OMGPlayable.SCORE_EXIT + ";" + omgPlayer2.getYourGroupName() + ",2,:" + OMGPlayable.SCORE_EVEN;
					} else {
						gameResult = omgPlayer1.getYourGroupName() + ",1,:" + OMGPlayable.SCORE_EVEN + ";" + omgPlayer2.getYourGroupName() + ",2,:" + OMGPlayable.SCORE_EXIT;
					}

					log_verboseBasic("GAMEOVER~~");
					break GAMEOVER;				
				}


				//
				if (!(STARTINDEX <= ox && ox <= (BoardSize-1)) || !(STARTINDEX <= oy && oy <= (BoardSize-1))) {
					log_verboseBasic("\n              [Warning]                ");
					log_verboseBasic("The move is out of the range.");
					log_verboseBasic("x in [" + STARTINDEX + "," + (BoardSize-1) + "],   " + "y in [" + STARTINDEX +"," + (BoardSize-1) +"]");
				} else {
					//penalty? ??
				}

			} while (!(STARTINDEX <= ox && ox <= (BoardSize-1)) || !(STARTINDEX <= oy && oy <= (BoardSize-1)));
			log_verboseBasic("==================================");
		}
		//게임 끝난 후 요약 정보 출력
		logger.displaySummary(gameResult);
		return gameResult;
	}

    public static void main(String[] args)  {		
		readProperyFile(args);

		for (int i=0; i < HowManyRuns ;i++)
		{
			String gameResult = (new OMGame(GlobalBoardSize_First)).play(i);		
			log_results("[round "+ i + "];" + gameResult);
		}		
    }

	private static void readProperyFile(String[] args)
	{
		prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("material/config.properties");

			// load a properties file
			prop.load(input);

			// get the property values
			player1fullname = prop.getProperty("player1");
			player2fullname = prop.getProperty("player2");
			GlobalBoardSize_First =  Integer.parseInt(prop.getProperty("BoardSize"));
			HowManyRuns = Integer.parseInt(prop.getProperty("HowManyRuns"));
			verbose = Boolean.parseBoolean(prop.getProperty("verbose"));
			verboseBasic = Boolean.parseBoolean(prop.getProperty("verboseBasic"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		//override the parameters if cmd arguments are in.
		if ( args.length > 2 )
		{
				player1fullname = args[0];
				player2fullname = args[1];
				try
				{
					GlobalBoardSize_First =  Integer.parseInt(args[2]);	
				}
				catch (Throwable th)
				{
					GlobalBoardSize_First = 21;
				}				

		} 

		try {
			resultsWriter = new PrintStream(new FileOutputStream("myoutput.txt", true));
		} catch (IOException ex) {
			ex.printStackTrace();
			resultsWriter = System.err;
		}

	}


    private static OMGPlayable createOMGPlayable(String fullPackageClassname) throws Exception {
        Object object = null;
        Class classDefinition = Class.forName(fullPackageClassname);
        object = classDefinition.newInstance();
        return (OMGPlayable)object;
    }

}

