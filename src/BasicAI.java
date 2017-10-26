import java.util.Random;


public class BasicAI extends AbstractAI {
	private static Random r = new Random();
	
	public static int randomMove(){
		return r.nextInt(Board.getNumCol());
	}

	@Override
	public int move(Board board) {
		return BasicAI.randomMove();
		
	}
	@Override
	public int move2(Board board) {
		return BasicAI.randomMove();
		
	}
}
