package hmm.algorithms;

public class DpMatrixElement 
{
	/**
	 * Pointer to the row of the element that determined this element's
	 * value
	 */
	private int rowPointer;
	
	/**
	 * Pointer to the column of the element that determined this element's
	 * value
	 */
	private int columnPointer;

	/**
	 * This element's value
	 */
	private double value = Double.NaN;
	
	public double getValue()
	{
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	public void setBackPointer(int row, int column)
	{
		this.rowPointer = row;
		this.columnPointer = column;
	}
	
	public void setRowPointer(int row)
	{
		this.rowPointer = row;
	}
	
	public void setColumnPointer(int column)
	{
		this.columnPointer = column;
	}
	
	public int getRowPointer()
	{
		return this.rowPointer;
	}
	
	public int getColumnPointer()
	{
		return this.columnPointer;
	}
	
}
