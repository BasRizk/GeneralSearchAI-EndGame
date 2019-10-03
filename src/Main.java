public class Main {

	/**
	 * TODO 1 uses search to try to formulate a winning plan:
	 * 
	 * @param grid, a string representing the grid to perform the search on:
	 *
	 *		m,n;ix,iy;tx,ty;
	 *		s1,x,s1y,s2x,s2y,s3x,s3y,s4x,s4y,s5x,s5y,s6x,s6y;
	 *		w1x,w1y,w2x,w2y,w3x,w3y,w4x,w4y,w5x,w5y
	 *		
	 *	where
	 *		- m and n represent the width and height of the grid respectively.
	 *		- ix and iy represent the x and y starting positions of Iron Man.
	 *		- tx and ty represent the x and y positions of Thanos;
	 *		- six,siy represent the x and y position of stone si.
	 *		- wix,wiy represent the x and y position of warrior wi.
	 *
	 *	All x and y positions are assuming 0-indexing.
	 *	The minimum dimensions of the input grid must be at least 5  5 with at least 5 warriors.
	 *
	 *
	 * @param strategy, a string indicating the search strategy to be applied:
	 * 		BF, DF, ID, UC,
	 * 		- GRi for greedy search, with i 2 f1; 2g distinguishing the two heuristics, and	
	 * 		- ASi for A search, with i 2 f1; 2g distinguishing the two heuristics.
	 * 	
	 * @param visualize, if true, results in your program’s visual presentation of the grid
	 * 					as it undergoes the different steps of the discovered solution
	 * 					 (if one was discovered).
	 * 
	 * @return a String: plan;cost;nodes if a solution was found where
	 * 			– plan is a string representing the operators Iron Man needs to follow separated
	 * 			by commas. The possible operator names are: up, down, left, right,
	 * 			collect, kill, and snap.
	 * 			– cost is a number representing the cost of the found plan.
	 * 			– nodes is the number of nodes chosen for expansion during the search.
	 * 			If the problem has no solution, the string There is no solution. should be returned.
	 */
	
	public static String solve(String grid, String strategy, boolean visualize) {
		
		// Parsing Grid Input
		int gridWidth, gridHeight; int iX, iY; int tX, tY;
		int [] sI = new int[12];
		int [] wI = new int[10];
		
		String [] lines = grid.split(";");
		String [] line;
		line = lines[0].split(",");
		gridWidth = Integer.parseInt(line[0]);
		gridHeight = Integer.parseInt(line[1]);
		line = lines[1].split(",");
		String dimens = lines[1];
		iX = Integer.parseInt(line[0]);
		iY = Integer.parseInt(line[1]);
		line = lines[2].split(",");
		tX = Integer.parseInt(line[0]);
		tY = Integer.parseInt(line[1]);
		
		line = lines[3].split(",");
		for(int i = 0; i < line.length; i ++) {
			sI[i] = Integer.parseInt(line[i]);
		}
		
		line = lines[4].split(",");
		for(int i = 0; i < line.length; i ++) {
			wI[i] = Integer.parseInt(line[i]);
		}
		
		
		// Initializations
		// TODO, VERY TRIVIAL NOW, JUST TAKING INIT POS OF IRON MAN,
		// HOWEVER, ACCORDING TO THE PROBLEM, STATE IS DEFINED
		EndGame endGameProblem = new EndGame();

		String state; Node parentNode;
		int depth; int pathCost;

		state = dimens;
		parentNode = null;
		depth = 1;
		pathCost = 0;

		
		Node initState = new Node(state, parentNode, null, depth, pathCost);		
		endGameProblem.initialState = initState;
		
		return GeneralSearchProblem.search(endGameProblem, strategy, visualize);
	}
	
}
