import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * 
 * @author Jamie Ditchburn
 *
 * Generates a random maze using depth-first search and a recursive backtracker.
 * https://en.wikipedia.org/wiki/Maze_generation_algorithm
 * 
 */
public class MazeGenerator implements ActionListener, Runnable{

	public static MazeGenerator mazeGenerator;
	static Random rand = new Random();
	
	// Window render
	public final static int WIDTH = 808, HEIGHT = 808;
	public Renderer renderer;
	private static Timer timer;
	
	// Maze vars
	private static int cols = 101; 		// Must be odd
	private static int rows = 101;		//
	private static Cell[][] cells = new Cell[cols][rows];
	private static Set<Cell> unvisited = new HashSet<>(((rows - 1) / 2) * ((cols - 1) / 2));
	private static Stack<Cell> stack = new Stack<>();
	private static List<Cell> neighbours = new ArrayList<>(4);
	private static Cell start;
	private static Cell current;
	
	public MazeGenerator() {
		
		// Render
		JFrame jframe = new JFrame();
		timer = new Timer(20, this);				// Timer to refresh window.
		renderer = new Renderer();
		jframe.add(renderer);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH+6, HEIGHT+29);			// Account for window cutting edges
		jframe.setResizable(false);
		jframe.setTitle("Maze Generator");
		jframe.setVisible(true);
		
		// Maze spawn
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				Cell tempCell = new Cell(x, y);
				if (x % 2 == 0 || y % 2 == 0) {
					tempCell.setWall(true);
				} else {
					unvisited.add(tempCell);
				}
				cells[x][y] = tempCell;
			}
		}
		
		addNeighbours();
		start = cells[1][1];
		visited(start);
		timer.start();
		
	}
	
	// Add neighbours to each cell
	private void addNeighbours() {
		for (Cell[] cellArr: cells) {
			for (Cell cell: cellArr) {
				neighbours = new ArrayList<>(4);
				int x = cell.getX();
				int y = cell.getY();
				if (x < cols - 3) neighbours.add(cells[x+2][y]);		// Right
				if (x > 2) neighbours.add(cells[x-2][y]);				// Left
				if (y < rows - 3) neighbours.add(cells[x][y+2]);		// Down
				if (y > 2) neighbours.add(cells[x][y-2]);				// Up
				cell.setNeighbours(neighbours);
			}
		}
	}
	
	private void visited(Cell cell) {
		cell.setVisited(true);
		unvisited.remove(true);
	}
	
	public void repaint(Graphics g) {
		// Background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Draw and colour grid squares
		for (Cell[] cellArr: cells) {
			for (Cell cell: cellArr) {
				int x = cell.getX();
				int y = cell.getY();
				
				// Set colour of cell to be drawn
				g.setColor(Color.WHITE);
				if (cell.isWall()) g.setColor(Color.BLACK);
				if (cell.isVisited()) g.setColor(Color.WHITE);
				if (cell == current) g.setColor(Color.RED);
				g.fillRect((x * (WIDTH / cols)), (y * (HEIGHT / rows)), (WIDTH / cols), (HEIGHT / rows));
			}
		}
	}
	
	public void actionPerformed(ActionEvent arg0) {
		renderer.repaint();	
	}
	
	public static void main(String[] args) {
		mazeGenerator = new MazeGenerator();
		
		Thread thread = new Thread(mazeGenerator);
		thread.start();
	}

	public void run() {
		
		current = start;
		
		while (unvisited.size() > 0) {

			// Step 1: If the current cell has any neighbours which have not been visited 
			neighbours = current.getNeighbours();
			Iterator<Cell> iterator = neighbours.iterator();
			while(iterator.hasNext()) {
				Cell neighbour = iterator.next();
				if (neighbour.isVisited()) iterator.remove();
			}
			
			if (neighbours.size() > 0) {
				
				// Step 1.1: Choose randomly one of the unvisited neighbours
				int randInt = rand.nextInt(neighbours.size());
				Cell neighbour = neighbours.get(randInt);
				
				// Step 1.2: Push the current cell to the stack
				stack.push(current);
				
				// Step 1.3: Remove the wall between the current cell and the chosen cell
				int x = neighbour.getX();
				int y = neighbour.getY();
				
				if (x == current.getX() + 2 && y == current.getY()) {				
					cells[x - 1][y].setWall(false);
				} else if (x == current.getX() - 2 && y == current.getY()) {		
					cells[x + 1][y].setWall(false);
				} else if (x == current.getX() && y == current.getY() + 2) {		
					cells[x][y - 1].setWall(false);
				} else if (x == current.getX() && y == current.getY() - 2) {		
					cells[x][y + 1].setWall(false);
				}			
				
				// Step 1.4: Make the chosen cell the current cell and mark it as visited
				visited(current);
				current = neighbour;
				
			} else if (stack.size() > 0){
				
				// Step 2: Else if stack is not empty, pop and set current
				visited(current);
				current = stack.pop();
			}
			
			// Sleep so we can watch the maze being built
			try {
				Thread.sleep(2);			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}
