import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

abstract class GeneralSearchProblem {

	String[] operators;
	Node initialState;
	Node[] stateSpace;

	public abstract boolean goalTest(Node n);

	public abstract ArrayList<Node> expandNode(Node n);

	public abstract void resetTree();

	public abstract void visualise(String plan);

	public GeneralSearchProblem(Node initialState) {
		this.initialState = initialState;
	}

	public void setOperators(String[] operators) {
		this.operators = operators;
	}

	public abstract int heuristic1(Node n);

	public abstract int heuristic2(Node n);

	public static String search(GeneralSearchProblem problem, String strategy, boolean verbose) {

		Comparator<Node> nodeCostComparator = null;

		switch (strategy) {
		case "UC":
			nodeCostComparator = new Comparator<Node>() {
				@Override
				public int compare(Node n1, Node n2) {
					return n1.getPathCost() - n2.getPathCost();
				}
			};
			break;

		case "GR1": // TODO
			nodeCostComparator = new Comparator<Node>() {
				@Override
				public int compare(Node n1, Node n2) {
					return problem.heuristic1(n1) - problem.heuristic1(n2);
				}
			};
			break;

		case "GR2": // TODO
			nodeCostComparator = new Comparator<Node>() {
				@Override
				public int compare(Node n1, Node n2) {
					return problem.heuristic2(n1) - problem.heuristic2(n2);
				}
			};
			break;

		case "AS1":
			nodeCostComparator = new Comparator<Node>() {
				@Override
				public int compare(Node n1, Node n2) {
					return (problem.heuristic1(n1) + n1.getPathCost()) - (problem.heuristic1(n2) + n2.getPathCost());
				}
			};
			break;

		case "AS2":
			nodeCostComparator = new Comparator<Node>() {
				@Override
				public int compare(Node n1, Node n2) {
					return (problem.heuristic2(n1) + n1.getPathCost()) - (problem.heuristic2(n2) + n2.getPathCost());
				}
			};
			break;

		}

		PriorityQueue<Node> priorityQueue = null;
		LinkedList<Node> nodesSearchQueue = null;

		// Data Structure choice
		if (nodeCostComparator != null)
			priorityQueue = new PriorityQueue<>(nodeCostComparator);
		else
			nodesSearchQueue = new LinkedList<Node>();

		Node initState = problem.initialState;

		if (priorityQueue != null) {
			priorityQueue.add(initState);
		} else {
			nodesSearchQueue.addLast(initState);
		}

		boolean success = false;
		Node currentNode = null;
		ArrayList<Node> expandedNodes;
		int depthCounter = 1;
		int numOfExpandedNodes = 0;
		boolean areAllNodesExpanded = true;
		// areAllNodesExpanded ::
		// Used for ID to check whether the search tree has been expanded
		// completely or are there more nodes to expand

		while (true) {

			if (priorityQueue != null) {
				if (priorityQueue.isEmpty()) {
					success = false;
					break;
				}
				currentNode = priorityQueue.remove();
			} else {
				if (nodesSearchQueue.isEmpty()) {
					success = false;
					break;
				}
				currentNode = nodesSearchQueue.removeLast();
			}

			if (problem.goalTest(currentNode)) {
				success = true;
				break;
			}

			// According to strategy expand the currentNode, and
			// enqueue the resulted nodes in the according order.

			switch (strategy) {

			case "BF":
				expandedNodes = problem.expandNode(currentNode);
				numOfExpandedNodes++;
				for (Node node : expandedNodes) {
					nodesSearchQueue.addFirst(node);
				}
				break;

			case "DF":
				expandedNodes = problem.expandNode(currentNode);
				numOfExpandedNodes++;
				for (Node node : expandedNodes) {
					nodesSearchQueue.addLast(node);
				}
				break;

			case "ID":
				if (currentNode.getDepth() < depthCounter) {
					expandedNodes = problem.expandNode(currentNode);
					numOfExpandedNodes++;

					for (Node node : expandedNodes) {
						nodesSearchQueue.addLast(node);
					}

				} else {
					// There are still more nodes deeper in the tree that
					// are yet to be expanded
					areAllNodesExpanded = false;
				}

				if (nodesSearchQueue.isEmpty() && !areAllNodesExpanded) {
					nodesSearchQueue.addLast(initState);
					depthCounter++;
					problem.resetTree();
					areAllNodesExpanded = true;
				}
				break;

			case "UC":
			case "GR1":
			case "GR2":
			case "AS1":
			case "AS2":
				// As already priority function is defined at the beginning of the method
				expandedNodes = problem.expandNode(currentNode);
//				System.out.println("Expanded at level " + currentNode.getDepth());
//				System.out.println("Num of Expanded Nodes = " + expandedNodes.size());
				numOfExpandedNodes++;
				for (Node node : expandedNodes) {
					priorityQueue.add(node);
				}
				break;

			default:
				break;

			}

//			if(verbose) {
			// TODO Show some effects BASED ON visualize
//			}

		}

		if (success) {
			String plan = "";
			String cost = currentNode.getPathCost() + "";
			String nodes = numOfExpandedNodes + "";
			while (true) {
				if (currentNode == null) {
					break;
				}
				if (currentNode.getOperator() != null) {
					plan = currentNode.getOperator() + "," + plan;
				}
				currentNode = currentNode.getParentNode();
			}

			plan = plan.substring(0, plan.length() - 1);

			System.out.println(plan + ";" + cost + ";" + nodes);
			if (verbose) {
				problem.visualise(plan);
			}
			return plan + ";" + cost + ";" + nodes;
		}

		return "Failure";

	}

}
