package robot;

import jason.asSemantics.*; // to do unification
import jason.asSyntax.*; // to handle AgentSpeak
// syntax.

public class startSearch extends DefaultInternalAction {
	private static final long serialVersionUID = 1L;
	static Communication2 comm;
	Thread commThread;
	private final int START_CODE = 1;
	
	public startSearch() {
		comm = new Communication2("LEGOBOT-06", "00:16:53:11:16:9d");
		commThread = new Thread(comm);
		commThread.start();
	}

	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		comm.send(START_CODE);
		
		
		//int[] theArr = [res1, res2, res3];
		
		return true;
	}

}

