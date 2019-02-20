package model;

import control.EstimatorInterface;

public class DummyLocalizer implements EstimatorInterface {
		
	private int rows, cols, head;
	private double[][] transitionMatrix;
 
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	
	public DummyLocalizer( int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		transitionMatrix = new double[rows*cols*head][rows*cols*head];
		fillTransitionMatrix();
	}	
	
	public int getNumRows() {
		return rows;
	}
	
	public int getNumCols() {
		return cols;
	}
	
	public int getNumHead() {
		return head;
	}
	
	private void fillTransitionMatrix() {
		int matrixRow = 0;
		for (int x = 0; x < rows - 1; x++) {
			for (int y = 0; y < cols - 1; y++) {
				for (int h = 0; h < head - 1; h++) {
				// This is each of the 255 rows for 8x8. Then fill probability for every column in each row
				fillTransitionMatrixRow(x, y, h, matrixRow);
				}
			}
		}
		
	}
	
	private void fillTransitionMatrixRow(int x, int y, int h, int matrixRow) {
		int matrixCol = 0;
		for (int nX = 0; nX < rows - 1; nX++) {
			for (int nY = 0; nY < cols - 1; nY++) {
				for (int nH = 0; nH < head - 1; nH++) {
					transitionMatrix[matrixRow][matrixCol] = calcTProb(x, y, h, nX, nY, nH);
					matrixCol++;
				}
			}
		}	
	}

	// Calculate probability based on rules
	// P(nH == h | not encountering a wall) = 0.7
	// P(nH != h | not encountering a wall) = 0.3
	// P(nH == h | encountering a wall) = 0.0
	// P(nH != h | encountering a wall) = 1.0
	private double calcTProb(int x, int y, int h, int nX, int nY, int nH) {
//		boolean isAtCorner = isAtCorner(x, y, h);
//		boolean isAtWall = isAtWall(x, y, h);
		boolean isInFrontOf = isInFrontOf(x, y, h, nX, nY, nH);
		boolean isRightOf = isRightOf(x, y, h, nX, nY, nH);
		boolean isLeftOf = isLeftOf(x, y, h, nX, nY, nH);
		boolean isBehindOf = isBehindOf(x, y, h, nX, nY, nH);		
		boolean encounteringWall = encounteringWall(x, y, h, nX, nY, nH);

		if (!encounteringWall && isInFrontOf)
			return 0.7;
		if (!encounteringWall && isLeftOf)
			return 0.3;
		if (!encounteringWall && isRightOf)
			return 0.3;
		if (!encounteringWall && isBehindOf)
			return 0.3;	
		return 0.0;
		}

	// Get transition probability for robot
	public double getTProb( int x, int y, int h, int nX, int nY, int nH) {
		 // Transform (x, y, h) & (nX, nY, nH) to coordinates in transitionMatrix
		return transitionMatrix[0][0];
	}

	private boolean isLeftOf(int x, int y, int h, int nX, int nY, int nH) {
		if (h == NORTH && nH == WEST && x == nX && y - 1 == nY)
			return true;
		if (h == EAST && nH == NORTH && x - 1 == nX && y == nY)
			return true;
		if (h == WEST && nH == SOUTH && x + 1 == nX && y == nY)
			return true;
		if (h == SOUTH && nH == EAST && x == nX && y + 1 == nY)
			return true;
		return false;
	}
	private boolean isRightOf(int x, int y, int h, int nX, int nY, int nH) {
		if (h == NORTH && nH == EAST && x == nX && y + 1 == nY)
			return true;
		if (h == EAST && nH == SOUTH && x + 1 == nX && y == nY)
			return true;
		if (h == WEST && nH == NORTH && x - 1 == nX && y == nY)
			return true;
		if (h == SOUTH && nH == WEST && x == nX && y - 1 == nY)
			return true;
		return false;
	}
	private boolean isBehindOf(int x, int y, int h, int nX, int nY, int nH) {
		if (h == NORTH && nH == SOUTH && x + 1 == nX && y == nY)
			return true;
		if (h == EAST && nH == WEST && x == nX && y - 1 == nY)
			return true;
		if (h == WEST && nH == EAST && x == nX && y + 1 == nY)
			return true;
		if (h == SOUTH && nH == NORTH && x - 1 == nX && y == nY)
			return true;
		return false;
	}
	private boolean isInFrontOf(int x, int y, int h, int nX, int nY, int nH) {
		if (h == NORTH && h == nH &&  x - 1 == nX && y == nY)
			return true;
		if (h == EAST && h == nH && x == nX && y + 1 == nY)
			return true;
		if (h == WEST && h == nH && x == nX && y - 1 == nY) 
			return true;
		if (h == SOUTH && h == nH &&  x + 1 == nX && y == nY)
			return true;
		return false;
	}

	private boolean encounteringWall(int x, int y, int h, int nX, int nY, int nH) {
		if (x + 1 == rows - 1 && x + 1 == nX)
			return true;
		if (x - 1 == 0 && x - 1 == nX)
			return true;
		if (y + 1 == cols - 1 && y + 1 == nY)
			return true;
		if (y - 1 == 0 && y - 1 == nY)
			return true;
		return false;
	}
	

	private boolean isNextToPosition(int x, int y, int h, int nX, int nY, int nH) {
		if (x + 1 == nX && y == nY || x - 1 == nX && y == nY || x == nX && y + 1 == nY || x == nX && y - 1 == nY) {
			return true;
		}
		return false;
	}

	private boolean isAtWall(int x, int y, int h) {
		if ((x == 0 || x == rows || y == 0 || y == cols) && !isAtCorner(x, y, h))
			return true;
		return false;
	}

	private boolean isAtCorner(int x, int y, int h) {
		if (x == 0 && y == 0 || x == rows - 1 && y == 0 || x == 0 && y == cols - 1 || x == rows && y == cols - 1)
			return true;
		return false;
	}

	// Get sensor reading probability
	public double getOrXY( int rX, int rY, int x, int y, int h) {
		return 0.1;
	}

   // Return current TRUE position of the robot
	public int[] getCurrentTrueState() {
		
		
		int[] ret = new int[3];
		ret[0] = rows/2;
		ret[1] = cols/2;
		ret[2] = head;
		return ret;
	}

	// return the position where the sensor thinks that the robot is
	public int[] getCurrentReading() {
		int[] ret = null;
		return ret;
	}

    // Summed probability to be at position x, y
	public double getCurrentProb( int x, int y) {
		double ret = 0.0;
		return ret;
	}
	
	public void update() {
		System.out.println("Nothing is happening, no model to go for...");
	}
	
	
}