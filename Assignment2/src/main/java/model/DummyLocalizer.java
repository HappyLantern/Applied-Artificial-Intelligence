package model;

import java.util.Random;

import control.EstimatorInterface;

public class DummyLocalizer implements EstimatorInterface {

	private int rows, cols, head;
	private double[][] transitionMatrix;
	private double[][] observationMatrix;
	private double[] fVector;
	private int[] sensorReading;
	private int currentTrueState[] = { -1, -1, -10 };


	private static final int[][] POSITIONS_UNO = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 },
			{ 1, 1 }, { 1, -1 } };

	private static final int[][] POSITIONS_DOS = { { -2, 0 }, { -2, -1 }, { -2, -2 }, { 1, -2 }, { 0, -2 }, { 1, 2 },
			{ 2, -2 }, { 2, -1 }, { 2, 0 }, { 2, 1 }, { 2, 2 }, { -1, -2 }, { 0, 2 }, { -1, 2 }, { -2, 2 }, { -2, 1 } };
	private static final int[][] DIRECTIONS = { { -1, 0 }, // West
			{ 0, 1 }, // North
			{ 1, 0 }, // East
			{ 0, -1 } };// South

	// Evaluating
	private int count = 0;
	private double distanceSum = 0;
	
	public DummyLocalizer(int rows, int cols, int head) {

		this.rows = rows;
		this.cols = cols;
		this.head = head;

		sensorReading = new int[2];

		transitionMatrix = new double[rows * cols * head][rows * cols * head];
		fillTransitionMatrix();

		observationMatrix = new double[rows * cols + 1][rows * cols * head];
		fillObservationVectors();

		fVector = new double[rows * cols * head];
		fillFMatrix();
	}

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

		if (truePosition(rX, rY, x, y))
			return 0.1;
		if (oneFromPosition(rX, rY, x, y))
			return 0.05;
		if (twoFromPosition(rX, rY, x, y))
			return 0.025;

		if (!onMap(rX, rY))
			return 1.0 - 0.1 - availableOuterUno * 0.05 - availableOuterDos * 0.025;
		return 0.0;
	}

	public int[] getCurrentTrueState() {
		if (currentTrueState[2] != -10) {
			int[] temp = { currentTrueState[0], currentTrueState[1], currentTrueState[2] };
			return temp;
		}

		currentTrueState[0] = rows / 2;
		currentTrueState[1] = cols / 2;
		currentTrueState[2] = new Random().nextInt(4);
		int[] temp = { currentTrueState[0], currentTrueState[1], currentTrueState[2] };
		return temp;
	}

	// return the position where the sensor thinks that the robot is
	public int[] getCurrentReading() {
		return sensorReading;
	}

	public double getCurrentProb(int x, int y) {
		double sum = 0;
		for (int i = 0; i < 4; i++)
			sum += fVector[(x * rows + y) * 4 + i];
		return sum;
	}

	public void update() {
		int[] trueState = getCurrentTrueState();
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

		currentTrueState[0] = tRow + DIRECTIONS[newHead][0];
		currentTrueState[1] = tCol + DIRECTIONS[newHead][1];
		currentTrueState[2] = newHead;

		int[] sensorState = { -1, -1 };
		int[] randomSensorDirection = { 0, 0 };

		int random = new Random().nextInt(1000);
		if (random < 100) {

			sensorState = getCurrentTrueState();

		} else if (100 < random && random < 100 + 50 * 8) {
			int r = new Random().nextInt(POSITIONS_UNO.length);
			if (onMap(getCurrentTrueState()[0] + POSITIONS_UNO[r][0], getCurrentTrueState()[1] + POSITIONS_UNO[r][1])) {
				sensorState = getCurrentTrueState();
				randomSensorDirection = POSITIONS_UNO[r];
			}

		} else if ((100 + 50 * 8) < random && random < 100 + 50 * 8 + 25 * 16) {

			int r = new Random().nextInt(POSITIONS_DOS.length);
			if (onMap(getCurrentTrueState()[0] + POSITIONS_DOS[r][0], getCurrentTrueState()[1] + POSITIONS_DOS[r][1])) {
				sensorState = getCurrentTrueState();
				randomSensorDirection = POSITIONS_DOS[r];
			}
		}
		sensorState[0] = sensorState[0] + randomSensorDirection[0];
		sensorState[1] = sensorState[1] + randomSensorDirection[1];

		sensorReading[0] = sensorState[0];
		sensorReading[1] = sensorState[1];

		updateFVector();
		count++;
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

	private void fillTransitionMatrix() {
		int matrixRow = 0;
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				for (int h = 0; h < head; h++) {
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

					double prob = getTProb(x, y, h, nX, nY, nH);

					transitionMatrix[matrixRow][matrixCol] = prob;
					matrixCol++;
				}
			}
		}
	}

	private boolean onMap(int x, int y) {
		return (x >= 0 && x < rows) && (y >= 0 && y < cols);
	}

	private void updateFVector() {
		double[] ove = observationMatrix[rows * cols];
		if (sensorReading[0] >= 0)
			ove = observationMatrix[sensorReading[0] * rows + sensorReading[1]];

		double[][] oM = vectorToDiagonalMatrix(ove);
		double[][] tT = transposeMatrix(transitionMatrix);
		fVector = multiplyMatrix(multiplyMatrix(oM, tT), fVector);

		int ind = 0;
		double max = 0;
		double sum = 0;

		for (int i = 0; i < fVector.length; i++)
			sum += fVector[i];

		for (int i = 0; i < fVector.length; i++) {
			fVector[i] = fVector[i] / (double) sum;
			if (fVector[i] > max) {
				max = fVector[i];
				ind = i;
			}
		}

		int xMax = ind / (rows * 4);
		int yMax = ind / 4 - xMax * rows;
		int xTrue = getCurrentTrueState()[0];
		int yTrue = getCurrentTrueState()[1];
		double distance = Math.sqrt(Math.pow(xMax - xTrue, 2) + Math.pow(yMax - yTrue, 2));
		distanceSum += distance;

		if (count == 100)
			System.out.println("Median distance after 100 steps: " + distanceSum / count);
	}

	private double[][] vectorToDiagonalMatrix(double[] vector) {
		double[][] temp = new double[vector.length][vector.length];
		for (int i = 0; i < vector.length; i++)
			temp[i][i] = vector[i];
		return temp;
	}

	private double[] multiplyMatrix(double[][] a, double[] x) {
		int m = a.length;
		int n = a[0].length;
		double[] y = new double[m];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				y[i] += a[i][j] * x[j];
		return y;
	}

	private double[][] multiplyMatrix(double[][] a, double[][] b) {
		int m1 = a.length;
		int n1 = a[0].length;
		int n2 = b[0].length;
		double[][] c = new double[m1][n2];
		for (int i = 0; i < m1; i++)
			for (int j = 0; j < n2; j++)
				for (int k = 0; k < n1; k++)
					c[i][j] += a[i][k] * b[k][j];
		return c;
	}

	private void fillFMatrix() {
		for (int i = 0; i < rows * cols * head; i++)
			fVector[i] = 1.0 / ((double) rows * cols * head);
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

	private double[][] transposeMatrix(double[][] m) {
		int rowLength = m.length;
		int colLength = m[0].length;

		double[][] t = new double[colLength][rowLength];
		for (int i = 0; i < rowLength; i++)
			for (int j = 0; j < colLength; j++)
				t[j][i] = m[i][j];

		return t;
	}

	private boolean twoFromPosition(int rX, int rY, int x, int y) {
		return rX == x && rY == y + 2 || rX == x && rY == y - 2 || rX == x + 2 && rY == y || rX == x - 2 && rY == y
				|| rX == x + 1 && rY == y + 2 || rX == x + 1 && rY == y - 2 || rX == x + 2 && rY == y + 2
				|| rX == x + 2 && rY == y - 2 || rX == x + 2 && rY == y - 1 || rX == x + 2 && rY == y + 1
				|| rX == x - 1 && rY == y + 2 || rX == x - 1 && rY == y - 2 || rX == x - 2 && rY == y + 1
				|| rX == x - 2 && rY == y - 1 || rX == x - 2 && rY == y + 2 || rX == x - 2 && rY == y - 2;
	}

	private boolean oneFromPosition(int rX, int rY, int x, int y) {
		return rX == x && rY == y - 1 || rX == x && rY == y + 1 || rX == x + 1 && rY == y || rX == x - 1 && rY == y
				|| rX == x + 1 && rY == y + 1 || rX == x - 1 && rY == y - 1 || rX == x + 1 && rY == y - 1
				|| rX == x - 1 && rY == y + 1;
	}

	private boolean truePosition(int rX, int rY, int x, int y) {
		return rX == x && rY == y;
	}
}