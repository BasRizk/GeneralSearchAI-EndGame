import java.util.ArrayList;
import java.util.HashMap;

public class EndGame extends GeneralSearchProblem {
	
	int [] worriorsPos;
	int [] stonesPos;
	int [] thanosPos;
//	ArrayList<HashMap<String, Integer>> visitedStates;
	int gridWidth;
	int gridHeight;
	int numOfWorriors;
	
	/**
	 * EndGame search tree consists of Nodes, just like any search tree;
	 * the only difference, that make these Node belong to the EndGame
	 * problem specifically is the format of the state of its node.
	 * 
	 * this class for example relays on the Node class, which is basically
	 * a generic search tree node, however, as the state of the node is simply
	 * a generic HashMap <String, ?>, hence, we can have our own format to
	 * this state second term of the pair to afford our problem state;
	 * 
	 * therefore, our EndGame state contains:
	 * 
	 * iX, iY (Location of Iron Man)
	 * (s1C)ollected, s2C., s3C., s4C., s5C., s6C.,
	 * (w1K)illed, w2K, w3k, w4k, w5k	
	 * 
	 * The damage taken by Iron man at a certain node will be represented by 
	 * the path cost to that node.
	 * 
	 * @param initialState
	 */
	public EndGame(int [] ironmanPos, int gridWidth, int gridHeight,
			int [] worriorsPos, int [] stonesPos, int [] thanosPos) {
		
		super(createInitialState(ironmanPos, worriorsPos.length / 2));
		String [] operators = new String []{
				"up", "down", "left", "right", 
				"collect", "kill", "snap"};
		this.setOperators(operators);
		
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.worriorsPos = worriorsPos;
		this.stonesPos = stonesPos;
		this.thanosPos = thanosPos;
		this.numOfWorriors = worriorsPos.length / 2;
		
//		this.visitedStates = new ArrayList<HashMap<String,Integer>>();
	}
	
	private static Node createInitialState(int [] ironmanPos, int numOfWorriors) {
		HashMap<String, Integer> state; Node parentNode;
		int depth; int pathCost;

		state = new HashMap<String, Integer>();
		state.put("iX",ironmanPos[0]); state.put("iY",ironmanPos[1]);
		for(int i = 1; i <= 6; i++) {
			state.put("s" + i + "C",0);
		}
		for(int i = 1; i <= numOfWorriors; i++) {
			state.put("w" + i + "K",0);
		}
		parentNode = null;
		depth = 1;
		pathCost = 0;
		
		return new Node(state, parentNode, null, depth, pathCost);		
	}
	
	@Override
	public boolean goalTest(Node node) {
		if( node.getPathCost() < 100 &&
			node.getOperator() == "snap") {
			return true;
		}
		
		return false;
	}		

	public Node applyOperator(Node node, String operator) {
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> nodeState = new HashMap<String, Integer>();		
		HashMap<String, Integer> oldState = (HashMap<String, Integer>) node.getState();
		
		// Initialize node state with the previous state values
		nodeState.put("iX", oldState.get("iX")); nodeState.put("iY", oldState.get("iY"));
		for(int i = 1; i <= 6; i++) {
			nodeState.put("s" + i + "C", oldState.get("s" + i + "C"));
//			System.out.println(nodeState.get("s" + i + "C"));
		}
		for(int i = 1; i <= numOfWorriors; i++) {
			nodeState.put("w" + i + "K", oldState.get("w" + i + "K"));
//			System.out.println(nodeState.get("s" + i + "C"));
		}
		
//		System.out.println(operator);
//		System.out.println(nodeState.get("iX"));
//		System.out.println(nodeState.get("iY"));
//		System.out.println(node.getPathCost());
//		System.out.println(node.getDepth());
//		System.out.println("--");


		int cost = node.getPathCost();
		int depth = node.getDepth() + 1;
		
		// TODO is this allowed?
		if(cost > 100) {
			return null;
		}

		Integer newValue;
		switch(operator) {
		case "collect": 
			// collecting stone decreases HP by 3
			for(int i = 1; i <= 6; i++) {
				if( nodeState.get("s" + i + "C") == 0 && 
					nodeState.get("iX") == this.stonesPos[(i-1) * 2] &&
					nodeState.get("iY") == this.stonesPos[((i-1) * 2) + 1]) {
					cost += 3;
					nodeState.put("s" + i + "C", 1);
					break;
				} else if(i == 6) {
					return null;
				}
			}
			
			break;

		case "snap":
			if(	this.thanosPos[0] == nodeState.get("iX") &&
			  	this.thanosPos[1] == nodeState.get("iY")) {
				
				for(int i = 1; i <= 6; i++) {
					if( nodeState.get("s" + i + "C") == 0) {
						return null;
					};
				}
			
				return new Node(nodeState, node, operator, depth, cost); 
				
			} else {
				return null;
			}

		case "kill":
			// killing warrior decreases HP by 2
			boolean isWorriorFound = false;
			
			for(int i = 1; i <= this.numOfWorriors; i++) {
				if(nodeState.get("w" + i + "K") == 0) {
					if( (this.worriorsPos[(i-1) * 2] - nodeState.get("iX") == 1) ||
						(this.worriorsPos[(i-1) * 2] - nodeState.get("iX") == -1)) {
						
						if(this.worriorsPos[((i-1) * 2) + 1] == nodeState.get("iY")) {
							isWorriorFound = true;
							cost += 2;
							nodeState.put("w" + i + "K", 1);
						}
						
					} else if(this.worriorsPos[(i-1) * 2] == nodeState.get("iX")) {
						
						if( (this.worriorsPos[((i-1) * 2) + 1] - nodeState.get("iY") == 1) || 
							(this.worriorsPos[((i-1) * 2) + 1] - nodeState.get("iY") == -1)) {
							
							isWorriorFound = true;
							cost += 2;
							nodeState.put("w" + i + "K", 1);
							
						}
						
					}
				}
			}
			
			if(!isWorriorFound) {
				return null;
			}

			break;

		case "up":
			newValue = (Integer) nodeState.get("iX");
			newValue--;
			if ( newValue >= 0) {
				for(int i = 1; i <= this.numOfWorriors; i++) {
					if(nodeState.get("w" + i + "K") == 0) {
						if( (this.worriorsPos[(i-1) * 2] == newValue) &&
							(this.worriorsPos[((i-1) * 2) + 1] == nodeState.get("iY"))) {
							return null;
						}
					}
				}
				nodeState.put("iX", newValue);				
			} else {
				return null;
			}
			break;
			
		case "down":
			newValue = (Integer) nodeState.get("iX");
			newValue++;
			if ( newValue < gridHeight) {
				for(int i = 1; i <= this.numOfWorriors; i++) {
					if(nodeState.get("w" + i + "K") == 0) {
						if( (this.worriorsPos[(i-1) * 2] == newValue) &&
							(this.worriorsPos[((i-1) * 2) + 1] == nodeState.get("iY"))) {
							return null;
						}
					}
				}
				nodeState.put("iX", newValue);
			} else {
				return null;
			}
			break;
			
		case "left":
			newValue = (Integer) nodeState.get("iY");
			newValue--;
			if ( newValue >= 0) {
				for(int i = 1; i <= this.numOfWorriors; i++) {
					if(nodeState.get("w" + i + "K") == 0) {
						if( (this.worriorsPos[(i-1) * 2] == nodeState.get("iX")) &&
							(this.worriorsPos[((i-1) * 2) + 1] == newValue)) {
							return null;
						}
					}
				}
				nodeState.put("iY", newValue);
			} else {
				return null;
			}
			break;
			
		case "right": 
			newValue = (Integer) nodeState.get("iY");
			newValue++;
			if ( newValue < gridWidth) {
				for(int i = 1; i <= this.numOfWorriors; i++) {
					if(nodeState.get("w" + i + "K") == 0) {
						if( (this.worriorsPos[(i-1) * 2] == nodeState.get("iX")) &&
							(this.worriorsPos[((i-1) * 2) + 1] == newValue)) {
							return null;
						}
					}
				}
				nodeState.put("iY", newValue);
			} else {
				return null;
			}
			break;	
					
		}
		
		// warriors hits ironman by 1 hp each
		for(int i = 1; i <= this.numOfWorriors; i++) {
			if(nodeState.get("w" + i + "K") == 0) {
				if( (this.worriorsPos[(i-1) * 2] - nodeState.get("iX") == 1) ||
					(this.worriorsPos[(i-1) * 2] - nodeState.get("iX") == -1)) {
					
					if(this.worriorsPos[((i-1) * 2) + 1] == nodeState.get("iY")) {
						
						cost++;
						
					}
					
				} else if(this.worriorsPos[(i-1) * 2] == nodeState.get("iX")) {
					
					if( (this.worriorsPos[((i-1) * 2) + 1] - nodeState.get("iY") == 1) || 
						(this.worriorsPos[((i-1) * 2) + 1] - nodeState.get("iY") == -1)) {
						
						cost++;
						
					}
					
				}
			}
		}
		
		// thanos hits ironman by 5 hp
		if( (this.thanosPos[0] - nodeState.get("iX") == 1) || 
			(this.thanosPos[0] - nodeState.get("iX") == -1)) {
			
			if(this.thanosPos[1] == nodeState.get("iY")) {
				
				cost += 5;
				
			}
				
		} else if(this.thanosPos[0] == nodeState.get("iX")) {
			
			if( (this.thanosPos[1] - nodeState.get("iY") == 1) ||
				(this.thanosPos[1] - nodeState.get("iY") == -1)) {
				
				cost += 5;
				
			}
			
		} else if(this.thanosPos[0] == nodeState.get("iX") &&
				  this.thanosPos[1] == nodeState.get("iY")) {
				
			cost += 5;
			
		}
		
//		System.out.println("Pass");

		return new Node(nodeState, node, operator, depth, cost);
	}
	
	public ArrayList<Node> expandNode(Node node) {
		
		Node newNode;
		ArrayList<Node> expandedNodes = new ArrayList<Node>();
		for(int i = 0; i < operators.length; i ++) {
			newNode = applyOperator(node, operators[i]);
			
			// aim to stop repeating states
			// TODO maybe will need to use a more efficient way
//			if(newNode != null && this.visitedStates.contains(newNode.getState())) {
			if(newNode != null && isRepeatedState(newNode, newNode.getState())) {
				newNode = null;
			}
			
			if(newNode != null) {
				expandedNodes.add(newNode);
//				this.visitedStates.add((HashMap<String, Integer>) newNode.getState());
			} else {
				//TODO
			}
		}
		return expandedNodes;
	}
	
	public boolean isRepeatedState(Node node, HashMap<String, ?> state) {
		Node parentNode = node.getParentNode();
		
		if(parentNode == null) {
			return false;
		}
		else if(parentNode.getState().equals(state)) {
			return true;
		} else {
			return isRepeatedState(parentNode, state);
		}
	}

}