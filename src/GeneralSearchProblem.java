import java.util.ArrayList;
import java.util.LinkedList;

abstract class GeneralSearchProblem {

	String [] operators;
	Node initialState;
	Node [] stateSpace;
	
	public abstract boolean goalTest(Node n);
	public abstract ArrayList<Node> expandNode(Node n);
	public abstract void resetTree();
	
	public GeneralSearchProblem(Node initialState) {
		this.initialState = initialState;
	}
	
	public void setOperators(String [] operators) {
		this.operators = operators;
	}
	
	public static void ucsSort(LinkedList<Node> nodesSearchQueue, Node node) {
		    
		   for(int i = 0; i < nodesSearchQueue.size(); i++) {
		    
			   if(nodesSearchQueue.get(i).getPathCost() <= node.getPathCost()) {
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
		int depthCounter = 1;
		int numOfExpandedNodes = 0;
		boolean areAllNodesExpanded = true;	// used for ID to check whether the search tree has been expanded completely or are there more nodes to expand

		while(true) {
				
			if(nodesSearchQueue.isEmpty()) {
				success = false;
				break;
			}
			
			currentNode = nodesSearchQueue.removeLast();
			if(problem.goalTest(currentNode)) {
				success = true;
				break;
			}
			
			// According to strategy expand the currentNode, and
			// enqueue the resulted nodes in the according order. 

			switch(strategy) {
			
			case "BF":
				expandedNodes = problem.expandNode(currentNode);
				numOfExpandedNodes++;
				for(Node node : expandedNodes) {
					nodesSearchQueue.addFirst(node); 
				}
				break;
				
			case "DF":
				expandedNodes = problem.expandNode(currentNode);
				numOfExpandedNodes++;
				for(Node node : expandedNodes) {
					nodesSearchQueue.addLast(node); 
				}
				break;
				
			case "ID":
				if(currentNode.getDepth() < depthCounter) {
					expandedNodes = problem.expandNode(currentNode);
					numOfExpandedNodes++;
					
					for(Node node: expandedNodes) {
						nodesSearchQueue.addLast(node);
					}
					
				} else {
					// There are still more nodes deeper in the tree that
					// are yet to be expanded
					areAllNodesExpanded = false;
				}
				
				if(nodesSearchQueue.isEmpty() && !areAllNodesExpanded) {
					nodesSearchQueue.addLast(initState);
					depthCounter++;
					problem.resetTree();
					areAllNodesExpanded = true;
				}
				break;
				
			case "UC":
//				System.out.println("Expand at level " + currentNode.getDepth());
				expandedNodes = problem.expandNode(currentNode);
//				System.out.println("Expanded");
//				System.out.println("Num of Expanded Nodes = " + expandedNodes.size());
				numOfExpandedNodes++;
				for(Node node : expandedNodes) {
					ucsSort(nodesSearchQueue, node);
				}
				break;
				
			case "GR":
				expandedNodes = problem.expandNode(currentNode);
				numOfExpandedNodes++;
				// TODO
				break;
			
			case "AS":
				expandedNodes = problem.expandNode(currentNode);
				numOfExpandedNodes++;
				// TODO
				break;
				
			default: break;
			
			}
		
			if(verbose) {
				// TODO  Show some effects BASED ON visualize
			}
			
		}
		
		if(success) {
			String plan = "";
			String cost = currentNode.getPathCost() + "";
			String nodes = numOfExpandedNodes + "";
			while(true) {
				if(currentNode == null) {
					break;
				}
				if(currentNode.getOperator() != null) {
					plan = currentNode.getOperator() + "," + plan;
				}
				currentNode = currentNode.getParentNode();
			}

			plan = plan.substring(0, plan.length() -1);
			
			System.out.println(plan + ";" + cost + ";" + nodes);
			return plan + ";" + cost + ";" + nodes;
		}
		
		return "Failure";
		
	}
	
}
