import java.util.Scanner;

/**
 * 
 */

/**
 * 
 *
 */
public class GameSim {

	
	public static void simulate(int d1, int d2) throws Exception{
		
		AbstractAI[] ai=new MinMaxAI[2];
		int simulationNumber;
		int wins=0;
		int loses=0;
		int ties=0;
		int move;
		Board b;
		
		/*
		ai[0]=new MinMaxAI(d1);
		ai[1]=new MinMaxAI(d2);
		*/
		
		String player1 = "O";
		String player2 = "X";
		ai[0]=new MinMaxAI(d1, player1);
		ai[1]=new MinMaxAI(d2, player2);
		
		simulationNumber= 10;
		System.out.println("Running "+simulationNumber+" simulations for " + d1 + " " + d2);
		for(int x=0;x<simulationNumber;x++){
			//System.out.println("sim " + (x+1));
			b=new Board();
			for(int turn=0;true;turn++){
				move=ai[turn%2].move2(b);
				//System.out.println("player move "+move);
				//System.out.println("current board value: " + Eval.evalBoard(b));
				//Board.printBoard(b);
				
				if(turn%2==0){
					//b.humanPlay(move);
					b.fillBoard(move, player1);
				}
				else {
					//b.pcPlay(move);
					b.fillBoard(move, player2);
				}
			
				if(Eval.ifHumanWon(b)){
					//Board.printBoard(b);
					//Eval.checkWinning(b, "O", true);
					//System.out.println("current board value: " + Eval.evalBoard(b));
					wins++;
					break;
				}
				else if(Eval.checkTie(b)){
					//Board.printBoard(b);
					//System.out.println("tie!");
					//System.out.println("current board value: " + Eval.evalBoard(b));
					ties++;
					break;
				}
				else if(Eval.ifPCWon(b)){
					//Board.printBoard(b);
					//Eval.checkWinning(b, "X", true);
					//System.out.println("current board value: " + Eval.evalBoard(b));
					loses++;
					break;
				}
				
			}
			
		}
		
		System.out.println("AI #1 " + d1 + " won "+wins+" times.");
		System.out.println("AI #2 " + d2 + " won "+loses+" times.");
		System.out.println("There were "+ties+" ties.");
		
		
	}
	
	public static void main(String args[]) throws Exception{
		//reyna
		simulate(4,4); //fixed center now (rand for generating bin data) 
		simulate(2,4);
		simulate(4,2);
		
		simulate(3,4);
		simulate(4,3);
		
		simulate(2,3);
		simulate(3,2);
		
		//greg
		simulate(4,5);
		simulate(5,4);
		
		simulate(4,6);
		simulate(6,4);
		
	}
	
}
