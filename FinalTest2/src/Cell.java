
/**

 * Class that implements the notion of a cell. The arena is split in the robot's memory into a grid of 6x8 cells.
 * Cells have properties that reflect the real world, i.e, can contain obstacles, may have been visited, have neighbours.
 * 
 * @author Christopher Gurrell
 * @author Joseph Mount
 * @author Minhyung Adrian Kim
 *
 */
public class Cell {
	
	//Declare class variables.
	int x, y, nNeighbours;
	boolean visited, isObstacle, checked;
	
	//Declare class objects.
	Cell parent;			//A reference to the cell's parent.
	Cell[] neighbours;		//An array containing references to the cells neighbours.

	/**
	 * Constructor
	 * 
	 * @param sX
	 * @param sY
	 * @param sVisited
	 * @param sIsObstacle
	 */
	public Cell(int x, int y, boolean visited, boolean isObstacle){
		//Instantiate variables.
		this.x = x;
		this.y = y;
		this.visited = visited;
		this.isObstacle = isObstacle;
		
		parent=null;				//When a new cell is created, it has no parents.
		neighbours = new Cell[4];	//A cell may have up to 4 neighbours.
		nNeighbours = 0;			//Initially, a cell has no neighbours but this will be instantiated in exploration at runtime.
	}
	
	/**
	 * Adds a neighbour to the current cell and increments the count of neighbours.
	 */
	public void addNeighbour(Cell n){
		neighbours[nNeighbours]=n;
		nNeighbours++;
	}
	
	/**
	 * Sets the x coordinate of the cell.
	 * @param x The x coordinate.
	 */
	public void setX(int x){
		this.x= x;
	}
	
	/**
	 * Sets the y coordinate of the cell.
	 * @param y The y coordinate.
	 */
	public void setY(int y){
		this.y = y;
	}
	
	/**
	 * Identifies the cell as having been visited.
	 */
	public void setVisited(){
		visited = true;
	}
	
	/**
	 * Removes the cell's current parent.
	 */
	public void resetParent(){
		parent = null;
	}
	
	/**
	 * Sets the parent of the current cell.
	 * @param p The parent cell.
	 */
	public void setParent(Cell p){
		parent = p;
	}
}
