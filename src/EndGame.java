import java.util.ArrayList;
import java.util.HashMap;

public class EndGame extends GeneralSearchProblem {
	
	int [] warriorsPos;
	int [] stonesPos;
	int [] thanosPos;
	boolean areStonesCollected;
	HashMap<ArrayList<Integer>, Integer> visitedStates;
	int gridWidth;
	int gridHeight;
	int numOfWorriors;
	
	ArrayList<Integer> nodeState;		
	ArrayList<Integer> oldState;
	int cost;
	int depth;
	int newValue;
	boolean isWorriorFound;
	Node newNode; Integer bestPastSimilarNodeCost;
	ArrayList<Node> expandedNodes;
	
	/**
	 * EndGame search tree consists of Nodes, just like any search tree;
	 * the only difference, that make these Node belong to the EndGame
	 * problem specifically is the format of the state of its node.
	 * 
	 * this class for example relays on the Node class, which is basically
	 * a generic search tree node (ArrayList<Integer>).
	 * 
	 * The format of EndGame state will be as follows:
	 * 
	 * ----
	 * At 0,1 - X and Y positions, respectively, of Ironman.
	 * 
	 * At i+1, i from 1 to 6, the state of each infinity stone (i),
	 * where 0 denotes that the stone is not collected,
	 * and 1 denotes that the stone is collected.
	 * 
	 * At i+7, i from 1 to 5 (or more than 5), the state of each warrior (i),
	 * where 0 denotes that the warrior is not killed,
	 * and 1 denotes that the warrior is killed.
	 * ----
	 * 
	 * The damage taken by Iron man at a certain node will be represented by 
	 * the path cost to that node.
	 * 
	 * @param initialState
	 */
	public EndGame(int [] ironmanPos, int gridWidth, int gridHeight,
			int [] warriorsPos, int [] stonesPos, int [] thanosPos) {
		
		super(createInitialState(ironmanPos, warriorsPos.length / 2));
		String [] operators = new String []{
				"up", "down", "left", "right", 
				"kill", "snap", "collect"};
		this.setOperators(operators);
		
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.warriorsPos = warriorsPos;
		this.stonesPos = stonesPos;
		this.thanosPos = thanosPos;
		this.numOfWorriors = warriorsPos.length / 2;
		this.areStonesCollected = false;
		this.visitedStates = new HashMap<ArrayList<Integer>, Integer>();
		expandedNodes = new ArrayList<Node>();
	}
	
	private static Node createInitialState(int [] ironmanPos, int numOfWorriors) {
		ArrayList<Integer> state; Node parentNode; String operator;
		int depth; int pathCost;

		state = new ArrayList<Integer>();
		state.add(ironmanPos[0]); state.add(ironmanPos[1]);
		for(int i = 1; i <= 6; i++) {
			state.add(0);
		}
		for(int i = 1; i <= numOfWorriors; i++) {
			state.add(0);
		}
		parentNode = null;
		operator = null;
		depth = 1;
		pathCost = 0;
		
		return new Node(state, parentNode, operator, depth, pathCost);		
	}
	
	@Override
	public boolean goalTest(Node node) {
		if( node.getPathCost() < 100 &&
			node.getOperator() == "snap") {
			return true;
		}
		
		return false;
	}		

	@SuppressWarnings("unchecked")
	public Node applyOperator(Node parentNode, String operator) {
		nodeState = new ArrayList<Integer>();		
		oldState = parentNode.getState();
		
		// Initialize node state with the previous state values
		nodeState = (ArrayList<Integer>)oldState.clone();
		
//		for(int i = 0; i < nodeState.size(); i++) {
//			System.out.println(nodeState.get(i));
//		}
//		System.out.println(operator);
//		System.out.println(node.getPathCost());
//		System.out.println(node.getDepth());
//		System.out.println("--");


		cost = parentNode.getPathCost();
		depth = parentNode.getDepth() + 1;
		
		// TODO is this allowed?
		if(cost > 100) {
			return null;
		}

		switch(operator) {
		
		case "up":
			newValue = nodeState.get(0);
			newValue--;
			if ( newValue >= 0) {
				for(int i = 0; i < this.numOfWorriors; i++) {
					if(nodeState.get(i + 8) == 0) {
						if( (this.warriorsPos[i * 2] == newValue) &&
							(this.warriorsPos[(i * 2) + 1] == nodeState.get(1))) {
							return null;
						}
					}
				}
				nodeState.set(0, newValue);				
			} else {
				return null;
			}
			break;
			
		case "down":
			newValue = nodeState.get(0);
			newValue++;
			if ( newValue < gridHeight) {
				for(int i = 0; i < this.numOfWorriors; i++) {
					if(nodeState.get(i + 8) == 0) {
						if( (this.warriorsPos[i * 2] == newValue) &&
							(this.warriorsPos[(i * 2) + 1] == nodeState.get(1))) {
							return null;
						}
					}
				}
				nodeState.set(0, newValue);
			} else {
				return null;
			}
			break;
			
		case "left":
			newValue = (Integer) nodeState.get(1);
			newValue--;
			if ( newValue >= 0) {
				for(int i = 0; i < this.numOfWorriors; i++) {
					if(nodeState.get(i + 8) == 0) {
						if( (this.warriorsPos[i * 2] == nodeState.get(0)) &&
							(this.warriorsPos[(i * 2) + 1] == newValue)) {
							return null;
						}
					}
				}
				nodeState.set(1, newValue);
			} else {
				return null;
			}
			break;
			
		case "right": 
			newValue = (Integer) nodeState.get(1);
			newValue++;
			if ( newValue < gridWidth) {
				for(int i = 0; i < this.numOfWorriors; i++) {
					if(nodeState.get(i + 8) == 0) {
						if( (this.warriorsPos[i * 2] == nodeState.get(0)) &&
							(this.warriorsPos[(i * 2) + 1] == newValue)) {
							return null;
						}
					}
				}
				nodeState.set(1, newValue);
			} else {
				return null;
			}
			break;
			
		case "kill":
			// killing warrior decreases HP by 2
			isWorriorFound = false;
			
			for(int i = 0; i < this.numOfWorriors; i++) {
				if(nodeState.get(i + 8) == 0) {
					if( (this.warriorsPos[i * 2] - nodeState.get(0) == 1) ||
						(this.warriorsPos[i * 2] - nodeState.get(0) == -1)) {
						
						if(this.warriorsPos[(i * 2) + 1] == nodeState.get(1)) {
							isWorriorFound = true;
							cost += 2;
							nodeState.set(i + 8, 1);
						}
						
					} else if(this.warriorsPos[i * 2] == nodeState.get(0)) {
						
						if( (this.warriorsPos[(i * 2) + 1] - nodeState.get(1) == 1) || 
							(this.warriorsPos[(i * 2) + 1] - nodeState.get(1) == -1)) {
							
							isWorriorFound = true;
							cost += 2;
							nodeState.set(i + 8, 1);
							
						}
						
					}
				}
			}
			
			if(!isWorriorFound) {
				return null;
			}

			break;
			
		case "snap":
			if(	this.thanosPos[0] == nodeState.get(0) &&
			  	this.thanosPos[1] == nodeState.get(1)) {
				
				for(int i = 0; i < 6; i++) {
					if( nodeState.get(i + 2) == 0) {
						return null;
					}
				}
				
				return new Node(nodeState, parentNode, operator, depth, cost); 
			} else {
				return null;
			}
			
		case "collect": 
			// collecting stone decreases HP by 3
			for(int i = 0; i < 6; i++) {
				if( nodeState.get(i + 2) == 0 && 
					nodeState.get(0) == this.stonesPos[i * 2] &&
					nodeState.get(1) == this.stonesPos[(i * 2) + 1]) {
					cost += 3;
					nodeState.set(i + 2, 1);
					break;
				} else if(i == 5) {
					return null;
				}
			}
			
			break;

		}
		
		// warriors hits ironman by 1 hp each
		for(int i = 0; i < this.numOfWorriors; i++) {
			if(nodeState.get(i + 8) == 0) {
				if( (this.warriorsPos[i * 2] - nodeState.get(0) == 1) ||
					(this.warriorsPos[i * 2] - nodeState.get(0) == -1)) {
					
					if(this.warriorsPos[(i * 2) + 1] == nodeState.get(1)) {
						
						cost++;
					}
					
				} else if(this.warriorsPos[i * 2] == nodeState.get(0)) {
					
					if( (this.warriorsPos[(i * 2) + 1] - nodeState.get(1) == 1) || 
						(this.warriorsPos[(i * 2) + 1] - nodeState.get(1) == -1)) {
						
						cost++;	
					}	
				}
			}
		}
		
		// thanos hits ironman by 5 hp
		if( (this.thanosPos[0] - nodeState.get(0) == 1) || 
			(this.thanosPos[0] - nodeState.get(0) == -1)) {
			
			if(this.thanosPos[1] == nodeState.get(1)) {
				
				cost += 5;
				
			}
				
		} else if(this.thanosPos[0] == nodeState.get(0)) {
			
			if( (this.thanosPos[1] - nodeState.get(1) == 1) ||
				(this.thanosPos[1] - nodeState.get(1) == -1)) {
				
				cost += 5;
				
			}
			
		} else if(this.thanosPos[0] == nodeState.get(0) &&
				  this.thanosPos[1] == nodeState.get(1)) {
				
			cost += 5;
			
		}
		
//		System.out.println("Pass");

		return new Node(nodeState, parentNode, operator, depth, cost);
	}
	
	public ArrayList<Node> expandNode(Node node) {
		
		expandedNodes.clear();
		for(int i = 0; i < operators.length; i ++) {
			newNode = applyOperator(node, operators[i]);
			
			if(newNode != null) {
				areStonesCollected = false;
				for(int j = 0; j < 6; j++) {
					if( newNode.getState().get(j + 2) == 0) {
						break;
					} else if(j == 5) {
						this.areStonesCollected = true;
					}
				}
				if( (int) newNode.getState().get(0) == this.thanosPos[0] &&
						(int) newNode.getState().get(1) == this.thanosPos[1] &&
						!areStonesCollected) {
					continue;
				}
				if( (int) newNode.getState().get(0) == this.thanosPos[0] &&
						(int) newNode.getState().get(1) == this.thanosPos[1] &&
						areStonesCollected) {
					// Do not consider it as repeated state
					expandedNodes.add(newNode);
					continue;
				}
								
				bestPastSimilarNodeCost = this.visitedStates.get(newNode.getState());
				if(bestPastSimilarNodeCost != null) {
					if(newNode.getPathCost() - bestPastSimilarNodeCost > -1) {
//						System.out.println("Repeated State found.");
//						newNode.printDetails();
//						System.out.println("bestPastSimilarNodeCost = " + bestPastSimilarNodeCost);						
					}
					continue;

				}
				
				this.visitedStates.put(newNode.getState(), newNode.getPathCost());				
				expandedNodes.add(newNode);
			
			}
		}
		return expandedNodes;
	}

	@Override
	public void resetTree() {
		this.visitedStates.clear();
	}
	
	@Override
	public int heuristic1(Node n) {
		// Estimate through the number of uncollected stones times the cost of collection
		ArrayList<Integer> state = n.getState();
		int numOfUncollectedStones = 0;
		
		for(int i = 0; i < 6; i++) {
			if(state.get(i + 2) == 0) {
				numOfUncollectedStones++;
			}
		}
		
		return numOfUncollectedStones * 3;
	}
	
	@Override
	public int heuristic2(Node n) {
		// Minimize number of movement and killing and favor the collect action or being
		// in thanos' cell
		
		ArrayList<Integer> state = n.getState();
		int penalty = 0;
		
		if(	this.thanosPos[0] == state.get(0) &&
			this.thanosPos[1] == state.get(1)) {			
				return penalty;
		}
		
		if(n.getOperator() == "collect") {
			return penalty;
		}
		else if(n.getOperator() == "kill"){
			penalty = 5;
		}
		else {
			penalty = 10;
		}
		
		return penalty;
		
	}

}