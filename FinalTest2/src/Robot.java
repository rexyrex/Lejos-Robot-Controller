
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorHTSensor;

/**
 * Class implementing the movement methods of the robot with support for calibration through constants.
 * 
 * @author Christopher Gurrell
 * @author Joseph Mount
 * @author Minhyung Adrian Kim
 *
 */
public class Robot {
	
	//Declare calibration constants.
	private static final int DISTANCE = 35;
	private static final int US_ANGLE = 660;
	private static final int RIGHT_ANGLE = 90 + 22;
	private static final int LEFT_ANGLE = -(90 + 20);
	
	//Declare and instantiate NXT movement classes to be used by the robot.
	DifferentialPilot dp = new DifferentialPilot(3.22, 19, Motor.B, Motor.C);
	UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
	TouchSensor rightBump = new TouchSensor(SensorPort.S1);
	TouchSensor leftBump = new TouchSensor(SensorPort.S2);
	ColorHTSensor cs = new ColorHTSensor(SensorPort.S4);
	
	/**
	 * Main method to be run for calibration purposes prior to exploration.
	 * 
	 */
	public static void main(String[] args) throws Exception {
		Robot robot = new Robot();
		System.out.print("Press any button to start");
		Button.waitForAnyPress();
		LCD.clear();
		System.out.println("Color ID : "+ robot.getColor());
		Button.waitForAnyPress();
		
		/*robot.rotateRight();	
		robot.rotateLeft();
		robot.rotateSensorRight();
		robot.rotateSensorLeft();*/
	}
	
	public int getColor(){
		return cs.getColorID();
	}
	
	/**
	 * Rotates the ultrasonic sensor right by 90 degrees.
	 */
	public void rotateSensorRight(){
		Motor.A.rotate(US_ANGLE);
	}
	
	/**
	 * Rotates the ultrasonic sensor left by 90 degrees.
	 */
	public void rotateSensorLeft(){
		Motor.A.rotate(-(US_ANGLE));
	}
	
	/**
	 * Moves the robot forward distance equal to one cell.
	 */
	public void forward(){

			dp.travel(DISTANCE);

	}
	
	/**
	 * Moves the robot forward distance equal to half of one cell.
	 */
	public void moveHalf(){
		dp.travel((DISTANCE/2));
	}
	
	/**
	 * Rotates the robot right by 90 degrees.
	 */
	public void rotateRight(){
		dp.rotate(RIGHT_ANGLE);
	}
	
	/**
	 * Rotates the robot left by 90 degrees.
	 */
	public void rotateLeft(){
		dp.rotate(LEFT_ANGLE);
	}
	
	/**
	 * Returns the distance from an object according to the ultrasonic sensor.
	 * 
	 * @return int The distance from an object.
	 */
	public int ultrasonicDistance() {
		return us.getDistance();
	}
}
