package robot;

import jason.asSemantics.*; // to do unification
import jason.asSyntax.*; // to handle AgentSpeak
// syntax.

public class distance2 extends DefaultInternalAction {
	private static final long serialVersionUID = 1L;
	

	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		int term1 = startSearch.comm.read();
		int term2 = startSearch.comm.read();
		int term3 = startSearch.comm.read();
		
		NumberTerm res1 = new NumberTermImpl(term1);
		NumberTerm res2 = new NumberTermImpl(term2);
		NumberTerm res3 = new NumberTermImpl(term3);
		
		//int[] theArr = [res1, res2, res3];
		
		return un.unifies(res1, args[0]) && un.unifies(res2, args[1]) && un.unifies(res3, args[2]);
	}

}
