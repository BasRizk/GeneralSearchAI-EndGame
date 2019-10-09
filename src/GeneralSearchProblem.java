import java.util.ArrayList;
import java.util.LinkedList;

abstract class GeneralSearchProblem {

	String [] operators;
	Node initialState;
	Node [] stateSpace;
	
	public abstract boolean goalTest(Node n);
	public abstract ArrayList<Node> expandNode(Node n);
	
	public GeneralSearchProblem(Node initialState) {
		this.initialState = initialState;
	}
	
	public void setOperators(String [] operators) {
		this.operators = operators;
	}
	public static void ucsSort(LinkedList<Node> nodesSearchQueue, Node node) {
		    
		   for(int i = 0; i < nodesSearchQueue.size() -1 ; i++) {
		    
			   if(nodesSearchQueue.get(i).getPathCost() >= node.getPathCost()) {
				   nodesSearchQueue.add(i, node);
				   return;
			   }
		   }
		   nodesSearchQueue.addLast(node);
	}
	
	public static String search(GeneralSearchProblem problem, String strategy, boolean verbose) {
		
		LinkedList<Node> nodesSearchQueue = new LinkedList<Node>();
		Node initState = problem.initialState;
		nodesSearchQueue.addLast(initState); 
		
		boolean success = false;
		Node currentNode = null;
		ArrayList<Node> expandedNodes;

		while(true) {
				
			if(nodesSearchQueue.isEmpty()) {
				success = false;
				break;
				//return "Failure";
			}
			
			currentNode = nodesSearchQueue.getLast();
//			System.out.println(nodesSearchQueue.getLast().getOperator());
			
			if(problem.goalTest(currentNode)) {
				success = true;
				break;
			}
			
			// TODO according to strategy expand the currentNode, and
			// enqueue the resulted nodes in the according order. 
			expandedNodes = problem.expandNode(currentNode);
			nodesSearchQueue.removeLast();

			switch(strategy) {
			
			case "BF":
				for(Node node : expandedNodes) {
					nodesSearchQueue.addFirst(node); 
				}
				
				break;
			case "DF":
				for(Node node : expandedNodes) {
					nodesSearchQueue.addLast(node); 
				}
				
				break;
			case "ID":
				nodesSearchQueue.addFirst(initState);
				for(Node node: expandedNodes) {
					nodesSearchQueue.addFirst(node);
				}
			case "UC":
				for(Node node : expandedNodes) {
					ucsSort(nodesSearchQueue, node);
				}
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
			System.out.print(currentNode.getPathCost() + ", ");
			while(true) {
				if(currentNode == null) {
					System.out.println("");
					break;
				}
				System.out.print(currentNode.getOperator() + ", ");
				currentNode = currentNode.getParentNode();
			}
			return "plan;cost;node";
		}
		
		return "Failure";
		
	}
	
}
