import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SVMData {
	//eval board using level 4
	private static int getMM4Val(Board b){
		MinMaxTree tree=new MinMaxTree(b, 4, -1);
		return tree.getValue();//for "X". bigger better
	}
	
	//generate boards between two level 4 player
	public static void writeSVMData() throws Exception{
		int totalGames = 10000;
		
		FileWriter fstream = new FileWriter("svmMMT4_4.txt");
		BufferedWriter out = new BufferedWriter(fstream);
		
		AbstractAI[] ai=new MinMaxAI[2];
		Random rand = new Random();
		
		String player1 = "X";
		String player2 = "O";
		ai[0]=new MinMaxAI(4, player1);
		ai[1]=new MinMaxAI(4, player2);
		
		int numWin = 0;
		int numLose = 0;
		//for (int i = 0; i< totalGames; i++){
		while(numWin <= totalGames/2 || numLose <= totalGames/2){
			System.out.println("writing game " + (numWin+numLose+1) + " ...");
			int numRound = rand.nextInt(6) + 5; //5-10 moves for per player
			Board b = new Board();
			int move = -1;
			Board savedBoard = null;
			String label;
			int turn = 0;
			while(true){
				move = ai[turn%2].move2(b);
				//System.out.println("current board value: " + Eval.evalBoard(b));
				//Board.printBoard(b);
				
				if(turn%2==0){
					b.fillBoard(move, player1);
				}
				else {
					b.fillBoard(move, player2);
				}
				turn++;
				if (turn == numRound * 2){
					savedBoard = new Board(b);
				}
				if(Eval.ifHumanWon(b)){
					label = "-1";
					numLose++;
					if (savedBoard == null){
						savedBoard = new Board(b);
					}
					break;
				}
				else if(Eval.checkTie(b)){
					label = "0";
					break;
				}
				else if(Eval.ifPCWon(b)){
					label = "+1";
					numWin++;
					if (savedBoard == null){
						savedBoard = new Board(b);
					}
					break;
				}
			}
			//int score = getMM4Val(b);
			//write board data to the file if win or lose
			if ((label == "+1" && numWin <= totalGames/2) || (label == "-1" && numLose <= totalGames/2)){
				String featStr = Board.toSVMFeatureStr(savedBoard);
				out.write(label+featStr+"\n");
				//Board.printBoard(savedBoard);
				//System.out.println(label + featStr);
			}
		}
		out.close();
	}
	
	public static void main(String[] args) throws Exception{
		writeSVMData();
	}
}
	