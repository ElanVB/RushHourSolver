/**
 * The Car class is an object that stores the given width, height 
 * and allowed movement directions of any car in the game.
 * 
 * @author ElanVB
 * @since 20-09-2016
 */
public class Car implements Comparable<Car> {
	/** A static variable used to assign the unique id's to each car. */
	private static int nextId = 0;
	
	/** The instance variables that describe the car. */
	private final int id, width, height;
	private final boolean[] movementDirections;
	private final String stringDirections;

	/**
	 * This method is used to assign unique ID's to newly created cars.
	 * @return next unique ID.
	 */
	private static int assignId() {
		return nextId++;
	}

	/** 
	 * This method is used to check if the Car is allowed to move down.
	 * @return True if car may move down, false otherwise.
	 */
	public boolean mayMoveDown() {
		return movementDirections[1];
	}
	
	/** 
	 * This method is used to check if the Car is allowed to move left.
	 * @return True if car may move left, false otherwise.
	 */
	public boolean mayMoveLeft() {
		return movementDirections[2];
	}
	
	/** 
	 * This method is used to check if the Car is allowed to move right.
	 * @return True if car may move right, false otherwise.
	 */
	public boolean mayMoveRight() {
		return movementDirections[3];
	}
	
	/** 
	 * This method is used to check if the Car is allowed to move up.
	 * @return True if car may move up, false otherwise.
	 */
	public boolean mayMoveUp() {
		return movementDirections[0];
	}

	/**
	 * This is the Class Constructor for the Car object.
	 * @param width The number of columns the car will span.
	 * @param height The number of rows the car will span
	 * @param directions The directions the car may move in.
	 */
	public Car(int width, int height, String directions) {
		this.id = assignId();
		this.width = width;
		this.height = height;
		this.movementDirections = getDirections(directions.toLowerCase());
		this.stringDirections = getStringDirections(movementDirections);
	}

	/**
	 * This method is used to give an ordering to cars.
	 * @return 0 if the cars are equal, 1 if this car is greater than otherCar, -1 otherwise.
	 */
	public int compareTo(Car otherCar) {
		return (this.id == otherCar.id) ? (0) : ((this.id < otherCar.id) ? (-1) : (1));
	}

	/**
	 * This method takes a string containing the directions that the car
	 * may move in, and converts it into a boolean array describing this.
	 * @param directions The string containing the directions the car may move in.  ("up", "down", "left", "right")
	 * @return A boolean array describing the directions the car may move in.
	 */
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

	/**
	 * This method is used to get the height of the Car.
	 * @return The height of the car.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * This method is used to get the unique ID of the Car.
	 * @return The unique ID of the car.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * This method converts the boolean array describing the directions the car may
	 * move in into a string, this is mostly used for toString and debugging.
	 * @param directions The boolean array describing the directions the car may.
	 * @return A string containing the directions the car may move in.
	 */
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

	/**
	 * This method is used to get the width of the Car.
	 * @return The width of the car.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * This method is used to reset the unique ID counter. This must be done if multiple
	 * between each creation of a new board.
	 */
	public static void resetId() {
		nextId = 0;
	}
	
	/**
	 * This method converts the information that defines this car into a string.
	 * @return A string representing this car.
	 */
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
