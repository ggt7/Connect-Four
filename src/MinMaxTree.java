/**
 * 
 */
import java.util.ArrayList;

/**
 * Fills in Tree to decide AI's proper move
 *TODO maybe work on initialize() placement in  findMove rather than constructor
 */
/* MinMaxTree
 * 
 * 
 * 
 */
public class MinMaxTree {

	private ArrayList<MinMaxTree> children=new ArrayList<MinMaxTree>(Board.getNumCol());
	private Board board;
	private int depth;
	
	private String symbol;
	
	private int value;
	private MinMaxTree bestChild;
	private int bestColumn=-1;
	private int parentMove = -1; //move that leads to me
	
	public MinMaxTree(Board b, int treeDepth){
		board=b;
		depth=treeDepth;
		parentMove = -1;
		this.initialize();
	}
	public MinMaxTree(Board b, int treeDepth, int prevMove){
		board=b;
		depth=treeDepth;
		parentMove = prevMove;
		this.initialize();
	}
	
	public MinMaxTree(Board b, int treeDepth, int prevMove, String piece){
		board=b;
		depth=treeDepth;
		parentMove = prevMove;
		this.initialize();
		symbol=piece;
	}
	
	/* Assumes children are in order where index 0 means move in column 0
	 * 
	 * @return the column of the ideal move
	 */
	public int getMove() {
		if(bestColumn==-1)
			this.getValue();
		//return children.indexOf(bestChild);
		
		if(this.board.equals(new Board())){
			//return 3;//(int)(Math.random()*3+3);
			return (int) (Math.random()*2+2); //2,3,4 possible start positions
		}
		
		this.value=this.alphaBeta(Integer.MIN_VALUE,Integer.MAX_VALUE);
		
		ArrayList<MinMaxTree> bests=new ArrayList<MinMaxTree>();
		for(MinMaxTree c:children)
			if(c.getValue()==this.value)
				bests.add(c);
		int index=(int)(Math.random()*bests.size());
		
		return bests.get(index).parentMove;
	}
	

	/*Finds all the children of current tree 
	 * 
	 * @param depth the Depth to which this tree will go
	 */
	private void initialize(){
		if(depth==0||(Eval.ifPCWon(board))||(Eval.ifHumanWon(board))||(Eval.checkTie(board))){
			bestChild=null;
			value=Eval.evalBoard(board, symbol);
			depth=0;
			return;
		}
		//Fill Children
		Board childBoard;
		for(int x=0;x<Board.getNumCol();x++){
			
			childBoard=new Board(board);
			boolean ifValMove;
			if (depth%2==0)
				ifValMove = childBoard.pcPlay(x);
			else 
				ifValMove = childBoard.humanPlay(x);

			if (ifValMove)
				children.add(new MinMaxTree(childBoard,depth-1, x));
		}
				
	}
	
	private MinMaxTree findMinChild(){
		MinMaxTree min=children.get(0);
		bestColumn=0;
		//int index=0;
		for(MinMaxTree c:children){
			if(c.getValue()<min.getValue()){
				min=c;
				//bestColumn=index;
				bestColumn=c.parentMove;
			}
		//index++;
		}
		
		return min;
	}
	
	private MinMaxTree findMaxChild(){
		MinMaxTree max=children.get(0);
		bestColumn=0;
		//int index=0;
		for(MinMaxTree c:children){
			if(c.getValue()>max.getValue()){
					max=c;
					//bestColumn=index;
					bestColumn=c.parentMove;
			}
			//index++;
		}
			
		return max;
	}
	
	private void findValue(){
		//Maximize at an even depth, minimize at odd
			if(depth%2==0)
				bestChild=this.findMaxChild();
			else bestChild=this.findMinChild();
					

		value=bestChild.getValue();		
	}
	
	/*
	 * Find value and return if not already found
	 */
	public int getValue(){
		return value;
	}
	
	private int alphaBeta(int alpha, int beta){
		if(this.depth==0)return value;
		
		if(this.depth%2==0){
			for(MinMaxTree c:children){
				alpha=Math.max(alpha, c.alphaBeta(alpha,beta));
				if(beta<=alpha){
					break;
				}
			}
			this.value=alpha;
			return alpha;
		}
		else{
			for(MinMaxTree c:children){
				beta=Math.min(beta, c.alphaBeta(alpha,beta));
				if(beta<=alpha){
					break;
				}
			}
			this.value=beta;
			return beta;	
		}
		
	}
	
	
	
	
	
	
}
