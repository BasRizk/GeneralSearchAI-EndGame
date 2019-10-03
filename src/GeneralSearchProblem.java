import java.util.LinkedList;

abstract class GeneralSearchProblem {

	String [] operators;
	Node initialState;
	Node [] stateSpace;
	
	public abstract boolean goalTest(Node n);
	public abstract void pathCost();
	
	public static String search(GeneralSearchProblem problem, String strategy, boolean verbose) {
		// TODO maybe move Main search algorithm stuff here instead
		
		LinkedList<Node> nodesSearchQueue = new LinkedList<Node>();
		Node initState = problem.initialState;
		nodesSearchQueue.addLast(initState); 
		
		boolean success = false;
		Node currentNode;
		while(true) {
				
			if(nodesSearchQueue.isEmpty()) {
				success = false;
				break;
				//return "Failure";
			}
			
			currentNode = nodesSearchQueue.getLast();
			
			if(problem.goalTest(currentNode)) {
				success = true;
				break;
			}
			
			// TODO according to strategy expand the currentNode, and
			// enqueue the resulted nodes in the according order. 
			switch(strategy) {
			
			case "BF":
				
			case "DF":
			
			case "ID":
			
			case "UC":
			
			case "GR":
			
			case "AS":
			
			default: break;
			}
		
			if(verbose) {
				// TODO  Show some effects
			}
			
		}
		
		if(success) {
			String plan;
			String cost;
			String nodes;
			return "plan;cost;node";
		}
		
		return "Failure";
		
	}
	
}
