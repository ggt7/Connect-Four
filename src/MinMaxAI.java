

/*MinMaxAI
 * Uses a MinMaxTree in order to determine the best move for a given Board.
 * 
 * TODO add support for when a move is impossible i.e. when a child is null
 * TODO try to implement alpha-beta pruning for efficiency
 */
public class MinMaxAI extends AbstractAI {

	//Hashtable<Board,MinMaxTree> lookUpTable=new Hashtable<Board, MinMaxTree>();  Maybe use later for efficiency, would require some changes to MinMaxTree
	int searchDepth;
	String maxPlayer;
	
	public MinMaxAI() {
		searchDepth=4;
		maxPlayer="X";
	}	
	
	public MinMaxAI(int depthToSearch){
		if(depthToSearch<1)depthToSearch=1;
		searchDepth=depthToSearch;
		maxPlayer="X";
	}
	
	public MinMaxAI(int depthToSearch, String mPlayer){
		if(depthToSearch<1)depthToSearch=1;
		searchDepth=depthToSearch;
		maxPlayer=mPlayer;
	}

	@Override
	public int move(Board board) {
		MinMaxTree tree=new MinMaxTree(board, searchDepth, -1,maxPlayer);
		return tree.getMove();
	}
	
	@Override
	public int move2(Board board) throws Exception{
		MMT tree = new MMT(board, searchDepth, maxPlayer);
		return tree.getMove();
	}
	
}

