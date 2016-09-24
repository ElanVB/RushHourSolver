/**
 * The Car class is an object that stores the given width, height 
 * and allowed movement directions of any car in the game.
 * 
 * @author ElanVB
 * @since 20-09-2016
 */
public class Car implements Comparable<Car> {
	private static int nextId = 0;
	private final int id, width, height;
	private final boolean[] movementDirections; // up, down, left, right
	private final String stringDirections;

	private static int assignId() {
		return nextId++;
	}

	public boolean mayMoveDown() {
		return movementDirections[1];
	}
	
	public boolean mayMoveLeft() {
		return movementDirections[2];
	}
	
	public boolean mayMoveRight() {
		return movementDirections[3];
	}
	
	public boolean mayMoveUp() {
		return movementDirections[0];
	}

	public Car(int width, int height, String directions) {
		this.id = assignId();
		this.width = width;
		this.height = height;
		this.movementDirections = getDirections(directions.toLowerCase());
		this.stringDirections = getStringDirections(movementDirections);
	}

	public int compareTo(Car otherCar) {
		return (this.id == otherCar.id) ? (0) : ((this.id < otherCar.id) ? (-1) : (1));
	}

	private boolean[] getDirections(String directions) {
		boolean[] dirs = new boolean[4];

		if(directions.indexOf("up") > -1)
			dirs[0] = true;

		if(directions.indexOf("down") > -1)
			dirs[1] = true;

		if(directions.indexOf("left") > -1)
			dirs[2] = true;

		if(directions.indexOf("right") > -1)
			dirs[3] = true;

		return dirs;
	}

	public int getHeight() {
		return this.height;
	}

	public int getId() {
		return this.id;
	}

	public String getStringDirections(boolean[] directions) {
		StringBuilder stringDirections = new StringBuilder();

		stringDirections.append((movementDirections[0])?"up, ":"");
		stringDirections.append((movementDirections[1])?"down, ":"");
		stringDirections.append((movementDirections[2])?"left, ":"");
		stringDirections.append((movementDirections[3])?"right, ":"");

		if(stringDirections.length() <= 0)
			return "";
		
		return stringDirections.toString().substring(0, stringDirections.length() - 2);
	}

	public int getWidth() {
		return this.width;
	}

	public static void resetId() {
		nextId = 0;
	}
	
	public String toString() {
		StringBuilder stringCar = new StringBuilder();

		stringCar.append("Car ");
		stringCar.append(this.id);
		stringCar.append(": (width - ");
		stringCar.append(this.width);
		stringCar.append(" ; height - ");
		stringCar.append(this.height);
		stringCar.append(") : ");
		stringCar.append(stringDirections);

		return stringCar.toString();
	}
}
