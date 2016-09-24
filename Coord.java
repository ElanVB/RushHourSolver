public class Coord implements Comparable<Coord>{
	private final int row, col, hash;
	
	public int compareTo(Coord otherCoord) {
		return new Integer(this.hash).compareTo(otherCoord.hash);
	}
	
	public Coord(int row, int col) {
		this.row = row;
		this.col = col;
		this.hash = 421*row + 1283*col;
	}
	
	public boolean equals(Object otherCoord) {
		return this.compareTo((Coord) otherCoord) == 0;
	}
	
	public int getCol() {
		return this.col;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int hashCode() {
		return hash;
	}
	
	public String toString() {
		StringBuilder stringCoord = new StringBuilder();
		
		stringCoord.append("(");
		stringCoord.append(this.row);
		stringCoord.append(", ");
		stringCoord.append(this.col);
		stringCoord.append(")");
		
		return stringCoord.toString();
	}
}
