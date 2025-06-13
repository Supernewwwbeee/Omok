package OMG;

public class OMGameUtil 
{
	public static void main(String[] args) throws Exception
	{
		//System.out.println("Hello World!");
		//int HowManyTests = 10;
		String[] testunits = {"team6.Team6Tester1"}; // 테스트할 클래스 이름들(패키지명 포함)

		for (int j=0; j<testunits.length; j++ ) // 긱 테스트 유닛에 대해 반복
		{	
			// 1. 테스트 클래스 동적 생성
			// createOMG33Testable 메소드를 사용해 문자열로 주어진 클래스 이름(testunits items)으로부터 OMG33Testable 타입의 객체를 생성
			OMG33Testable tester = OMGameUtil.createOMG33Testable(testunits[j]);
			// 2. 테스트 시나리오 생성
	        //    생성된 tester 객체의 createTestScenario 메소드를 호출하여 테스트 시나리오(OMG33TestScenario)를 가져옴.
	        //    이 시나리오에는 게임판(board), 금수 지점 리스트(ForbiddenPoints), 허용 지점 리스트(PermittedPoints), 현재 턴(turn) 정보가 포함.
			OMG33TestScenario tempunit = tester.createTestScenario();
			
			// 3. 금수 지점(ForbiddenPoints) 테스트
	        //    시나리오에 정의된 모든 금수 지점에 대해 반복
			for (int k=0; k< tempunit.ForbiddenPoints.size() ; k++ ) 
			{
				Point pt = tempunit.ForbiddenPoints.get(k); // k번째 금수 지점 좌표
				// isForbiddenMove가 true를 반환하면 (즉, 실제로 금수이면) 테스트 통과
				if (isForbiddenMove(tempunit.board, pt.getX(), pt.getY(),tempunit.turn)){
					//System.out.println("[testteam20] Scenario " + j ", Forbidden unit " + k + " passed");
				}else {
					// 실패 메시지 출력
					System.out.println("[testteam20] Scenario " + j + ", Forbidden unit " + k + " failed");
				}
			}
			// 4. 허용 지점(PermittedPoints) 테스트
	        //    시나리오에 정의된 모든 허용 지점에 대해 반복
			for (int k=0; k< tempunit.PermittedPoints.size() ; k++ )// k번째 허용 지점 좌표
			{
				Point pt = tempunit.PermittedPoints.get(k);
				// isForbiddenMove가 true를 반환하면 (즉, 금수이면) 테스트 실패 (허용되어야 하므로)
				if (isForbiddenMove(tempunit.board, pt.getX(), pt.getY(),tempunit.turn)){
					System.out.println("[testteam20] Scenario " + j + ", Permitted unit " + k + " failed");
				}else {
					//System.out.println("[testteam20] Scenario " + j ", Permitted unit " + k + " passed");
				}
			}
		}
	}

	public static boolean checkFinished(int[][] data, int chX, int chY, int turn) {
		// data: 게임판 (2차원 배열)
	    // chX, chY: 마지막으로 돌을 놓은 x, y 좌표
	    // turn: 현재 플레이어 (돌 색깔, 예: 1은 흑, 2는 백) 
		int maxIndex = data[0].length-1; // 게임판의 최대 인덱스 
		int _x = chX;
		int _y = chY;
		int count = 0;
		OMGame.showData(data); // OMGame 클래스의 메소드를 호출하여 현재 게임판 상태 보여줌.	
		OMGame.log_verbose("[verbose] chX:" + chX + ", chY: " + chY + ", turn:" + turn); // 상세 로그 출력
		// 가로 방향 검사 (좌우로 5개 연속인지)
	    // 1. 해당 행에서 가장 왼쪽으로 이동하며 같은 색 돌인지 확인. 결과: 최초의 turn돌의 바로 왼쪽에 위치. 
		while( _y >= OMGame.STARTINDEX && data[_x][_y] == turn ) {
			_y--;
		}
		OMGame.log_verbose("[verbose] [after horizontal searching] _x:" + _x + ", _y:" + _y);
		// 2. 오른쪽으로 이동하며 연속으로 같은 색 돌 개수 카운트
		while( _y < maxIndex && data[_x][++_y] == turn) {
			count++;
			OMGame.log_verbose("[verbose] horizontal 5-check count:" + count);
		}
		if(count == 5) {
			OMGame.log_verbose("[verbose]" + turn + " victory");
			return true; // 5개 연속이면 승리
		}

		// 세로 방향 검사 (위아래로 5개 연속인지) - 로직은 가로와 유사
		_x = chX;
		_y = chY;
		count = 0;
		//System.out.println("chX:" + chX + ", chY: " + chY + ", turn:" + turn);
		while( _x >= OMGame.STARTINDEX && data[_x][_y] == turn ) {
			_x--;
		}
		OMGame.log_verbose("[verbose] [after vertical searching] _x:" + _x + ", _y:" + _y);
		while( _x < maxIndex && data[++_x][_y] == turn ) {
			count++;
			OMGame.log_verbose("[verbose] vertical 5-check count:" + count);
		}
		if(count == 5) {
			OMGame.log_verbose("[verbose] " + turn + " victory");
			return true;
		}


		// 대각선 (좌상단 -> 우하단) 검사 - 로직 유사
		_x = chX;
		_y = chY;
		count = 0;
		while( _x >= OMGame.STARTINDEX && _y >= OMGame.STARTINDEX && data[_x][_y] == turn ) {
			_x--;
			_y--;			
		}
		OMGame.log_verbose("[verbose] [after diagonal-downward searching] _x:" + _x + ", _y:" + _y);
		while(_x < maxIndex && _y < maxIndex && data[++_x][++_y] == turn ) {
			count++;			
			OMGame.log_verbose("[verbose] diagonal-downward 5-check count:" + count);
		}
		if(count == 5) {
			OMGame.log_verbose("[verbose] " + turn + "victory");
			return true;
		}

		// 대각선 (우상단 -> 좌하단) 검사 - 로직 유사
		_x = chX;
		_y = chY;
		count = 0;
		while(_x <= maxIndex && _y >= OMGame.STARTINDEX && data[_x][_y] == turn  ) {
			_x++;
			_y--;			
		}
		OMGame.log_verbose("[verbose] [after diagonal-upward searching] _x:" + _x + ", _y:" + _y);
		while(_x > OMGame.STARTINDEX && _y < maxIndex && data[--_x][++_y] == turn ) {
			count++;			
			OMGame.log_verbose("[verbose] diagonal-upward 5-check count:" + count);
		}
		if(count == 5) {
			OMGame.log_verbose("[verbose] " + turn + "victory");
			return true;
		}

		return false;
	}




    //for the sake of convenience
	public static final int[][] Directions = {
        {1, 0}, {0, 1}, {1, 1}, {1, -1}
    };


    // visualize the board
    public static void visualizeBoard(int[][] board) {
        visualizeBoard(board, -1, -1);
    }

    // visualize the board along with additional point (chX, chY)
    public static void visualizeBoard(int[][] board, int chX, int chY) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i == chX && j == chY) {// 강조할 좌표이면
                    System.out.print("[ " + board[i][j] + " ] ");// 대괄호로 감싸서 출력
                } else {
                    System.out.print("  " + board[i][j] + "   ");// 일반 출력
                }
            }
            System.out.println();// 한 행 출력이 끝나면 줄바꿈
        }
    }

	public static boolean checkThreeThree(int[][] data, int chX, int chY, int turn) {
		/*
		 실제 3-3 검사 로직은 OMGameUtil2 클래스의 checkThreeThree 메소드에 위임
		*/
		 
		return OMGameUtil2.checkThreeThree(data, chX, chY, turn); 
	}


    public static boolean isForbiddenMove(int[][] data, int chX, int chY, int turn) {
    	// 3-3 검사 결과가 곧 금수 여부를 결정
    	return checkThreeThree(data, chX, chY, turn);
    }

    private static OMG33Testable createOMG33Testable(String fullPackageClassname) throws Exception {
        Object object = null; // 1. 문자열로 주어진 클래스 이름을 사용하여 Class 객체 로드
        Class classDefinition = Class.forName(fullPackageClassname); // 2. 로드된 Class 객체로부터 인스턴스 생성 (기본 생성자 호출)
        //    Class.newInstance()는 deprecated 되었으므로, 최신 자바에서는
        //    Constructor.newInstance()를 사용하는 것이 권장
        object = classDefinition.newInstance();
        //생성된 객체를 OMG33Testable 타입으로 형변환하여 반환
        return (OMG33Testable)object;
    }

}






