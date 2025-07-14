package OMG;

public class OMGameUtil2Team7 {


    public static int[][] copy(int[][] src) {
        if (src == null) {
            return null;
        }
 
        int[][] copy = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = src[i].clone();
        }
 
        return copy;
    }

	
	public static boolean checkThreeThree(int[][] data, int chX, int chY, int turn) {
		int[][] dataCopy = copy(data);
		return checkThreeThreeInner(dataCopy, chX, chY, turn);
	}

    // 
    public static boolean checkThreeThreeInner(int[][] data, int chX, int chY, int turn) {
        int count = 0; // 

        // 
        int[][] dirs = {
            {0, 1},   // 
            {1, 0},   // 
            {1, 1},   // 
            {1, -1}   //
        };

        data[chX][chY] = turn;

        for (int[] d : dirs) {
            if (findOpenThree(data, chX, chY, turn, d[0], d[1])) {
                count++;
            }
        }

        data[chX][chY] = 0;

        return count >= 2;
    }

    private static boolean findOpenThree(int[][] board, int x, int y, int turn, int dx, int dy) {
        int size = board.length; 
        int enemy = (turn == 1) ? 2 : 1; 

        for (int i = -5; i <= 0; i++) {
            int sx = x + i * dx; 
            int sy = y + i * dy; 
            int ex = sx + 5 * dx; 
            int ey = sy + 5 * dy; 

            if (sx < 0 || sy < 0 || sx >= size || sy >= size ||
                ex < 0 || ey < 0 || ex >= size || ey >= size) continue;

            int[] line = new int[6]; 
            boolean blocked = false; 

            for (int j = 0; j < 6; j++) {
                int cx = sx + j * dx;
                int cy = sy + j * dy;
                line[j] = board[cx][cy];
                if (line[j] == enemy) {
                    blocked = true;
                    break;
                }
            }

            if (blocked) continue;


            if (line[0] == 0 && line[1] == turn && line[2] == turn && line[3] == turn && line[4] == 0) return true;

            if (line[1] == 0 && line[2] == turn && line[3] == turn && line[4] == turn && line[5] == 0) return true;

            if (line[0] == 0 && line[1] == turn && line[2] == 0 && line[3] == turn && line[4] == turn && line[5] == 0) return true;

            if (line[0] == 0 && line[1] == turn && line[2] == turn && line[3] == 0 && line[4] == turn && line[5] == 0) return true;
        }

        return false;
    }
}
