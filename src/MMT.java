import java.util.ArrayList;


public class MMT {
	//each node as a board as its state. 
	//var of who is the maxplayer of the tree
	//binary val indicates this node is the max or min player
	//value of this node
	private String maxPlayer;
	private ArrayList<MMT> childLst = null;
	private Integer value = null;
	private Board board;
	private boolean ifMaxPlayer;
	private int parentMove; //move that leads to me
	
	//for outside calling the constructor
	public MMT(Board b, int depth, String mPlayer){
		this(b, depth, mPlayer, true, -1);
	}
	
	//private constructor for generating children
	private MMT(Board b, int depth, String mPlayer, boolean ifMax, int prevMove) {
		board = new Board(b);
		maxPlayer = mPlayer;
		ifMaxPlayer = ifMax;
		parentMove = prevMove;
		//fill in the children
		childLst = generateChildren(board, mPlayer, !ifMax, depth);
		//assign value to node 
		value = getValue(this);
	}
	
	//if I am the max player, then the piece placed in my node is by the min player
	private static String getCurrentNodePiece(String mPlayer, boolean ifIAmMax){
		if (ifIAmMax)
			return Board.getOpponent(mPlayer);
		return mPlayer;
	}
	
	//return a list of children
	private static ArrayList<MMT> generateChildren(Board currentBoard, String mPlayer, boolean ifChildMax, int depthToGo){
		//System.out.println("generate children! depth to go is " + depthToGo);
		if (depthToGo == 0)
			return null;
		ArrayList<MMT> childLst = new ArrayList<MMT>();
		for (int j = 0; j<Board.getNumCol(); j++){
			Board childBoard = new Board(currentBoard);
			boolean success = childBoard.fillBoard(j, getCurrentNodePiece(mPlayer, ifChildMax));
			//Board.printBoard(childBoard);
			//System.out.println("score for this board: " + Eval.evalBoard(childBoard, mPlayer));
			//if valid move, then add board
			if (success){
				//set depth to 0 (to stop) if someone wins or tie
				int depth;
				if (Eval.checkTie(childBoard) || Eval.ifHumanWon(childBoard) ||
						Eval.ifPCWon(childBoard)){
					depth = 0;
				}
				else
					depth = depthToGo -1;
					//depth = depthToGo; //depth not reduced at constructor
				MMT child = new MMT(childBoard, depth, mPlayer, ifChildMax, j);
				childLst.add(child);
			}
		}
		if (childLst.size() == 0)
			return null;
		return childLst;
	}
	
	private static Integer getValue(MMT tree){
		if(tree.value != null)
			return tree.value;
		//evaluate board when it's a leaf node
		if(tree.childLst == null){
			return Eval.evalBoard(tree.board, tree.maxPlayer);
		}
		if (tree.ifMaxPlayer)
			return getMaxChildVal(tree.childLst);
		else
			return getMinChildVal(tree.childLst);
	}
	
	//precondition: childLst is not null
	private static int getMaxChildVal(ArrayList<MMT> childLst){
		//assign values first
		for (MMT tree: childLst){
			if(tree.value == null)
				tree.value = getValue(tree);
		}
		//now get max child val
		int max = childLst.get(0).value;
		for (int i=1; i<childLst.size(); i++){
			if (childLst.get(i).value > max)
				max = childLst.get(i).value;
		}
		return max;
	}
	
	//precondition: childLst is not null
	private static int getMinChildVal(ArrayList<MMT> childLst){
		//assign values first
		for (MMT tree: childLst){
			if(tree.value == null)
				tree.value = getValue(tree);
		}
		//now get min child val
		int min = childLst.get(0).value;
		for (int i=1; i<childLst.size(); i++){
			if (childLst.get(i).value < min)
				min = childLst.get(i).value;
		}
		return min;
	}

	public int getMove() throws Exception{
		if (childLst == null){
			throw new Exception ("tree has no children! can't generate moves!");
		}
		//if empty board, then pick center
		if (Board.ifEmpty(board))
			//return 3;
			return (int) (Math.random()*2+2); //2,3,4 possible start positions
		
		int maxVal = -10000000;
		MMT bestChild = null;
		for(int i=0; i< childLst.size(); i++){
			/*
			if (childLst.get(i).value > maxVal){
				maxVal = childLst.get(i).value;
				bestChild = childLst.get(i);
			}*/
			//pick random if equal
			if (childLst.get(i).value > maxVal ||
					(childLst.get(i).value == maxVal && Math.random() < 0.65)){
				maxVal = childLst.get(i).value;
				bestChild = childLst.get(i);
			}
		}
		return bestChild.parentMove;
	}

}
