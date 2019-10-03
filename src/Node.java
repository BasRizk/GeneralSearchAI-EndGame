import java.util.ArrayList;

public class Node {

	String state;
	Node parentNode;
	String operator;
	int depth;
	int pathCost;
	
	public Node(String state, Node parentNode, String operator, int depth, int pathCost) {
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
	
	public Node applyOperator() {
		// TODO maybe moved to the endgame class
		return null;
	}

}
