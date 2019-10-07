import java.util.ArrayList;
import java.util.HashMap;

public class EndGame extends GeneralSearchProblem {

//	HashMap<String, Boolean> worriorsKilled;
//	HashMap<String, Boolean> stonesCollected;
	
	int [] worriorsPos;
	int [] stonesPos;
	int [] thanosPos;
	int gridWidth;
	int gridHeight;
	
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
	 * iX, iY, HP,
	 * (s1C)ollected, s2C., s3C., s4C., s5C., s6C.,
	 * (w1K)illed, w2K, w3k, w4k, w5k	
	 * 
	 * @param initialState
	 */
	public EndGame(int [] ironmanPos, int gridWidth, int gridHeight,
			int [] worriorsPos, int [] stonesPos, int [] thanosPos) {
		
		super(createInitialState(ironmanPos));
		String [] operators = new String []{
				"up", "down", "left", "right", 
				"collect", "kill", "snap"};
		this.setOperators(operators);
	}
	
	private static Node createInitialState(int [] ironmanPos) {
		HashMap<String, Integer> state; Node parentNode;
		int depth; int pathCost;

		state = new HashMap<String, Integer>();
		state.put("iX",ironmanPos[0]); state.put("iY",ironmanPos[1]);
		state.put("hp",100); //HP
		for(int i = 1; i <= 6; i++) {
			state.put("s" + i + "C",0);
		}
		for(int i = 1; i <= 6; i++) {
			state.put("w" + i + "K",0);
		}
		parentNode = null;
		depth = 1;
		pathCost = 0;

		
		return new Node(state, parentNode, null, depth, pathCost);		
		
	}
	
	@Override
	public boolean goalTest(Node node) {
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> nodeState =
				(HashMap<String, Integer>) node.getState();
		for(int i = 1; i <= 6; i++) {
			if( nodeState
					.get("s" + i + "C") == 0) {
				return false;
			};
		}
		if( nodeState.get("iX") == thanosPos[0] &&
			nodeState.get("iY") ==  thanosPos[1] &&
			nodeState.get("hp") >= 100) {
			return true;
		}
		
		return false;
	}		

	public Node applyOperator(Node node, String operator) {
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> nodeState = 
				(HashMap<String, Integer>) node.getState().clone();

		Integer newValue;
		switch(operator) {
		case "up":
			newValue = (Integer) nodeState.get("iX");
			newValue--;
			if ( newValue >= 0) {
				nodeState.put("iX", newValue);
			} else {
				return null;
			}
			break;
			
		case "down":
			newValue = (Integer) nodeState.get("iX");
			newValue++;
			if ( newValue <= gridHeight) {
				nodeState.put("iX", newValue);
			} else {
				return null;
			}
			break;
			
		case "left":
			newValue = (Integer) nodeState.get("iY");
			newValue--;
			if ( newValue >= 0) {
				nodeState.put("iY", newValue);
			} else {
				return null;
			}
			break;
			
		case "right": 
			newValue = (Integer) nodeState.get("iY");
			newValue++;
			if ( newValue <= gridWidth) {
				nodeState.put("iY", newValue);
			} else {
				return null;
			}
			break;
			
		case "collect": 
			// collecting stone decreases HP by 3

			break;
		case "kill":
			// killing warrior increases HP by 2

			break;
		case "snap": break;
		}
		// thanos hits ironman by 5 hp
		return null;
	}
	
	public ArrayList<Node> expandNode(Node node) {
		
		Node newNode;
		ArrayList<Node> expandedNodes = new ArrayList<Node>();
		for(int i = 0; i <= operators.length; i ++) {
			newNode = applyOperator(node, operators[i]);
			
			// aim to stop repeating states
			for(Node expandedNode : expandedNodes) {
				if(expandedNode.getState().equals(newNode.getState())) {
					newNode = null;
					break;
				}
			}
			
			if(newNode != null) {
				expandedNodes.add(newNode);
			} else {
				//TODO
			}
		}
		return expandedNodes;
	}

}