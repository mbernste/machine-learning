package graph.floydwarshall;

import graph.Path;

import java.util.Map;
import java.util.Map.Entry;

import bimap.BiMap;

public class AllPairsShortestPaths<T> 
{
	private final BiMap<Integer, T> indexToNode;
	
	private final Double[][] distanceMatrix;
	
	private final Integer[][] nextNodeMatrix;
	
	public AllPairsShortestPaths(Double[][] distanceMatrix, Integer[][] nextNodeMatrix, Map<Integer, T> indexToNode)
	{
		this.indexToNode = new BiMap<>();
		for (Entry<Integer, T> e : indexToNode.entrySet())
		{
			this.indexToNode.put(e.getKey(), e.getValue());
		}
		
		this.distanceMatrix = distanceMatrix;
		this.nextNodeMatrix = nextNodeMatrix;
	}
	
	public Path<T> getPath(T origin, T destination)
	{
		if (!pathExists(origin, destination))
		{
			return null;
		}
		
		Path<T> path = new Path<>(origin, destination);
		
		Integer currNodeIndex = indexToNode.getKey(origin);
		Integer destNodeIndex = indexToNode.getKey(destination);
		
		while (!currNodeIndex.equals(destNodeIndex))
		{	
			Integer nextNodeIndex = nextNodeMatrix[currNodeIndex][destNodeIndex];
			
			Double currDistance = distanceMatrix[currNodeIndex][destNodeIndex];
			Double nextNodeDistance = distanceMatrix[nextNodeIndex][destNodeIndex];
			Double edgeWeight = currDistance - nextNodeDistance;
			
			path.appendNodeToPath(indexToNode.getValue(nextNodeIndex), edgeWeight); // TODO CALCULATE THE ACTUAL DISTANCE
			currNodeIndex = nextNodeIndex;
		}
		
		return path;
	}
	
	public boolean pathExists(T origin, T destination)
	{
		if (nextNodeMatrix[indexToNode.getKey(origin)][indexToNode.getKey(destination)] == null)
		{
			return false;
		}
		return true;
	}
	
}
