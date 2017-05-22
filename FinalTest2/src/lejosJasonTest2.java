import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class lejosJasonTest2 {
	static UltrasonicSensor us;
	static NXTConnection conn;
	static BTSend2 sender;
	static Thread senderThread;
	static ColorHTSensor cs = new ColorHTSensor(SensorPort.S4);

	public static void main(String[] args) throws Exception {
		us = new UltrasonicSensor(SensorPort.S3);
		//int distance;
		int colorID;
		System.out.println("Waiting");
		conn = Bluetooth.waitForConnection();
		System.out.println("Connected");
		sender = new BTSend2(conn);
		senderThread = new Thread(sender);
		senderThread.setDaemon(true);
		senderThread.start();
		int test=0;
		while (true) {
			//distance = us.getDistance();
			colorID = cs.getColorID();
			
			LCD.clear();
			LCD.drawString(colorID + "", 0, 0);
			//Integer[] theList = new Integer[3];
			//theList[0] = distance;
			//theList[1] = 1;
			//theList[2] = 2;
			
			while(!sender.checkIfStart() && test == 0){
				System.out.println("Waiting to start...");
				test = 1;
			}
			
			
			sender.write((int)(Math.random()*10));
			sender.write((int)(Math.random()*10));
			sender.write(colorID);
			Thread.sleep(3000);
		}
	}
}
