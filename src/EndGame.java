import java.util.ArrayList;
import java.util.HashMap;

public class EndGame extends GeneralSearchProblem {
	
	int [] worriorsPos;
	int [] stonesPos;
	int [] thanosPos;
	int [] ironmanPos;
	
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
				"kill", "snap", "collect"};
		this.setOperators(operators);
		
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.worriorsPos = worriorsPos;
		this.stonesPos = stonesPos;
		this.thanosPos = thanosPos;
		this.ironmanPos = ironmanPos;
	
		this.numOfWorriors = worriorsPos.length / 2;
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
						if( (this.worriorsPos[i * 2] == newValue) &&
							(this.worriorsPos[(i * 2) + 1] == nodeState.get(1))) {
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
						if( (this.worriorsPos[i * 2] == newValue) &&
							(this.worriorsPos[(i * 2) + 1] == nodeState.get(1))) {
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
						if( (this.worriorsPos[i * 2] == nodeState.get(0)) &&
							(this.worriorsPos[(i * 2) + 1] == newValue)) {
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
						if( (this.worriorsPos[i * 2] == nodeState.get(0)) &&
							(this.worriorsPos[(i * 2) + 1] == newValue)) {
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
					if( (this.worriorsPos[i * 2] - nodeState.get(0) == 1) ||
						(this.worriorsPos[i * 2] - nodeState.get(0) == -1)) {
						
						if(this.worriorsPos[(i * 2) + 1] == nodeState.get(1)) {
							isWorriorFound = true;
							cost += 2;
							nodeState.set(i + 8, 1);
						}
						
					} else if(this.worriorsPos[i * 2] == nodeState.get(0)) {
						
						if( (this.worriorsPos[(i * 2) + 1] - nodeState.get(1) == 1) || 
							(this.worriorsPos[(i * 2) + 1] - nodeState.get(1) == -1)) {
							
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
				if( (this.worriorsPos[i * 2] - nodeState.get(0) == 1) ||
					(this.worriorsPos[i * 2] - nodeState.get(0) == -1)) {
					
					if(this.worriorsPos[(i * 2) + 1] == nodeState.get(1)) {
						
						cost++;
					}
					
				} else if(this.worriorsPos[i * 2] == nodeState.get(0)) {
					
					if( (this.worriorsPos[(i * 2) + 1] - nodeState.get(1) == 1) || 
						(this.worriorsPos[(i * 2) + 1] - nodeState.get(1) == -1)) {
						
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
	public void visualise(String plan) {
		
		
		String [] line = plan.split(",");
		ArrayList<Integer> thanos = new ArrayList<Integer>();
		for(int g = 0; g<thanosPos.length; g++ ) {
			thanos.add(thanosPos[g]);
		}
		ArrayList<Integer> stones = new ArrayList<Integer>();
		for(int g = 0; g< stonesPos.length; g++) {
			stones.add(stonesPos[g]);
		}
		ArrayList<Integer> warriors = new ArrayList<Integer>();
		for(int g = 0; g< worriorsPos.length; g++) {
			warriors.add(worriorsPos[g]);
		}
		for(int i = 0; i< line.length; i++) {
		
			switch(line[i]) {
				case "left":	
					ironmanPos[1] -=1;	
					break;
				case "right":
					ironmanPos[1] +=1;
					break;
				case "up":
					ironmanPos[0] -=1;
					break;
				case "down":
					ironmanPos[0] +=1;
					break;
				case "collect":
						
					for(int j = 0; j < stones.size(); j+=2) {
				    	if(ironmanPos[1] ==stones.get(j+1) && ironmanPos[0] == stones.get(j)) {
				    		stones.remove(j);
				    		stones.remove(j);
						    break;
				    	}
					}
					break;
				case "kill":
					
					for(int j = 0; j < warriors.size(); j+=2) 
					{
						boolean up = ((ironmanPos[1]-1) ==warriors.get(j+1) && ironmanPos[0] == warriors.get(j));
						boolean down = ((ironmanPos[1]+1) ==warriors.get(j+1) && ironmanPos[0] == warriors.get(j));
						boolean left = (ironmanPos[1] ==warriors.get(j+1) && (ironmanPos[0]-1) == warriors.get(j));
						boolean right = (ironmanPos[1] ==warriors.get(j+1) && (ironmanPos[0]+1) == warriors.get(j));
				    	if(up){
				    		warriors.remove(j);
				    		warriors.remove(j);
				    	}
				    	if(down) {
				    		warriors.remove(j);
				    		warriors.remove(j);
				    	}
				    	if(left) {
				    		warriors.remove(j);
				    		warriors.remove(j);
				    	}
				    	if(right) {
				    		warriors.remove(j);
				    		warriors.remove(j);
				    	}
					}
					break;
				case "snap":
					thanos.remove(0);
					thanos.remove(0);
					break;
			}
		System.out.println();
		System.out.println(line[i]);
		System.out.println();
		drawGraph(thanos, warriors, stones);
		}
				
	}
	public void drawGraph(ArrayList<Integer> thanos,ArrayList<Integer> warriors, ArrayList<Integer> stones) {
		int countx = 0;
		int county = 0;
		boolean flag = true;
		boolean collected = true;
		String[] easyGrid =  new String[gridHeight*gridWidth];
		
		  for (int x = 0; x < gridHeight*gridWidth; x++) {
			  	flag = true;
			  	collected = true;
			    if(x % gridWidth== 0 && x != 0) {
			    	countx = 0;
			    	System.out.println();
			    	county++;
			    	
			    }
			    if(!thanos.isEmpty()) {
				    if(countx == thanos.get(1) && county == thanos.get(0) && countx == ironmanPos[1] && county == ironmanPos[0]) {
				    	easyGrid[x] = "T/I";
					    System.out.print(easyGrid[x]+" | ");
					    collected = false;
				    }
			    }
			    if(!thanos.isEmpty()) {
			    	if(countx == thanos.get(1) && county == thanos.get(0)&& collected) { 
			    			easyGrid[x] = " T ";
			    			System.out.print(easyGrid[x]+" | ");
			    			flag = false;}
			    }
			    if(countx == ironmanPos[1] && county == ironmanPos[0])
			    {
			    	for(int i = 0; i< stones.size(); i+=2) {	
				    	if(countx ==stones.get(i+1) && county == stones.get(i)) {
				    		
				    		easyGrid[x] = "S/I";
						    System.out.print(easyGrid[x]+" | ");
						    collected = false;
						    break;
				    	}
			    	}
				if(collected) {
			    easyGrid[x] = " I ";
			    System.out.print(easyGrid[x]+" | ");
			    flag = false;
					}
			    }
			    if(true) {
			    for(int i = 0; i< stones.size(); i+=2) {	
			    	if(countx ==stones.get(i+1) && county == stones.get(i) && collected) {
			    		easyGrid[x] = " S ";
					    System.out.print(easyGrid[x]+" | ");
					    flag = false;
					    break;
			    	}
			    
			    }
			    for(int i = 0; i<warriors.size(); i+=2) {
			    	if(countx ==warriors.get(i+1) && county == warriors.get(i)) {
			    		easyGrid[x] = " W ";
					    System.out.print(easyGrid[x]+" | ");
					    flag = false;
					    break;
			    	}
			    }
			    }
			    
			    if(flag && collected)
			    {
			    easyGrid[x] = "   ";
			    System.out.print(easyGrid[x]+" | ");
			    }
			    countx++;
			}
		  System.out.println();
	}
	
	
}