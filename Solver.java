import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Solver {
	public static void main(String[] args) {
		for(int count = 1; count <= 20; count++) {
			String filename = "test" + ((count < 10)?"0":"") + count;
			System.out.println("Solving " + filename);
			Board origional = new Board(filename);
			ConcurrentLinkedQueue<Board> path = solve(origional);

			if(path == null) {
				System.out.println("No Solution");
				continue;
			}
			Car.resetId();
		}
	}

	public static ConcurrentLinkedQueue<Board> solve(Board origin) {
		HashMap<Board, Board> previousBoardPath = new HashMap<Board, Board>();
		HashMap<Board, Integer> movesFromOrigin = new HashMap<Board, Integer>();
		HashSet<Board> visited = new HashSet<Board>();
		ConcurrentLinkedQueue<Board> queue = new ConcurrentLinkedQueue<Board>();

		queue.add(origin);
		previousBoardPath.put(origin, null);
		movesFromOrigin.put(origin, 0);

		while(!queue.isEmpty()) {
			Board currentBoard = queue.remove();
			visited.add(currentBoard);

			if(currentBoard.isWinState()) {
				ConcurrentLinkedQueue<Board> path = new ConcurrentLinkedQueue<Board>();
				Stack<Board> reversePath = new Stack<Board>();
				reversePath.add(currentBoard);
				currentBoard = previousBoardPath.get(currentBoard);

				while(currentBoard != null) {
					reversePath.push(currentBoard);
					currentBoard = previousBoardPath.get(currentBoard);
				}

				while(!reversePath.isEmpty()) {
					path.add(reversePath.pop());
				}

				System.out.println("Number of moves to solve is " + (path.size() - 1));
				return path;
			} else {
				// add other board states adjacent to this one
				for(Entry<Coord, Car> entry : currentBoard.getCarCoords().entrySet()) {
					Coord currentPosition = entry.getKey();

					if(currentBoard.canMoveDown(currentPosition)) {
						Board nextBoard = currentBoard.move(currentPosition, "down");

						if(!visited.contains(nextBoard) && !queue.contains(nextBoard)) {
							queue.add(nextBoard);
							previousBoardPath.put(nextBoard, currentBoard);
							movesFromOrigin.put(nextBoard, movesFromOrigin.get(currentBoard) + 1);
						}
					}

					if(currentBoard.canMoveLeft(currentPosition)) {
						Board nextBoard = currentBoard.move(currentPosition, "left");

						if(!visited.contains(nextBoard) && !queue.contains(nextBoard)) {
							queue.add(nextBoard);
							previousBoardPath.put(nextBoard, currentBoard);
							movesFromOrigin.put(nextBoard, movesFromOrigin.get(currentBoard) + 1);
						}
					}

					if(currentBoard.canMoveRight(currentPosition)) {
						Board nextBoard = currentBoard.move(currentPosition, "right");

						if(!visited.contains(nextBoard) && !queue.contains(nextBoard)) {
							queue.add(nextBoard);
							previousBoardPath.put(nextBoard, currentBoard);
							movesFromOrigin.put(nextBoard, movesFromOrigin.get(currentBoard) + 1);
						}
					}

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

		// there may be a instance where you reach a winState and it overrides the previous configuration of the same winState
		// and thus you get inaccurate distances, should this arise then you need to calculate the distance of each win state
		// as you go and ignore previous win states with higher distances
		return null;
	}
}
