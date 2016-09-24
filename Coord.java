/**
 * The Coord (Co-ordinate) class is used to store positions of other object of the board.
 * @author ElanVB
 *
 */
public class Coord implements Comparable<Coord> {
	/** The instance varibles that describe the Coord */
	private final int row, col, hash;
	
	/**
	 * This method is used to give an ordering to Coords.
	 * @return 0 if the coords are equal, 1 if this coord is greater than otherCoord, -1 otherwise.
	 */
	public int compareTo(Coord otherCoord) {
		return new Integer(this.hash).compareTo(otherCoord.hash);
	}
	
	/**
	 * This is the class Constructor for the Coord class.
	 * @param row The row number that describes this Coord.
	 * @param col The column number that describes this Coord.
	 */
	public Coord(int row, int col) {
		this.row = row;
		this.col = col;
		this.hash = 421*row + 1283*col;
	}
	
	/**
	 * This method is used to see if two Coords are equal.
	 * @return true if these Coords are equal, false otherwise.
	 */
	public boolean equals(Object otherCoord) {
		return this.compareTo((Coord) otherCoord) == 0;
	}
	
	/**
	 * This method is used to get the column number of this Coord.
	 * @return The column number of this Coord.
	 */
	public int getCol() {
		return this.col;
	}
	
	/**
	 * This method is used to get the row number of this Coord.
	 * @return The row number of this Coord.
	 */
	public int getRow() {
		return this.row;
	}
	
	/**
	 * @return A unique number describing this Coord.
	 */
	public int hashCode() {
		return hash;
	}
	
	/**
	 * @return A string representing this Coord.
	 */
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
