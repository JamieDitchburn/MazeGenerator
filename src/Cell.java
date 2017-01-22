import java.util.ArrayList;
import java.util.List;

public class Cell {
	
	private int x, y;
	private boolean wall = false;
	private boolean visited = false;
	private List<Cell> neighbours = new ArrayList<>();
	
	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public List<Cell> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<Cell> neighbours) {
		this.neighbours = neighbours;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isWall() {
		return wall;
	}

	public void setWall(boolean wall) {
		this.wall = wall;
	}

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
