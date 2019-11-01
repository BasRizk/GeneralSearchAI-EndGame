import java.util.ArrayList;

public class Node {

	private ArrayList<Integer> state;
	private Node parentNode;

	private String operator;
	private int depth;
	private int pathCost;

	public Node(ArrayList<Integer> state, Node parentNode, String operator, int depth, int pathCost) {
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
		if (node.parentNode == null) {
			plan.add(node);
			return plan;
		}

		plan.add(node);
		return getPlanRec(plan, node.parentNode);
	}

	public void printDetails() {
		System.out.println("Node Information: ");
		System.out.println("State: ");
		for (Integer value : state) {
			System.out.print(value + ", ");
		}
		System.out.println("");
		System.out.println("Depth: " + depth);
		System.out.println("PathCost: " + pathCost);
		System.out.println("Operator: " + operator);
	}

	public ArrayList<Integer> getState() {
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
