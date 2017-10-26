import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** provide methods to evaluate board */
public class Eval {
	
	private static final int NUM_ROW = Board.getNumRow();
	private static final int NUM_COL = Board.getNumCol();
	private static final int CONNECT_NUM = Board.getConnectNum();
	private static final String PC = Board.getPC();
	private static final String HUMAN = Board.getHuman();
	private static final String NONE = Board.getNone();
	private static final int WIN_BOOST = 10000;
	private static final Random r = new Random();
	
	private static final Map<Integer, Integer> lenWeightMap = new HashMap<Integer, Integer>(){
		{
			/*
			put(1, 2); //i.e. weight of a connect 1 piece on a board
			put(2, 5); //i.e. weight of a connect 2 piece on a board
			put(3, 20);
			*/
			put(1, 5); //i.e. weight of a connect 1 piece on a board
			put(2, 30); //i.e. weight of a connect 2 piece on a board
			put(3, 70);
		}
	};

	/*
	public Eval(boolean ifNoise){
		this.addNoise = ifNoise; 
	}
	*/
	
	//player1 is the player tries to max
	public static int evalBoard(Board b, String maxPlayer){
		int player1Score = eval(b, maxPlayer);
		int player2Score = eval(b, Board.getOpponent(maxPlayer));
		int noise1 = r.nextInt(5);
		int noise2 = r.nextInt(5);
		//int noise1 = 0;
		//int noise2 = 0;
		return (player1Score + noise1) - (player2Score + noise2);
		//return player1Score - player2Score;
	}
	public static boolean ifPCWon(Board b){
		return checkWinning(b, PC);
	}
	
	public static boolean ifHumanWon(Board b){
		return checkWinning(b, HUMAN);
	}
	
	public static boolean checkTie(Board b){
		for (int i=0; i<NUM_ROW; i++){
			for(int j=0; j < NUM_COL; j++){
				if (b.board[i][j] == NONE)
					return false;
			}
		}
		return true;
	}
	
	public static boolean checkWinning(Board b, String player){
		return checkWinning(b, player, false);
	}
	
	//check if the given side has won
	public static boolean checkWinning(Board b, String player, boolean ifPrint){
		//horizontal
		for (int i=0; i<NUM_ROW; i++){
			for(int j=0; j<= NUM_COL-CONNECT_NUM; j++){
				for(int k = 0; k< CONNECT_NUM; k++){
					if (b.board[i][j+k] != player)
						break;
					if (k == CONNECT_NUM-1){
						if (ifPrint)
							System.out.println(player + " won horizontally");
						return true;
					}
				}
			}
		}
		
		//vertical
		for(int j=0; j<NUM_COL; j++){
			for(int i=0; i<=NUM_ROW-CONNECT_NUM; i++){
				for(int k = 0; k< CONNECT_NUM; k++){
					if(b.board[i+k][j] != player)
						break;
					if(k == CONNECT_NUM-1){
						if (ifPrint)
							System.out.println(player + " won vertically");
						return true;
					}
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
					if(k == CONNECT_NUM-1){
						if (ifPrint)
							System.out.println(player + " won left to right diag");
						return true;
					}
				}
			}
			
			//down right to left
			for (int j= NUM_COL-1; j >= CONNECT_NUM-1; j--){
				for (int k=0; k< CONNECT_NUM; k++){
					if (b.board[i+k][j-k] != player)
						break;
					if(k == CONNECT_NUM-1){
						if (ifPrint){
							System.out.println(player + " won won right to left diag");
							System.out.println("last position " + (i+k) + ", " + (j-k));
						}
						return true;
					}
				}
			}
		}
		
		return false;
	}	

	//given a position i, j, check to see if it's possible to
	//place a piece there
	private static boolean ifPlaceable(Board b, int i, int j){
		return (i == Board.getValidRowPos(b, j));
	}

	
	private static int eval(Board b, String player){
		List<ConnectType> bigLst = new ArrayList<ConnectType>();
		//grab all connected pieces
		//doesn't check for 4-connect. checked in WIN_BOOST
		for (int k = 1; k < CONNECT_NUM; k++){
			List<ConnectType> connectLst = checkForKConnect(b, k, player);
			bigLst.addAll(connectLst);
		}
		
		//calculate score for all the pieces
		//for now, orientation doesn't matter
		int score = 0;
		//boost score if contains winning move
		if (checkWinning(b, player)){
			score += WIN_BOOST;
		}
		//score for length of connected pieces
		for (int i = 0; i < bigLst.size(); i++){
			//bigLst.get(i).print();
			int connectL = bigLst.get(i).len;
			//score += connectL * lenWeightMap.get(connectL); //weighted score for length
			//score += bigLst.get(i).openEnds * bigLst.get(i).openEnds; //score for # open ends
			score += connectL * lenWeightMap.get(connectL)*(bigLst.get(i).openEnds + bigLst.get(i).usableEnds*2); //weighted score for length
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
						int usableEnds = 0;
						//connect: a row, starts@ col j, ends@ col j+k
						/* original
						if ((j-1) >= 0 && b.board[i][j-1] == NONE)
							aliveness += 1;
						if ((j+k+1) < NUM_COL && b.board[i][j+k+1] == NONE)
							aliveness += 1;
						 */
						
						//take placibility into consideration
						if ((j-1) >= 0 && b.board[i][j-1] == NONE){
							aliveness += 1;
							if (ifPlaceable(b, i, j-1))
								usableEnds ++;
						}
						if ((j+k+1) < NUM_COL && b.board[i][j+k+1] == NONE){
							aliveness += 1;
							if (ifPlaceable(b, i, j+k+1))
								usableEnds++;
						}
						
						
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, usableEnds, ConnectType.Orientation.HORIZ, length);
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
						int usableEnds = 0;
						//connect: a col, starts@ row i, ends@ row i+k
						//vert, only need to check top end
						/*
						if ((i-1) >= 0 && b.board[i-1][j] == NONE)
							aliveness += 1;
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, ConnectType.Orientation.VERT, length);
							connectLst.add(aConn);
						}*/
						
						if ((i-1) >= 0 && b.board[i-1][j] == NONE){
							aliveness += 1;
							/*
							if (ifPlaceable(b, i-1, j))
								usableEnds++;
								*/
						}
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, usableEnds, ConnectType.Orientation.VERT, length);
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
						int usableEnds = 0;
						//connect: a diag, starts@ row i col j, ends@ i+k, j+k
						if ((i-1) >=0 && (j-1) >= 0 && b.board[i-1][j-1] == NONE){
							aliveness += 1;
							if (ifPlaceable(b, i-1, j-1))
								usableEnds++;
						}
						if ((i+k+1) < NUM_ROW && (j+k+1) < NUM_COL && b.board[i+k+1][j+k+1] == NONE){
							aliveness += 1;
							if (ifPlaceable(b, i+k+1, j+k+1))
								usableEnds++;
						}
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, usableEnds, ConnectType.Orientation.DIAG, length);
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
						int usableEnds = 0;
						//connect: a diag, starts@ row i col j, ends@ i+k, j-k
						if ((i-1) >=0 && (j+1) < NUM_COL && b.board[i-1][j+1] == NONE){
							aliveness += 1;
							if (ifPlaceable(b, i-1, j+1))
								usableEnds++;
						}
						if ((i+k+1) < NUM_ROW && (j-k-1) > 0 && b.board[i+k+1][j-k-1] == NONE){
							aliveness += 1;	
							if (ifPlaceable(b, i+k+1, j-k-1))
								usableEnds++;
						}
						if (aliveness > 0){
							ConnectType aConn = new ConnectType(aliveness, usableEnds, ConnectType.Orientation.DIAG, length);
							connectLst.add(aConn);
						}

					}
				}
			}
		}
		
		return connectLst;
	}
	
}
