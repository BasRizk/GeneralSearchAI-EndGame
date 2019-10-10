import java.util.ArrayList;
import java.util.HashMap;

public class Node {

	private HashMap<String, Integer> state;
	private Node parentNode;

	private String operator;
	private int depth;
	private int pathCost;
	
	public Node(HashMap<String, Integer> state, Node parentNode, String operator, int depth, int pathCost) {
		this.state = state;
		this.parentNode = parentNode;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
	}
	
	public ArrayList<Node> getPlan() {
		return getPlanRec(new ArrayList<Node>(), this);
	}
	
	private static ArrayList<Node> getPlanRec(ArrayList<Node> plan, Node node) {
		if(node.parentNode == null) {
			plan.add(node);
			return plan;
		} 
		
		plan.add(node);
		return  getPlanRec(plan, node.parentNode);
	}

	public HashMap<String, Integer> getState() {
		return state;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public String getOperator() {
		return operator;
	}

	public int getDepth() {
		return depth;
	}

	public int getPathCost() {
		return pathCost;
	}
}
