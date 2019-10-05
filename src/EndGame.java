public class EndGame extends GeneralSearchProblem {

//	HashMap<String, Boolean> worriorsKilled;
//	HashMap<String, Boolean> stonesCollected;
	
	String [] worriorsPos;
	String [] stonesPos;
	String [] thanosPos;
	int gridWidth;
	int gridHeight;
	
	/**
	 * EndGame search tree consists of Nodes, just like any search tree;
	 * the only difference, that make these Node belong to the EndGame
	 * problem specifically is the format of the state of its node.
	 * 
	 * this class for example relays on the Node class, which is basically
	 * a generic search tree node, however, as the state of the node is simply
	 * a String, hence, we can have our own format to this state String,
	 * to afford our problem state;
	 * 
	 * therefore, our EndGame format is:
	 * 
	 * currentXPos, currentYPos, HP,
	 * s1Collected, s2C., s3C., s4C., s5C., s6C.,
	 * w1Killed, w2K, w3k, w4k, w5k	
	 * 
	 * @param initialState
	 */
	public EndGame(Node initialState, int gridWidth, int gridHeight,
			String [] worriorsPos, String [] stonesPos, String [] thanosPos) {
		super(initialState);
		String [] operators = new String []{
				"up", "down", "left", "right", 
				"collect", "kill", "snap"};
		this.setOperators(operators);
	}
	
	@Override
	public boolean goalTest(Node node) {
		
		return false;
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void pathCost() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public Node applyOperator(String Operator) {
		
		// TODO Auto-generated method stub
		// collecting stone decreases HP by 3
		// killing worrior increases HP by 2
		// thanos hits ironman by 5 hp
		return null;
	}

}