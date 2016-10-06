import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Solver {
	public static void main(String[] args) {
		// loop through and solve each of the test files
		for(int count = 1; count <= 20; count++) {
			// construct the file name
			String filename = "test" + ((count < 10)?"0":"") + count;
			System.out.println("Solving " + filename);
			// create the board from the file
			Board origional = new Board(filename);
			// get the moves to solve the board
			ConcurrentLinkedQueue<Board> path = solve(origional);
			
			// if the path is null then there is no solution
			if(path == null) {
				System.out.println("No Solution");
				continue;
			}
			
			// ------------------------------------------------------------
			// this is only necessary if you test multiple files
			// in one execution of this program
			// when creating cars I provide them all with a unique Id
			// but the red car always gets 0 (the first Id)
			// this Id is simply created by counting up
			// and thus when a new board is to be read in, 
			// the id's must start counting from 0 again
			// thus I reset the Id counter.
			Car.resetId();
			// ------------------------------------------------------------
		}
	}

	/** this method that solves the board and returns a queue of board
	 * states that must be followed to solve the board
	 * This method makes use of Breadth First Search (traversal of boards)
	 * 
	 * @param origin The board that is to be solved
	 * @return The queue showing the board states that need to be followed to solve the board
	 */
	
	public static ConcurrentLinkedQueue<Board> solve(Board origin) {
		// create a data structure that maps board states to the previous board state
		// this is used to follow a path back to the original board state
		HashMap<Board, Board> previousBoardPath = new HashMap<Board, Board>();
		// create a data structure that maps a board state to the number of moves
		// it took to get to that board state from the original board state
		HashMap<Board, Integer> movesFromOrigin = new HashMap<Board, Integer>();
		// create a data structure that stores all board states that have already been visited.
		// This ensures we do not fall into an infinite loop
		HashSet<Board> visited = new HashSet<Board>();
		// create a data structure to store the board states that still need to be explored
		ConcurrentLinkedQueue<Board> queue = new ConcurrentLinkedQueue<Board>();

		// add the original board state to the queue
		queue.add(origin);
		// map the original board state to a null value as it has no previous board linked to it.
		previousBoardPath.put(origin, null);
		// map the original board to 0 moves from the original board.
		movesFromOrigin.put(origin, 0);

		// while there are still board states in the queue we must explore/traverse them
		while(!queue.isEmpty()) {
			// get the next board to explore out of the queue
			Board currentBoard = queue.remove();
			// add it to the set of board states that we have visited
			visited.add(currentBoard);

			// check if this board is in a win state
			if(currentBoard.isWinState()) {
				// if so then:
				// -------------
				// create a data structure to store the path from the original board state to the win state.
				ConcurrentLinkedQueue<Board> path = new ConcurrentLinkedQueue<Board>();
				// create a stack, this is actually just used to reverse the order of the board states
				// as we are presented with the final state and we need to back track to the original state.
				Stack<Board> reversePath = new Stack<Board>();
				// add the win state to the stack
				reversePath.add(currentBoard);
				// get the board state that came before this one (the previous move)
				currentBoard = previousBoardPath.get(currentBoard);

				// while you haven't found the original board state
				while(currentBoard != null) {
					// keep adding the states to the stack
					reversePath.push(currentBoard);
					// and getting the previous states
					currentBoard = previousBoardPath.get(currentBoard);
				}

				// then when you have all the states in the stack
				// reverse the stack and store it in the queue
				while(!reversePath.isEmpty()) {
					path.add(reversePath.pop());
				}

				// print the number of moves it took to solve
				System.out.println("Number of moves to solve is " + (path.size() - 1));
				// return the path.
				return path;
			} else { // if you are not in a win state
					 // add other board states adjacent to this one
				// loop through all the cars in the current board
				// (to do this you need to get all their positions)
				// thus you loop through all the entries (elements) in the mapping data structures
				// and the keys correspond to key value pairs
				// where the keys are the coordinates of the car and the values are the car objects themselves
				for(Entry<Coord, Car> entry : currentBoard.getCarCoords().entrySet()) {
					// get the position on the current car
					Coord currentPosition = entry.getKey();
					
					// check if the car can move down
					if(currentBoard.canMoveDown(currentPosition)) {
						// if so then create a new board state with the car moved one position down
						Board nextBoard = currentBoard.move(currentPosition, "down");
						
						// check if this board state is already in the queue or if it has already been explored
						if(!visited.contains(nextBoard) && !queue.contains(nextBoard)) {
							// if not then add it to the queue
							queue.add(nextBoard);
							// map the new board state to the current board state
							previousBoardPath.put(nextBoard, currentBoard);
							// map the new board state to a distance that is one move more than the current state
							movesFromOrigin.put(nextBoard, movesFromOrigin.get(currentBoard) + 1);
						}
					}

					// Repeat for left
					if(currentBoard.canMoveLeft(currentPosition)) {
						Board nextBoard = currentBoard.move(currentPosition, "left");

						if(!visited.contains(nextBoard) && !queue.contains(nextBoard)) {
							queue.add(nextBoard);
							previousBoardPath.put(nextBoard, currentBoard);
							movesFromOrigin.put(nextBoard, movesFromOrigin.get(currentBoard) + 1);
						}
					}

					// Repeat for right
					if(currentBoard.canMoveRight(currentPosition)) {
						Board nextBoard = currentBoard.move(currentPosition, "right");

						if(!visited.contains(nextBoard) && !queue.contains(nextBoard)) {
							queue.add(nextBoard);
							previousBoardPath.put(nextBoard, currentBoard);
							movesFromOrigin.put(nextBoard, movesFromOrigin.get(currentBoard) + 1);
						}
					}

					// Repeat for up
					if(currentBoard.canMoveUp(currentPosition)) {
						Board nextBoard = currentBoard.move(currentPosition, "up");

						if(!visited.contains(nextBoard) && !queue.contains(nextBoard)) {
							queue.add(nextBoard);
							previousBoardPath.put(nextBoard, currentBoard);
							movesFromOrigin.put(nextBoard, movesFromOrigin.get(currentBoard) + 1);
						}
					}
				}
			}
		}
		
		// if you reach the end of the while loop and no path has been returned yet then there is no solution
		// thus return null
		return null;
	}
}
