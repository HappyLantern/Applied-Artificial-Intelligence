package model;

import java.util.Random;

import control.EstimatorInterface;

public class DummyLocalizer implements EstimatorInterface {

	private int rows, cols, head;
	private double[][] transitionMatrix;
	private double[][] observationMatrix;
	
	private double[][] fMatrix;
	int currentTrueState[] = { -1, -1, -10 };


	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;

	public DummyLocalizer(int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		transitionMatrix = new double[rows * cols * head][rows * cols * head];
		observationMatrix = new double[rows * cols + 1][rows * cols * head];
		fillTransitionMatrix();
		fillObservationVectors();
		for (int i = 0; i < rows * cols * head; i++) {
			for (int j = 0; j < rows * cols * head; j++) {
				System.out.print(transitionMatrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < rows * cols + 1; i++) {
			for (int j = 0; j < rows * cols * head; j++) {
				System.out.print(observationMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	private void fillObservationVectors() {
		int currentVector = 0;
		for (int rX = 0; rX < rows; rX++) {
			for (int rY = 0; rY < cols; rY++) {
				fillObservationVector(rX, rY, currentVector);
				currentVector++;
			}
		}
		fillObservationVector(-1, -1, currentVector);
	}

	private void fillObservationVector(int rX, int rY, int currentVector) {
		int index = 0;
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				for (int h = 0; h < head; h++) {
					observationMatrix[currentVector][index] = getOrXY(rX, rY, x, y, h);
					index++;
				}
			}
		}

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
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				for (int h = 0; h < head; h++) {
					// System.out.println("Row" + x + " " + y + " " + h);
					// This is each of the 255 rows for 8x8. Then fill probability for every column
					// in each row
					fillTransitionMatrixRow(x, y, h, matrixRow);
					matrixRow++;
				}
			}
		}

	}

	private void fillTransitionMatrixRow(int x, int y, int h, int matrixRow) {
		int matrixCol = 0;
		for (int nX = 0; nX < rows; nX++) {
			for (int nY = 0; nY < cols; nY++) {
				for (int nH = 0; nH < head; nH++) {
					// System.out.println("Col" + nX + " " + nY + " " + nH);
					double prob = getTProb(x, y, h, nX, nY, nH);
					// System.out.println(prob);
					transitionMatrix[matrixRow][matrixCol] = prob;
					matrixCol++;
				}
			}
		}
	}

	private boolean onMap(int x, int y) {
		System.out.println("MAP: " + x + " " + y);
		return (x >= 0 && x < rows) && (y >= 0 && y < cols);
	}

	private static final int[][] DIRECTIONS = { 
			{ -1, 0 }, // West
			{ 0, 1 }, // North
			{ 1, 0 }, // East
			{ 0, -1 } };// South

	// Calculate probability based on rules
	// P(nH == h | not encountering a wall) = 0.7
	// P(nH != h | not encountering a wall) = 0.3
	// P(nH == h | encountering a wall) = 0.0
	// P(nH != h | encountering a wall) = 1.0
	// Get transition probability for robot
	// Transform (x, y, h) & (nX, nY, nH) to coordinates in transitionMatrix
	public double getTProb(int x, int y, int h, int nX, int nY, int nH) {

		int availableDirections = 4;

		for (int i = 0; i < DIRECTIONS.length; i++)
			if (!onMap(x + DIRECTIONS[i][0], y + DIRECTIONS[i][1]))
				--availableDirections;

		if (x + DIRECTIONS[nH][0] == nX && y + DIRECTIONS[nH][1] == nY) {
			if (h == nH)
				return 0.7;
			else if (!onMap(x + DIRECTIONS[h][0], y + DIRECTIONS[h][1]))
				return 1.0 / availableDirections;
			else
				return 0.3 / (availableDirections - 1);
		}

		return 0.0;
	}

	private static final int[][] POSITIONS_UNO = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 },
			{ 1, 1 }, { 1, -1 } };

	private static final int[][] POSITIONS_DOS = { { -2, 0 }, { -2, -1 }, { -2, -2 }, { 1, -2 }, { 0, -2 }, { 1, 2 },
			{ 2, -2 }, { 2, -1 }, { 2, 0 }, { 2, 1 }, { 2, 2 }, { -1, -2 }, { 0, 2 }, { -1, 2 }, { -2, 2 }, { -2, 1 } };

	// Get sensor reading probability
	// r = bl�
	// xy = truePos
	public double getOrXY(int rX, int rY, int x, int y, int h) {
		int availableOuterDos = 16;
		int availableOuterUno = 8;
		if (rX == -1 && rY == -1)
			rX = rY = -5;

		for (int i = 0; i < POSITIONS_UNO.length; i++)
			if (!onMap(x + POSITIONS_UNO[i][0], y + POSITIONS_UNO[i][1]))
				--availableOuterUno;

		for (int i = 0; i < POSITIONS_DOS.length; i++)
			if (!onMap(x + POSITIONS_DOS[i][0], y + POSITIONS_DOS[i][1]))
				--availableOuterDos;

		// System.out.println("UNO: " + availableOuterUno);
		// System.out.println("DOS;: " + availableOuterDos);

		if (truePosition(rX, rY, x, y))
			return 0.1;
		if (oneFromPosition(rX, rY, x, y))
			return 0.05;
		if (twoFromPosition(rX, rY, x, y))
			return 0.025;
		System.out.println(rX + " " + rY);
		if (!onMap(rX, rY))
			return 1.0 - 0.1 - availableOuterUno * 0.05 - availableOuterDos * 0.025;
		return 0.0;
	}

	private boolean twoFromPosition(int rX, int rY, int x, int y) {
		return rX == x && rY == y + 2 || rX == x && rY == y - 2 || rX == x + 2 && rY == y || rX == x - 2 && rY == y
				|| rX == x + 1 && rY == y + 2 || rX == x + 1 && rY == y - 2 || rX == x + 2 && rY == y + 2
				|| rX == x + 2 && rY == y - 2 || rX == x + 2 && rY == y - 1 || rX == x + 2 && rY == y + 1
				|| rX == x - 1 && rY == y + 2 || rX == x - 1 && rY == y - 2 || rX == x - 2 && rY == y + 1
				|| rX == x - 2 && rY == y - 1 || rX == x - 2 && rY == y + 2 || rX == x - 2 && rY == y - 2;
	}

	private double[][] multiplyMatrix(double[][] m1, double[][] m2) {
		int m1ColLength = m1[0].length;
		int m2RowLength = m2.length;
		if (m1ColLength != m2RowLength)
			return null;

		int m3RowLength = m1.length;
		int m3ColLength = m2[0].length;

		double[][] m3 = new double[m3RowLength][m3ColLength];
		for (int i = 0; i < m3RowLength; i++) { // rows from m1
			for (int j = 0; j < m3ColLength; j++) { // columns from m2
				for (int k = 0; k < m1ColLength; k++) { // columns from m1
					m3[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return m3;
	}

	private double[][] transposeMatrix(double[][] m) {
		int rowLength = m.length;
		int colLength = m[0].length;

		double[][] t = new double[colLength][rowLength];
		for (int i = 0; i < rowLength; i++)
			for (int j = 0; j < colLength; j++)
				t[j][i] = m[i][j];

		return t;
	}

	private boolean oneFromPosition(int rX, int rY, int x, int y) {
		return rX == x && rY == y - 1 || rX == x && rY == y + 1 || rX == x + 1 && rY == y || rX == x - 1 && rY == y
				|| rX == x + 1 && rY == y + 1 || rX == x - 1 && rY == y - 1 || rX == x + 1 && rY == y - 1
				|| rX == x - 1 && rY == y + 1;
	}

	private boolean truePosition(int rX, int rY, int x, int y) {
		return rX == x && rY == y;
	}

	// Return current TRUE position of the robot
	public int[] getCurrentTrueState() {
		if (currentTrueState[2] != -10) {
			int[] temp = {currentTrueState[0], currentTrueState[1] ,currentTrueState[2]};
			return temp;
		}

		currentTrueState[0] = rows / 2;
		currentTrueState[1] = cols / 2;
		currentTrueState[2] = new Random().nextInt(4);
		int[] temp = {currentTrueState[0], currentTrueState[1] ,currentTrueState[2]};
		return temp;
	}

	// return the position where the sensor thinks that the robot is
	public int[] getCurrentReading() {
		int[] sensorLocation = getCurrentTrueState();
		int zero = 100;
		int firstLayer = 50;
		int secondLayer = 25;
		int[] nothing = { -1, -1 };
		int[] randomSensorDirection = { 0, 0 };
		int random = new Random().nextInt(1000);

		int availableFirst = 8; // Assume
		int availableSecond = 16; // Assume

		
		System.out.println("Random " + random);
		System.out.println("Zero " + zero);
		System.out.println("FirstLayer " +  (zero + firstLayer * availableFirst));
		System.out.println("SecondLayer " +( zero + firstLayer * availableFirst + secondLayer * availableSecond));

		

		if (random < 100) {
			System.out.println("True loc: " + sensorLocation);
			return sensorLocation;
			
		} else if (zero < random && random < zero + firstLayer * availableFirst) {
			System.out.println("One from location");
			int r = new Random().nextInt(POSITIONS_UNO.length);
			System.out.println("DIR " + POSITIONS_UNO[r][0] + " " + POSITIONS_UNO[r][1]);
			System.out.println("POS " +sensorLocation[0] + " " + sensorLocation[1]);

			if (onMap(sensorLocation[0] + POSITIONS_UNO[r][0], sensorLocation[1] + POSITIONS_UNO[r][1]))
				randomSensorDirection = POSITIONS_UNO[r];
			else {
				System.out.println("Not on map");
				return nothing;
				
			}
			
		} else if ((zero + firstLayer * availableFirst) < random && random < zero + firstLayer * availableFirst + secondLayer * availableSecond) {
			// FIX THIS SAME AS THE ONE ABOVE ish
			System.out.println("Two from location");
			int r = new Random().nextInt(POSITIONS_DOS.length);
			if (onMap(sensorLocation[0] + POSITIONS_DOS[r][0], sensorLocation[1] + POSITIONS_DOS[r][1]))
				randomSensorDirection = POSITIONS_DOS[r];
			else {
				System.out.println("Not on map");
				return nothing;
			}
		} else {
			System.out.println("Nothing");
			return nothing;
		}

		sensorLocation[0] = sensorLocation[0] + randomSensorDirection[0];
		sensorLocation[1] = sensorLocation[1] + randomSensorDirection[1];
		System.out.println("Something: " + sensorLocation[0] + " " + sensorLocation[1]);

		return sensorLocation;
	}

	//  f = a OTtf
	public double getCurrentProb(int x, int y) {
		  double prob = 0.0; 

		  
		  return prob; 
	}
	


	public void update() {
		int[] trueState = getCurrentTrueState();
		System.out.println(trueState[0] + " " + trueState[1] + " " + trueState[2]);
		int tRow = trueState[0];
		int tCol = trueState[1];
		int tHead = trueState[2];

		boolean crashingIntoWall = !onMap(tRow + DIRECTIONS[tHead][0], tCol + DIRECTIONS[tHead][1]);

		int newHead = new Random().nextInt(4);
		if (crashingIntoWall || new Random().nextInt(10) >= 7)
			while (!onMap(tRow + DIRECTIONS[newHead][0], tCol + DIRECTIONS[newHead][1]) || newHead == tHead)
				newHead = new Random().nextInt(4);
		else
			newHead = tHead;

		System.out.println("Y2: " + (tCol + DIRECTIONS[newHead][1]));
		System.out.println("HEAD: " + newHead);
		System.out.println("ROW: " + trueState[0] + " = " + tRow + " + " + DIRECTIONS[newHead][0]);
		System.out.println("COL: " + trueState[1] + " = " + tCol + " + " + DIRECTIONS[newHead][1]);

		currentTrueState[0] = tRow + DIRECTIONS[newHead][0];
		currentTrueState[1] = tCol + DIRECTIONS[newHead][1];
		currentTrueState[2] = newHead;
		System.out.println("ROBOT: " + currentTrueState[0] + " " + currentTrueState[1]);

		// System.out.println("Nothing is happening, no model to go for...");
	}

}