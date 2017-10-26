import java.util.Scanner;


public class Main {
	public static void main(String[] args) throws Exception{
		instruction();
		Board b = new Board();
		Board.printBoard(b);
		boolean simulate=false;
		
		AbstractAI ai=new MinMaxAI(6);

		if(simulate){
		 GameSim.simulate(3,4);
		 return;
		}
		
		
		while(true){
			//System.out.println("eval board: " + Eval.evalBoard(b));
			while(true){
				int humanMove = humanTurn();
				if (b.humanPlay(humanMove-1))
					break;
			}
			Board.printBoard(b);
//			if (b.ifHumanWon()){
//note: check ifWon usage has changed. see below for example
			if (Eval.ifHumanWon(b)){
				print("Congrats! You won!");
				break;
			}
			
			print ("AI (" +Board.getPC()+") turn...");
			//int pcMove = ai.move(b);
			//int pcMove = ai.move(b);
			MMT mTree = new MMT(b, 4, "X");
			int pcMove = mTree.getMove();
			//print("ai move is " + (pcMove+1));
			b.pcPlay(pcMove);
			Board.printBoard(b);
			if(Eval.ifPCWon(b)){
				print("You lost!");
				break;
			}
			
			if(Eval.checkTie(b)){
				print("it's a tie!");
				break;
			}
		}
	}

	private static void print(String s){
		System.out.println(s);
	}
	
	private static int humanTurn(){
		Scanner in = new Scanner(System.in);
		while(true){
			print("You are " + Board.getHuman() + ". Please enter your move: ");
			int move = in.nextInt();	
			if(move > 0 && move <= Board.getNumCol())
				return move;
			print("invalid move. please try again");
		}
	}
	
	public static void instruction(){
		System.out.println("welcome to Connect 4!");
	}
}
