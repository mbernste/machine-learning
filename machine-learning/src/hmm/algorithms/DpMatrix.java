package hmm.algorithms;

import hmm.HMM;
import hmm.State;

import java.util.ArrayList;

import math.LogP;



import bimap.BiMap;

public class DpMatrix 
{
	private int numRows;
	private int numCols;
	
	/**
	 * Maps each state to a row
	 */
	private BiMap<State, Integer> stateRowMap;
	
	/**
	 * Map each time step (i.e. column) to a symbol
	 */
	private ArrayList<String> colSymbolMap;
	
	/**
	 * The matrix
	 */
	private DpMatrixElement[][] matrix;

	public DpMatrix(HMM model, String[] sequence)
	{		
		numRows = model.getNumStates();
		
		numCols = sequence.length + 1;
		
		initStateRowMap(model);
		initColSymbolMap(sequence);
		initMatrix();
	}
	
	public void initStateRowMap(HMM model)
	{
		stateRowMap = new BiMap<State, Integer>();
		
		ArrayList<State> states = new ArrayList<State>(model.getStateContainer()
		                                                    .getStates());
		
		for (int index = 0; index < states.size(); index++)
		{
			stateRowMap.put(states.get(index), index);
		}
	}
	
	public void initColSymbolMap(String[] sequence)
	{
		colSymbolMap = new ArrayList<String>();
		
		/*
		 *  The first time unit does not see a symbol emitted
		 */
		colSymbolMap.add("-");
		
		for (int i = 0; i < sequence.length; i++)
		{
			colSymbolMap.add(sequence[i]);
		}
	}
	
	public double getValue(State state, int timeUnit)
	{
		int row = stateRowMap.getValue(state);
		return matrix[row][timeUnit].getValue();
	}
	
	public void setValue(State state, int timeUnit, double value)
	{
		int row = stateRowMap.getValue(state);
		matrix[row][timeUnit].setValue(value);
	}
	
	public void setPreviousState(State currState, int timeUnit, State prevState)
	{
		/*
		 *  The current state's row 
		 */
		int currRow = stateRowMap.getValue(currState);
		
		/*
		 *  The previous state's row
		 */
		int backRow = stateRowMap.getValue(prevState);
		
		matrix[currRow][timeUnit].setRowPointer(backRow);
	}
	
	public State getPreviousState(State currState, int timeUnit)
	{
		/*
		 *  The current state's row 
		 */
		int currRow = stateRowMap.getValue(currState);
		
		/*
		 *  Get the row of the previous state from the current state
		 */
		int rowPointer = matrix[currRow][timeUnit].getRowPointer();
		
		/*
		 *  Return the state associated with this row
		 */
		return stateRowMap.getKey(rowPointer);
	}
	
	public void initMatrix()
	{
		matrix = new DpMatrixElement[numRows][numCols];

		/*
		 *  Initialize elements
		 */
		for (int r = 0; r < numRows; r++)
		{
			for (int c = 0; c < numCols; c++)
			{
				matrix[r][c] = new DpMatrixElement();
			}
		}
	}
	
	/**
	 * Print the score of each element in the dynamic
	 * programming matrix to standard output.
	 */
	@Override
	public String toString()
	{	
		String result = "";
		
		result += "\nDynamic Programming Matrix:\n\n";
		
		result += "\t\t";
		
		// Print the character over each columns
		for (String c : colSymbolMap)
		{
			result += (c + "\t");
		}
		result += "\n";
		
		for (int r = 0; r < numRows; r++)
		{
			result += ("[" + stateRowMap.getKey(r).getId() + "]\t");
			
			for (int c = 0; c < numCols; c++)
			{
				result += (LogP.exp(matrix[r][c].getValue()) + "\t");
			}
			result += "\n";
		}
		result += "\n";
			
		return result;
	}
	
	public int getNumColumns()
	{
		return this.numCols;
	}
	
}
