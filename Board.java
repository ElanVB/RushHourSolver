import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Board implements Comparable<Board> {
	private final int rows, cols, hash;
	private final HashMap<Coord, Car> carCoords;
	private final char[][] stringBoard;
	private static final int[] primes = {103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263};

	public Board(String filename) {
		this(new File(filename));
	}

	public Board(File information) {
		int rows = -1;
		int cols = -1;

		ArrayList<Integer> carRow = new ArrayList<Integer>();
		ArrayList<Integer> carCol = new ArrayList<Integer>();
		ArrayList<Car> cars = new ArrayList<Car>();

		try {
			Scanner infoReader = new Scanner(information);

			rows = infoReader.nextInt();
			cols = infoReader.nextInt();
			infoReader.nextLine();

			while(infoReader.hasNextLine()) {
				String position = infoReader.nextLine().trim();
				String dimentions = infoReader.nextLine().trim();
				String movement = infoReader.nextLine().trim();

				int spaceIndex = position.indexOf(' ');
				carRow.add(Integer.parseInt(position.substring(0, spaceIndex)));
				carCol.add(Integer.parseInt(position.substring(spaceIndex + 1, position.length())));

				spaceIndex = dimentions.indexOf(' ');
				cars.add(new Car(Integer.parseInt(dimentions.substring(0, spaceIndex)),
						Integer.parseInt(dimentions.substring(spaceIndex + 1, dimentions.length())),
						movement));

			}
			infoReader.close();
		} catch (FileNotFoundException exception) {
			System.err.println(information.toString() + " could not be found.");
			System.exit(1);
		} catch (NoSuchElementException exception) {
			System.err.println(exception);
			System.exit(1);
		}


		this.rows = rows;
		this.cols = cols;
		this.carCoords = populateCarCoords(carCol, carRow, cars);
		this.stringBoard = generateStringBoard(carCoords);
		this.hash = this.generateHash(this.stringBoard);
	}

	public Board(int rows, int cols, Integer[] carRows, Integer[] carCols, Car[] cars) {
		this.rows = rows;
		this.cols = cols;
		this.carCoords = populateCarCoords(carRows, carCols, cars);
		this.stringBoard = generateStringBoard(carCoords);
		this.hash = this.generateHash(this.stringBoard);
	}

	public Board(int rows, int cols, HashMap<Coord, Car> carCoords) {
		this.rows = rows;
		this.cols = cols;
		this.carCoords = (HashMap<Coord, Car>) carCoords.clone();
		this.stringBoard = generateStringBoard(carCoords);
		this.hash = this.generateHash(this.stringBoard);
	}

	public boolean canMoveDown(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("No car at " + carPosition);
			return false;
		}

		if(!car.mayMoveDown())
			return false;

		int rowIndex = carPosition.getRow() + car.getHeight();
		int startCol = carPosition.getCol();
		int endCol = startCol + car.getWidth();
		for(int colIndex = startCol; colIndex < endCol; colIndex++) {
			if(rowIndex >= this.rows || stringBoard[rowIndex][colIndex] != ' ')
				return false;
		}

		return true;
	}

	public boolean canMoveLeft(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("No car at " + carPosition);
			return false;
		}

		if(!car.mayMoveLeft())
			return false;

		int colIndex = carPosition.getCol() - 1;
		int startRow = carPosition.getRow();
		int endRow = startRow + car.getHeight();
		for(int rowIndex = startRow; rowIndex < endRow; rowIndex++) {
			if(colIndex < 0 || stringBoard[rowIndex][colIndex] != ' ')
				return false;
		}

		return true;
	}

	public boolean canMoveRight(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("No car at " + carPosition);
			return false;
		}

		if(!car.mayMoveRight())
			return false;

		int colIndex = carPosition.getCol() + car.getWidth();
		int startRow = carPosition.getRow();
		int endRow = startRow + car.getHeight();
		for(int rowIndex = startRow; rowIndex < endRow; rowIndex++) {
			if(colIndex >= this.cols || stringBoard[rowIndex][colIndex] != ' ')
				return false;
		}

		return true;
	}

	public boolean canMoveUp(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("No car at " + carPosition);
			return false;
		}

		if(!car.mayMoveUp())
			return false;

		int rowIndex = carPosition.getRow() - 1;
		int startCol = carPosition.getCol();
		int endCol = startCol + car.getWidth();
		for(int colIndex = startCol; colIndex < endCol; colIndex++) {
			if(rowIndex < 0 || stringBoard[rowIndex][colIndex] != ' ')
				return false;
		}

		return true;
	}

	public Board clone() {
		return new Board(this.rows, this.cols, this.carCoords);
	}

	public int compareTo(Board otherBoard) {
		return new Integer(this.hash).compareTo(otherBoard.hash);
	}

	public boolean equals(Object otherBoard) {
		return this.compareTo((Board) otherBoard) == 0;
	}

	private int generateHash(char[][] board) {
		int result = 0;

		for(int row = 0; row < this.rows; row++) {
			result += Arrays.hashCode(board[row]) * Board.primes[row];
		}

		return result;
	}

	private char[][] generateStringBoard(HashMap<Coord, Car> carCoords) {
		char[][] board = new char[this.rows][this.cols];

		for(int row = 0; row < this.rows; row++) {
			for(int col = 0; col < this.cols; col++) {
				board[row][col] = ' ';
			}
		}

		for(Entry<Coord, Car> entry : carCoords.entrySet()) {
			Coord position = entry.getKey();
			Car car = entry.getValue();

			int row = position.getRow();
			int col = position.getCol();
			int height = car.getHeight();
			int width = car.getWidth();
			int carId = car.getId();

			for(int rowIndex = row; rowIndex < row + height; rowIndex++) {
				for(int colIndex = col; colIndex < col + width; colIndex++) {
					if(rowIndex < 0 || colIndex < 0 || rowIndex >= this.rows || colIndex >= this.cols) {
						System.err.println("Invalid board, out of bounds at (" + rowIndex + ", " + colIndex + ") : car - " + car + " " + position);
						System.exit(1);
					}

					if(board[rowIndex][colIndex] == ' ') {
						board[rowIndex][colIndex] = (char) (carId + 48);
					} else {
						System.err.println("Invalid board, position conflict at (" + rowIndex + ", " + colIndex + ") : car - " + car + " " + position);
						System.exit(1);
					}
				}
			}
		}

		return board;
	}

	public HashMap<Coord, Car> getCarCoords() {
		return (HashMap<Coord, Car>) this.carCoords.clone();
	}

	public int hashCode() {
		return this.hash;
	}

	public boolean isWinState() {
		int colIndex = this.cols - 1;
		for (int row = 0; row < this.rows; row++) {
			if(this.stringBoard[row][colIndex] == '0') {
				return true;
			}
		}

		return false;
	}

	public Board move(Coord carPosition, String direction) {
		direction = direction.toLowerCase();
		if(direction.equals("up"))
			return this.moveUp(carPosition);
		else if(direction.equals("down"))
			return this.moveDown(carPosition);
		else if(direction.equals("left"))
			return this.moveLeft(carPosition);
		else if(direction.equals("right"))
			return this.moveRight(carPosition);
		else
			System.err.println(direction + " is not a direction");
		return this.clone();
	}

	public Board move(int row, int col, String direction) {
		direction = direction.toLowerCase();
		if(direction.equals("up"))
			return this.moveUp(row, col);
		else if(direction.equals("down"))
			return this.moveDown(row, col);
		else if(direction.equals("left"))
			return this.moveLeft(row, col);
		else if(direction.equals("right"))
			return this.moveRight(row, col);
		else
			System.err.println(direction + " is not a direction");
		return this.clone();
	}

	public Board moveDown(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Down : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(carPosition.getRow() + 1, carPosition.getCol());
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public Board moveDown(int row, int col) {
		Coord carPosition = new Coord(row, col);
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Down : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(row + 1, col);
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public Board moveLeft(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Left : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(carPosition.getRow(), carPosition.getCol() - 1);
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public Board moveLeft(int row, int col) {
		Coord carPosition = new Coord(row, col);
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Left : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(row, col - 1);
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public Board moveRight(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Right : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(carPosition.getRow(), carPosition.getCol() + 1);
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public Board moveRight(int row, int col) {
		Coord carPosition = new Coord(row, col);
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Right : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(row, col + 1);
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public Board moveUp(Coord carPosition) {
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Up : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(carPosition.getRow() - 1, carPosition.getCol());
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public Board moveUp(int row, int col) {
		Coord carPosition = new Coord(row, col);
		Car car = this.carCoords.get(carPosition);

		if(car == null) {
			System.err.println("move Up : no car at " + carPosition);
			System.exit(1);
		}

		Board newBoard = this.clone();
		newBoard.carCoords.remove(carPosition);
		Coord newPos = new Coord(row - 1, col);
		newBoard.carCoords.put(newPos, car);
		return new Board(this.rows, this.cols, newBoard.carCoords);
	}

	public HashMap<Coord, Car> populateCarCoords(ArrayList<Integer> rows, ArrayList<Integer> cols, ArrayList<Car> cars) {
		HashMap<Coord, Car> carMap = new HashMap<Coord, Car>();

		int numberCars = cars.size();
		for(int car = 0; car < numberCars; car++) {
			int currentRow = rows.get(car);
			int currentCol = cols.get(car);
			Coord position = new Coord(currentRow, currentCol);

			carMap.put(position, cars.get(car));
		}

		return carMap;
	}

	public HashMap<Coord, Car> populateCarCoords(Integer[] rows, Integer[] cols, Car[] cars) {
		HashMap<Coord, Car> carMap = new HashMap<Coord, Car>();

		int numberCars = cars.length;
		for(int car = 0; car < numberCars; car++) {
			int currentRow = rows[car];
			int currentCol = cols[car];
			Coord position = new Coord(currentRow, currentCol);

			carMap.put(position, cars[car]);
		}

		return carMap;
	}

	public String toString() {
		StringBuilder stringBoard = new StringBuilder();

		//		for(int row = 0; row < this.rows; row++) {
		//			stringBoard.append(Arrays.toString(this.stringBoard[row]));
		//			stringBoard.append("\n");
		//		}

		for(int row = this.rows - 1; row >= 0 ; row--) {
			stringBoard.append(Arrays.toString(this.stringBoard[row]));
			stringBoard.append("\n");
		}

		return stringBoard.toString();
	}
}