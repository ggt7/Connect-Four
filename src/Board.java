import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Board for the connect-4 game
public class Board {
	private static final int NUM_ROW = 6;
	private static final int NUM_COL = 7;
	private static final int CONNECT_NUM = 4;
	private static final String PC = "X";
	private static final String HUMAN = "O";
	private static final String NONE = "_";
	private static final int INVALID = -10;
	protected String[][] board = new String[NUM_ROW][NUM_COL];

	/*
	private Map<Integer, Integer> lenWeightMap = new HashMap<Integer, Integer>(){
		{
			put(2, 5);
			put(3, 20);
		}
	};
	*/


	public Board(){
		initalizeBoard();
	}

	
	//copy constructor
	public Board(Board b){
		copyBoard(b);
	}
	
	private void copyBoard(Board b){
		for (int i = 0; i< NUM_ROW; i++){
			for (int j=0; j<NUM_COL; j++){
				this.board[i][j] = b.board[i][j];
			}
		}
	}
	
	private void initalizeBoard(){
		for (int i = 0; i< NUM_ROW; i++){
			for (int j=0; j<NUM_COL; j++){
				this.board[i][j] = NONE;
			}
		}
	}
	
	public static boolean ifEmpty(Board b){
		for (int i = 0; i<NUM_ROW; i++){
			for (int j = 0; j<NUM_COL; j++){
				if (b.board[i][j] != NONE)
					return false;
			}
		}
		return true;
	}
	
	public static String getOpponent(String me){
		if (me.equals(PC))
			return HUMAN;
		return PC;
	}
	
	public static void printBoard(Board b){
		for (int i = 0; i<NUM_ROW; i++){
			String rowStr = "";
			for (int j = 0; j<NUM_COL; j++){
				rowStr += b.board[i][j] + " ";
			}
			System.out.println(rowStr);
		}
		String colStr = "";
		for (int j =0; j< NUM_COL; j++){
			colStr += Integer.toString(j+1) + " ";
		}
		System.out.println(colStr);
		System.out.println("-----------------");
	}
	
	/** a board written to SVM format in "X" perspective
	 * 1 if "X", -1 if "O", else 0
	 * @param b
	 * @return
	 */
	public static String toSVMFeatureStr(Board b){
		String str = ""; 
		for (int i=0; i< NUM_ROW; i++){
			for(int j=0; j<NUM_COL; j++){
				int index = i*7 + j + 1; //svm feature must be >= 1
				String val = "";
				if (b.board[i][j] == PC)
					val = "1";
				else if (b.board[i][j] == HUMAN)
					val = "-1";
				else
					val = "0";
				str += " "+index + ":"+val;
			}
		}
		return str;
	}
	
	/** return symbol for Human Player */
	public static String getHuman(){
		return HUMAN;
	}
	
	/** return symbol for PC Player */
	public static String getPC(){
		return PC;
	}
	
	public static String getNone(){
		return NONE;
	}
	
	/** return the number of columns */ 
	public static int getNumCol(){
		return NUM_COL;
	}
	
	/** return the number of rows*/ 
	public static int getNumRow(){
		return NUM_ROW;
	}

	public static int getConnectNum(){
		return CONNECT_NUM;
	}

	
	//col starts at 0
	private static boolean checkValColPos(int col){
		if (col < 0 || col >= NUM_COL)
			return false;
		return true;
	}
	
	//given the col position, get the valid row position
	public static int getValidRowPos(Board b, int col){
		if (!checkValColPos(col))
			return INVALID;
		int prev = INVALID;
		for (int i = 0; i< NUM_ROW; i++){
			if (b.board[i][col] != NONE){
				return prev;
			}
			prev = i;
		}
		return prev;
	}
	
	/**place the human piece at col, 0 index */
	public boolean humanPlay(int col){
		return fillBoard(col, HUMAN);
	}
	
	/**place the PC piece at col, 0 index */
	public boolean pcPlay(int col){
		return fillBoard(col, PC);
	}
	
	public boolean fillBoard(int col, String player){
		int row = getValidRowPos(this, col);
		if (row == INVALID)
			return false;
		board[row][col] = player;
		return true;
	}
	
	public boolean equals(Board b){
		for(int x=0;x<NUM_ROW;x++)
			for(int y=0;y<NUM_COL;y++)
				if(this.board[x][y]!=b.board[x][y])
					return false;
		return true;
	}
	
	/*
	public boolean ifPCWon(){
		return checkWinning(PC);
	}
	
	public boolean ifHumanWon(){
		return checkWinning(HUMAN);
	}
	
	public boolean checkTie(){
		for (int i=0; i<NUM_ROW; i++){
			for(int j=0; j < NUM_COL; j++){
				if (board[i][j] == NONE)
					return false;
			}
		}
		return true;
	}
	
	//check if the given side has won
	private static boolean checkWinning(Board b, String player){
		//horizontal
		for (int i=0; i<NUM_ROW; i++){
			for(int j=0; j<= NUM_COL-CONNECT_NUM; j++){
				for(int k = 0; k< CONNECT_NUM; k++){
					if (b.board[i][j+k] != player)
						break;
					if (k == CONNECT_NUM-1)
						return true;
				}
			}
		}
		
		//vertical
		for(int j=0; j<NUM_COL; j++){
			for(int i=0; i<=NUM_ROW-CONNECT_NUM; i++){
				for(int k = 0; k< CONNECT_NUM; k++){
					if(b.board[i+k][j] != player)
						break;
					if(k == CONNECT_NUM-1)
						return true;
				}
			}
		}
		
		//diagonal
		for(int i=0; i<=NUM_ROW-CONNECT_NUM; i++){
			//down left to right
			for(int j=0; j<=NUM_COL-CONNECT_NUM; j++){
				for(int k=0; k < CONNECT_NUM; k++){
					if (b.board[i+k][j+k] != player)
						break;
					if(k == CONNECT_NUM-1)
						return true;
				}
			}
			
			//down right to left
			for (int j= NUM_COL-1; j >= CONNECT_NUM-1; j--){
				for (int k=0; k< CONNECT_NUM; k++){
					if (b.board[i+k][j-k] != player)
						break;
					if(k == CONNECT_NUM-1)
						return true;
				}
			}
		}
		
		return false;
	}	

	public static int evalBoard(Board b, String player){
		List<ConnectType> bigLst = new ArrayList<ConnectType>();
		//grab all connected pieces
		for (int k = 2; k < CONNECT_NUM; k++){
			List<ConnectType> connectLst = checkForKConnect(b, k, player);
			bigLst.addAll(connectLst);
		}
		
		//calculate score for all the pieces
		//for now, orientation doesn't matter
		int score = 0;
		//boost score if contains winning move
		if (checkWinning(b, player)){
			score += 10000;
		}
		//score for length of connected pieces
		for (int i = 0; i < bigLst.size(); i++){
			bigLst.get(i).print();
			int connectL = bigLst.get(i).len;
			score += connectL * b.lenWeightMap.get(connectL); //weighted score for length
			score += bigLst.get(i).openEnds * bigLst.get(i).openEnds; //score for # open ends
		}
		return score;
	}
	
	
	//check if k connected piece exists for horizon, vert, and diag
	private static List<ConnectType> checkForKConnect(Board b, int length, String player){
		List<ConnectType> connectLst = new ArrayList<ConnectType>();
		//return a list of connections, alive connect-k (not strict. 2 connect might be contained in 3 connect)
		//later use weights, return weighted sum.
		
		//horizontal
		for (int i=0; i<NUM_ROW; i++){
			for(int j=0; j<= NUM_COL-length; j++){
				for(int k = 0; k< length; k++){
					if (b.board[i][j+k] != player)
						break;
					if (k == length-1){
						//reached length connect, check if alive
						int aliveness = 0;
						//connect: a row, starts@ col j, ends@ col j+k
						if ((j-1) >= 0 && b.board[i][j-1] == NONE)
							aliveness += 1;
						if ((j+k+1) < NUM_COL && b.board[i][j+k+1] == NONE)
							aliveness += 1;
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, ConnectType.Orientation.HORIZ, length);
							connectLst.add(aConn);
						}
					}
				}
			}
		}
		
		//vertical
		for(int j=0; j<NUM_COL; j++){
			for(int i=0; i<=NUM_ROW-length; i++){
				for(int k = 0; k< length; k++){
					if(b.board[i+k][j] != player)
						break;
					if(k == length-1){
						//reached length connect, check if alive
						int aliveness = 0;
						//connect: a col, starts@ row i, ends@ row i+k
						//vert, only need to check top end
						if ((i-1) >= 0 && b.board[i-1][j] == NONE)
							aliveness += 1;
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, ConnectType.Orientation.VERT, length);
							connectLst.add(aConn);
						}
					}
				}
			}
		}
		
		//diagonal
		for(int i=0; i<=NUM_ROW-length; i++){
			//down left to right
			for(int j=0; j<=NUM_COL-length; j++){
				for(int k=0; k < length; k++){
					if (b.board[i+k][j+k] != player)
						break;
					//reached length connect, check if alive
					if(k == length-1){
						int aliveness = 0;
						//connect: a diag, starts@ row i col j, ends@ i+k, j+k
						if ((i-1) >=0 && (j-1) >= 0 && b.board[i-1][j-1] == NONE)
							aliveness += 1;
						if ((i+k+1) < NUM_ROW && (j+k+1) < NUM_COL && b.board[i+k+1][j+k+1] == NONE)
							aliveness += 1;
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, ConnectType.Orientation.DIAG, length);
							connectLst.add(aConn);
						}
					}
				}
			}
			
			//down right to left
			for (int j= NUM_COL-1; j >= length-1; j--){
				for (int k=0; k< length; k++){
					if (b.board[i+k][j-k] != player)
						break;
					if(k == length-1){
						int aliveness = 0;
						//connect: a diag, starts@ row i col j, ends@ i+k, j-k
						if ((i-1) >=0 && (j+1) < NUM_COL && b.board[i-1][j+1] == NONE)
							aliveness += 1;
						if ((i+k+1) < NUM_ROW && (j-k-1) > 0 && b.board[i+k+1][j-k-1] == NONE)
							aliveness += 1;					
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, ConnectType.Orientation.DIAG, length);
							connectLst.add(aConn);
						}

					}
				}
			}
		}
		
		return connectLst;
	}
	*/


}
