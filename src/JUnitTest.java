import junit.framework.TestCase;

public class JUnitTest extends TestCase {
	/** Tests for class Client 
	 * @throws IOException 
	 * @throws ClassNotFoundException */
	
	public void testBoard(){
		Board b = new Board();
		//test vertical
		b.humanPlay(0);
		b.humanPlay(0);
		b.humanPlay(0);
//		assertEquals(false, Eval.ifHumanWon(b)); 
		assertEquals(false, Eval.ifHumanWon(b)); 
		b.humanPlay(0);
		assertEquals(true, Eval.ifHumanWon(b)); 
		
		//test horizontal 
		b.pcPlay(1);
		b.pcPlay(2);
		assertEquals(false, Eval.ifPCWon(b)); 
		b.pcPlay(3);
		b.pcPlay(4);
		assertEquals(true, Eval.ifPCWon(b)); 
		
		b = new Board();
		//test diagonal left to right
		b.humanPlay(0);
		b.humanPlay(1);
		b.humanPlay(1);
		b.humanPlay(2);
		b.humanPlay(2);
		b.humanPlay(2);
		b.pcPlay(3);
		b.humanPlay(3);
		b.humanPlay(3);
		assertEquals(false, Eval.ifHumanWon(b)); 
		assertEquals(false, Eval.ifPCWon(b)); 
		b.pcPlay(0);
		b.pcPlay(1);
		b.pcPlay(2);
		b.pcPlay(3);
		b.pcPlay(3);
		Board.printBoard(b);
		assertEquals(true, Eval.ifPCWon(b)); 
		
		//test diagonal right to left
		b = new Board();
		b.humanPlay(0);
		b.pcPlay(0);
		b.humanPlay(0);
		b.humanPlay(1);
		b.humanPlay(1);
		b.humanPlay(2);
		assertEquals(false, Eval.ifHumanWon(b)); 
		assertEquals(false, Eval.ifPCWon(b)); 
		//b.saveBoard();
		Board.printBoard(b);
		b.pcPlay(0);
		b.pcPlay(1);
		b.pcPlay(2);
		b.pcPlay(3);
		Board b2 = new Board(b);
		System.out.println("b content before");
		Board.printBoard(b);
		System.out.println("b2 content before");
		Board.printBoard(b2);
		
		assertEquals(true, Eval.ifPCWon(b)); 
		b.pcPlay(1);
		b2.humanPlay(1);
		System.out.println("b content after");
		Board.printBoard(b);

		System.out.println("b2 content after");
		Board.printBoard(b2);
		
		System.out.println("b2 X");
		System.out.println("b X");
		System.out.println("b O");
		Board b3 = new Board();
		assertEquals(true, Board.ifEmpty(b3));
		b3.humanPlay(1);;
		assertEquals(false, Board.ifEmpty(b3));
		assertEquals(true, ("X" == Board.getOpponent("O")));
		assertEquals("O", Board.getOpponent("X"));
	}
}
