/**struct like object, to describe the type of connections in a board
 * given a connection, it's either vertical, horizontal, or diagonal (left to right, or right to left).
 * len is the length of the connection
 * openEnds is 1 or 2
 */
public class ConnectType {
	public static enum Orientation{VERT, HORIZ, DIAG}
	protected int openEnds = 0;
	protected int usableEnds  = 0;
	protected Orientation orient;
	protected int len = 0;
	
	/*
	public ConnectType(int ends, Orientation ori, int lenNum){
		this(ends, 0, ori, lenNum);
	}
	*/
	public ConnectType(int ends, int usableEnds, Orientation ori, int lenNum){
		this.openEnds = ends;
		this.orient = ori;
		this.len = lenNum;
		this.usableEnds = usableEnds;
	}
	
	public void print(){
		System.out.println("len " + len);
		System.out.println("orien " + orient.toString());
		System.out.println("openEnds " + openEnds);
	}
}
