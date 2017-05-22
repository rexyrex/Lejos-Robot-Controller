
import java.io.IOException;
import java.util.LinkedList;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * Class implementing exploration behaviour via a breadth first search (BFS).
 * As the robot enters each cell, it finds the nearest unvisited cell using the BFS.
 * 
 * @author Christopher Gurrell
 * @author Joseph Mount
 * @author Minhyung Adrian Kim
 *
 */
public class Exploration {
	
	//Declare objects.

	Robot robot;
	NXTConnection conn;
	BTSend2 sender;
	Thread senderThread;
	
	//Declare class objects.
	private static Cell[][] visitedMap;
	private static LinkedList<Cell> moveQueue;
	
	//Declare calculation integers.
	private int posX, posY, heading;
	
	//Declare constants.
	private static final int WIDTH = 4;
	private static final int HEIGHT = 5;
	private static final int OBS_RANGE = 30;
	
	
	
	/**
	 * Constructor.
	 * @param map The map object that holds the world model and occupancy grid.
	 */
	public Exploration(){
		
		//Instantiate calculation integers.
		posX = 0;
		posY = HEIGHT-1;
		heading = 1;
		
		//Instantiate objects.
		robot = new Robot();

		
		//Instantiate class objects.
		visitedMap = new Cell[HEIGHT][WIDTH];
		moveQueue = new LinkedList<Cell>();
	}
	
	/**
	 * Method to be called by controller classes to start exploration of arena.
	 * @throws IOException 
	 */
	public void explore() throws IOException{
		System.out.println("Waiting");
		conn = Bluetooth.waitForConnection();
		System.out.println("Connected");
		sender = new BTSend2(conn);
		senderThread = new Thread(sender);
		senderThread.setDaemon(true);
		senderThread.start();
		
		while(!sender.checkIfStart()){
			System.out.println("Waiting to start...");
		}
		
		this.initDestList();
		this.move();
	}
	
	/**
	 * Instantiate a cell for every coordinate of the grid. 
	 * Create associations of which cells are adjacent (i.e, neighbours).
	 */
	private void initDestList(){
		
		//Creates cells for all coordinates of the grid.
		for(int i=0; i<HEIGHT; i++){
			for(int j=0; j<WIDTH; j++){
				visitedMap[i][j]=new Cell(j,i,false,false);
			}
		}
		
		//Mark the starting cell as visited.
		visitedMap[HEIGHT-1][0].setVisited(); 


		//Loops through all cells creating neighbour associations.
		for(int i=0; i<HEIGHT; i++){
			for(int j=0; j<WIDTH; j++){
				if(i==0){
					visitedMap[i][j].addNeighbour(visitedMap[i+1][j]);
					if(j==0){						
						visitedMap[i][j].addNeighbour(visitedMap[i][j+1]);
					} else if(j==WIDTH-1){
						visitedMap[i][j].addNeighbour(visitedMap[i][j-1]);
					} else {
						visitedMap[i][j].addNeighbour(visitedMap[i][j+1]);
						visitedMap[i][j].addNeighbour(visitedMap[i][j-1]);
					}
				} else if(i==HEIGHT-1){
					visitedMap[i][j].addNeighbour(visitedMap[i-1][j]);
					if(j==0){						
						visitedMap[i][j].addNeighbour(visitedMap[i][j+1]);
					} else if(j==WIDTH-1){
						visitedMap[i][j].addNeighbour(visitedMap[i][j-1]);
					} else {
						visitedMap[i][j].addNeighbour(visitedMap[i][j+1]);
						visitedMap[i][j].addNeighbour(visitedMap[i][j-1]);
					}
				} else if(j==0){
					visitedMap[i][j].addNeighbour(visitedMap[i][j+1]);
					
					if(i==HEIGHT-1){
						visitedMap[i][j].addNeighbour(visitedMap[i-1][j]);
					} else {
						visitedMap[i][j].addNeighbour(visitedMap[i+1][j]);
						visitedMap[i][j].addNeighbour(visitedMap[i-1][j]);
					}
				} else if(j==WIDTH-1){
					visitedMap[i][j].addNeighbour(visitedMap[i][j-1]);			
					visitedMap[i][j].addNeighbour(visitedMap[i+1][j]);
					visitedMap[i][j].addNeighbour(visitedMap[i-1][j]);					
					
				} else {
					visitedMap[i][j].addNeighbour(visitedMap[i][j+1]);
					visitedMap[i][j].addNeighbour(visitedMap[i][j-1]);
					visitedMap[i][j].addNeighbour(visitedMap[i+1][j]);
					visitedMap[i][j].addNeighbour(visitedMap[i-1][j]);
				}				
			}
		}
	}
	
	/**
	 * Returns a path from the robot's current position to an unvisited cell.
	 * 
	 * @param p A destination cell.
	 * @return path A linked list of cells.
	 */
	private LinkedList<Cell> getPath(Cell p){
		LinkedList<Cell> path = new LinkedList<Cell>();

		while(p.parent != null){
			((LinkedList<Cell>)path).add(0, p);; 

			p = p.parent;
		}
		resetAllParents();
		return path;
	}
	
	/**
	 * Uses BFS to find the closest unvisited cell.
	 * 
	 * @param p The robot's current cell.
	 * @return path If a path exists to a non-visited cell, returns that path, otherwise null.
	 */
	private LinkedList<Cell> findNextGoal(Cell p){
		LinkedList<Cell> closedList = new LinkedList<Cell>();
		LinkedList<Cell> openList = new LinkedList<Cell>();
		
		openList.add(p);
		p.parent = null;

		while(!openList.isEmpty()){
			Cell tempPoint = (Cell)openList.remove(0); 
			if(tempPoint.visited==false){
				return getPath(tempPoint);
			} else {
				closedList.add(tempPoint);
				for(int i=0; i<tempPoint.nNeighbours;i++){
					Cell neighbourPoint = tempPoint.neighbours[i];
					if(!closedList.contains(neighbourPoint)&& !openList.contains(neighbourPoint)&& !neighbourPoint.isObstacle){
						neighbourPoint.setParent(tempPoint);
						openList.add(neighbourPoint);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Sets all parent nodes in the array of cells to null.
	 */
	private void resetAllParents(){
		for(int i=0; i<HEIGHT; i++){
			for(int j=0; j<WIDTH; j++){
				visitedMap[i][j].resetParent();
			}
		}
	}
	
	/**
	 * Checks that all cells have been visited or observed.
	 * 
	 * @return boolean True if all cells have been visited, false otherwise.
	 */
	private boolean searchFinished(){
		for(int i=0; i<HEIGHT; i++){
			for(int j=0; j<WIDTH; j++){
				if(visitedMap[i][j].visited == false){
					return false;
				}				
			}
		}
		return true;
	}
	
	/**
	 * Causes the robot to turn in a given direction.
	 * 
	 * @param sHeading The desired direction the robot should turn.
	 */
	private void adjustHeading(int sHeading){
		// 0 = Right, 1 = Top, 2 = Left, 3 = Bottom
		if(Math.abs(heading-sHeading)==2){
			robot.rotateRight();
			robot.rotateRight();
		} else if(heading-sHeading==3){
			robot.rotateLeft();
		} else if(heading-sHeading==-3){
			robot.rotateRight();
		} else if(heading-sHeading==-1){
			robot.rotateLeft();
		} else if(heading-sHeading==1){
			robot.rotateRight(); 
		}
		heading = sHeading; 
	}
	
	/**
	 * Generates a heading and calls the relevant method for the robot to turn towards the destination cell.
	 * 
	 * @param p The destination cell.
	 */
	private void turnToPoint(Cell p){
		int destX = p.x;
		int destY = p.y;
		
		if(posX==destX){
			if(posY>p.y){
				adjustHeading(1);
			} else {
				adjustHeading(3);
			}
		} else if(posY==destY){
			if(posX>p.x){
				adjustHeading(0);
			} else {
				adjustHeading(2);
			}
		}		
	}
	
	/**
	 * Sets the robot position to cell p.
	 * 
	 * @param p The cell.
	 */
	private void updateCurrentPosition(Cell p){
		int cellX = p.x;
		int cellY = p.y;
		
		posX = cellX;
		posY = cellY;
		
	}
	
	
	private boolean isValidSquare(int x, int y){
		if(x>=0 && x<WIDTH && y>=0 && y<HEIGHT){			
			return true;
		} else {
			return false;
		}
	}
	

	
	
	
	/**
	 * Controls the movement of the robot in the arena by calling relevant methods while non-visited cells exist.
	 */
	private void move(){
		while(!searchFinished()){

			if(moveQueue.isEmpty()){
				moveQueue = findNextGoal(visitedMap[posY][posX]);
			}
		
			while(!moveQueue.isEmpty()){
				Cell nextPoint = moveQueue.remove(0);
				System.out.println(" ("+posX+","+posY+")");
				System.out.println("Color: "+ robot.getColor());
				
				
				
				sender.write(posX);
				sender.write(posY);
				sender.write(robot.getColor());
				
				turnToPoint(nextPoint);
				
				//observeSurroundings();
				
				if(robot.ultrasonicDistance() < OBS_RANGE){
					nextPoint.setVisited();
					
					moveQueue = findNextGoal(visitedMap[posY][posX]);

					
					nextPoint.isObstacle=true;
					
				} else {
					robot.forward();
					
					updateCurrentPosition(nextPoint);
					nextPoint.setVisited();

				}
				
			}
		}	
	}
}
