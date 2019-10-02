
public class Main {

	/**
	 * TODO 1 uses search to try to formulate a winning plan:
	 * 
	 * @param grid, a string representing the grid to perform the search on. This string
		should be in the following format:
			m,n;ix,iy;tx,ty;
			s1,x,s1y,s2x,s2y,s3x,s3y,s4x,s4y,s5x,s5y,s6x,s6y;
			w1x,w1y,w2x,w2y,w3x,w3y,w4x,w4y,w5x,w5y
		where
			 m and n represent the width and height of the grid respectively.
			 ix and iy represent the x and y starting positions of Iron Man.
			 tx and ty represent the x and y positions of Thanos;
			 six,siy represent the x and y position of stone si.
			 wix,wiy represent the x and y position of warrior wi.
		Note that the string representing the grid does not contain any spaces or new
		lines. It is just formatted this way above to make it more readable. All x and y
		positions are assuming 0-indexing. The minimum dimensions of the input grid
		must be at least 5  5 with at least 5 warriors.
		
	 * @param strategy, a string indicating the search strategy to be applied:
	  		 BF for breadth-first search,
			 DF for depth-first search,
			 ID for iterative deepening search,
			 UC for uniform cost search,
			 GRi for greedy search, with i 2 f1; 2g distinguishing the two heuristics, and	
			 ASi for A search, with i 2 f1; 2g distinguishing the two heuristics.
			
	 * @param visualize, a boolean parameter which, when set to true, results in your program’s
		side-effecting a visual presentation of the grid as it undergoes the different
		steps of the discovered solution (if one was discovered).
		
	 * @return a String of the following format: plan;cost;nodes if a solution
		was found where
		– plan is a string representing the operators Iron Man needs to follow separated
		by commas. The possible operator names are: up, down, left, right,
		collect, kill, and snap.
		– cost is a number representing the cost of the found plan.
		– nodes is the number of nodes chosen for expansion during the search.
		If the problem has no solution, the string There is no solution. should be returned.
	 */
	public static String solve(String grid, String strategy, boolean visualize) {
		int gridWidth, gridHeight; int iX, iY; int tX, tY;
		int [] sI = new int[12];
		int [] wI = new int[10];
		
		String [] lines = grid.split(";");
		String [] line;
		line = lines[0].split(",");
		gridWidth = Integer.parseInt(line[0]);
		gridHeight = Integer.parseInt(line[1]);
		line = lines[1].split(",");
		iX = Integer.parseInt(line[0]);
		iY = Integer.parseInt(line[1]);
		tX = Integer.parseInt(line[0]);
		tY = Integer.parseInt(line[1]);
		
		line = lines[2].split(",");
		for(int i = 0; i < line.length; i ++) {
			sI[i] = Integer.parseInt(line[i]);
		}
		
		line = lines[3].split(",");
		for(int i = 0; i < line.length; i ++) {
			wI[i] = Integer.parseInt(line[i]);
		}
			
		switch(strategy) {
		case "BF":
		case "DF":
		case "ID":
		case "UC":
		case "GR":
		case "AS":
		default: break;
		}
		
		if(visualize) {
			// TODO
			return "Show some effects";
		}
		
		return "plan;cost;node";
	}
}
