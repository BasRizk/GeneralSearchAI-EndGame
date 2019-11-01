import java.util.ArrayList;

public class Main {

	/**
	 * Search to try to formulate a winning plan:
	 * 
	 * @param grid,      a string representing the grid to perform the search on:
	 *
	 *                   m,n;ix,iy;tx,ty;
	 *                   s1,x,s1y,s2x,s2y,s3x,s3y,s4x,s4y,s5x,s5y,s6x,s6y;
	 *                   w1x,w1y,w2x,w2y,w3x,w3y,w4x,w4y,w5x,w5y
	 * 
	 *                   where - m and n represent the width and height of the grid
	 *                   respectively. - ix and iy represent the x and y starting
	 *                   positions of Iron Man. - tx and ty represent the x and y
	 *                   positions of Thanos; - six,siy represent the x and y
	 *                   position of stone si. - wix,wiy represent the x and y
	 *                   position of warrior wi.
	 *
	 *                   All x and y positions are assuming 0-indexing. The minimum
	 *                   dimensions of the input grid must be at least 5  5 with at
	 *                   least 5 warriors.
	 *
	 *
	 * @param strategy,  a string indicating the search strategy to be applied: BF,
	 *                   DF, ID, UC, - GRi for greedy search, with i 2 f1; 2g
	 *                   distinguishing the two heuristics, and - ASi for A search,
	 *                   with i 2 f1; 2g distinguishing the two heuristics.
	 * 
	 * @param visualize, if true, results in your program’s visual presentation of
	 *                   the grid as it undergoes the different steps of the
	 *                   discovered solution (if one was discovered).
	 * 
	 * @return a String: plan;cost;nodes if a solution was found where – plan is a
	 *         string representing the operators Iron Man needs to follow separated
	 *         by commas. The possible operator names are: up, down, left, right,
	 *         collect, kill, and snap. – cost is a number representing the cost of
	 *         the found plan. – nodes is the number of nodes chosen for expansion
	 *         during the search. If the problem has no solution, the string There
	 *         is no solution. should be returned.
	 */

	public static String solve(String grid, String strategy, boolean visualize) {

		// Parsing Grid Input
		int gridWidth, gridHeight;
		int[] ironmanPos;
		int[] thanosPos;
		int[] stonesPos = new int[12];
		int[] warriorsPos;

		ArrayList<Integer> warriorsPosDyn = new ArrayList<Integer>();
		String[] lines = grid.split(";");
		String[] line;

		line = lines[0].split(",");
		gridWidth = Integer.parseInt(line[0]);
		gridHeight = Integer.parseInt(line[1]);
		line = lines[1].split(",");
		ironmanPos = new int[2];
		ironmanPos[0] = Integer.parseInt(line[0]);
		ironmanPos[1] = Integer.parseInt(line[1]);
		line = lines[2].split(",");
		thanosPos = new int[2];
		thanosPos[0] = Integer.parseInt(line[0]);
		thanosPos[1] = Integer.parseInt(line[1]);

		line = lines[3].split(",");
		for (int i = 0; i < line.length; i++) {
			stonesPos[i] = Integer.parseInt(line[i]);
		}

		line = lines[4].split(",");

		for (int i = 0; i < line.length; i++) {
			warriorsPosDyn.add(Integer.parseInt(line[i]));
		}

		warriorsPos = new int[warriorsPosDyn.size()];
		for (int i = 0; i < line.length; i++) {
			warriorsPos[i] = warriorsPosDyn.get(i);
		}

		// Initializations
		EndGame endGameProblem = new EndGame(ironmanPos, gridWidth, gridHeight, warriorsPos, stonesPos, thanosPos);

		return GeneralSearchProblem.search(endGameProblem, strategy, visualize);
	}

	public static void main(String[] args) {
		long startTime, endTime, totalTime;
		String problemDescription, gridSize;

//		problemDescription = "15,15;12,13;5,7;7,0,9,14,14,8,5,8,8,9,8,4;6,6,4,3,10,2,7,4,3,11,10,0";		
		gridSize = "15";
		problemDescription = gridSize + "," + gridSize + 
				";1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3";
		String[] toRunStrategies = new String[]
				{ "DF", "BF", "ID", "UC", "GR1", "GR2", "AS1", "AS2" };
		boolean visualize = false;

		for (String strategy : toRunStrategies) {

			System.out.println("Running " + strategy);
			startTime = System.nanoTime();
			solve(problemDescription, strategy, visualize);
			endTime = System.nanoTime();
			totalTime = endTime - startTime;
			System.out.println((double) totalTime / 1000000000f + "\n");
		}

	}

}
