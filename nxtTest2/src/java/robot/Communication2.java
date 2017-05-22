package robot;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import lejos.pc.comm.NXTConnector;

public class Communication2 implements Runnable {
	NXTConnector conn;
	DataOutputStream dos;
	DataInputStream dis;
	String received;
	LinkedBlockingQueue<Integer> q;

	public Communication2(String nxtName, String nxtBTAddress) {
		received = null;
		q = new LinkedBlockingQueue<Integer>();
		// Open up a BlueTooth connection
		conn = new NXTConnector();
		//System.out.println("Checking connection...");
		boolean connected = conn.connectTo(nxtName, nxtBTAddress, 2);
		if (!connected) {
			//System.out.println("Failed to connect!");
			System.err.println("Failed to connect!");
			//System.exit(1);
		}
		System.out.println("Connected to " + nxtName);
		// Set up input (from Bluetooth), and output (to Bluetooth)
		dis = new DataInputStream(conn.getInputStream());
		dos = new DataOutputStream(conn.getOutputStream());
	}

	public void run() {
		try {
			while (true) {
				//int[] arr = new int[3];
				

				q.put(dis.readInt());
				Thread.sleep(100);
			}
		} catch (Exception e) {
		}
	}

	public Integer read() {
		while (q.size() == 0) {
			Thread.yield();
		}
		return q.poll();
	}
	
	public void send(int i) throws IOException{
		dos.writeInt(i);
		dos.flush();
	}
	
	public void close() throws IOException{
		dos.close();
		dis.close();
	}

}
