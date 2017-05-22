import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

import lejos.nxt.comm.NXTConnection;

public class BTSend2 implements Runnable {
	Queue<Integer> q;
	
	NXTConnection conn;
	DataOutputStream out;
	DataInputStream dis;
	private final int START_CODE = 1;

	public BTSend2(NXTConnection conn) {
		q = new Queue<Integer>();
		this.conn = conn;
		out = conn.openDataOutputStream();
		dis = conn.openDataInputStream();
	}

	public void run() {
		try {
			while (true) {
				while (q.empty()) {
					Thread.yield();
				}
				out.writeInt((Integer) q.pop());
				out.flush();
			}
		} catch (Exception e) {
		}
	}

	public void write(Integer list) {
		q.push(list);
		
	}
	
	public boolean checkIfStart() throws IOException{
		if(dis.available()>0){
			return dis.readInt()==START_CODE;
		} else {
			return false;
		}
	}

}
