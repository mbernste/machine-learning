package graph.floydwarshall;

import graph.DirectedGraph;
import graph.Path;

import java.util.HashMap;
import java.util.Map;

import pair.Pair;

/**
 *	An implementation of the Floyd-Warshall algorithm for computing all-pairs
 *	shortest paths on a directed graph with no negative cycles. 
 *
 */
public class FloydWarshall 
{
	 public static <T> AllPairsShortestPaths<T> runFloydWarshall(DirectedGraph<T> graph)
     {
		 Map<Integer, T> indexToNode = mapNodesToIndices(graph);
	
		 Pair<Double[][], Integer[][]> matrices = initializeMatrices(graph, indexToNode);
		 
		 Double[][] distanceMatrix = matrices.getFirst();
		 Integer[][] nextNodeMatrix = matrices.getSecond();
	
		 matrices = computeShortestPaths(distanceMatrix, nextNodeMatrix, graph, indexToNode);
		 
		 return new AllPairsShortestPaths<>(matrices.getFirst(), matrices.getSecond(), indexToNode);
     }
	 
	 private static <T> Map<Integer, T> mapNodesToIndices(DirectedGraph<T> graph)
	 {
		 Map<Integer, T> indexToNode = new HashMap<>();
		 int nodeIndex = 0;
		 for (T node : graph.getNodes())
		 {
			 indexToNode.put(new Integer(nodeIndex++), node);
		 }
		 return indexToNode;
	 }
	 
	 /**
	  * Initialize the matrices for storing shortest distances between nodes and the matrix
	  * storing the next node from each node along that shortest path.
	  * 
	  * @param graph the input graph
	  * @param indexToNode map of the node index to the node object
	  * @return the distance matrix and next node matrix
	  */
	 private static <T> Pair<Double[][], Integer[][]> initializeMatrices(DirectedGraph<T> graph, 
			 															 Map<Integer, T> indexToNode)
	 {
		 int numNodes = graph.getNodes().size();
		 Double[][] distanceMatrix = new Double[numNodes][numNodes];
		 Integer[][] nextNodeMatrix = new Integer[numNodes][numNodes];

		 for (int origIndex = 0; origIndex < numNodes; origIndex++)
		 {
			 for (int destIndex = 0; destIndex < numNodes; destIndex++)
			 {
				 T origin = indexToNode.get(origIndex);
				 T destination = indexToNode.get(destIndex);
				 
				 if (origIndex == destIndex)
				 {
					 distanceMatrix[origIndex][destIndex] = 0d;
					 nextNodeMatrix[origIndex][destIndex] = origIndex;
				 }
				 else if (graph.edgeExists(origin, destination))
				 {
					 distanceMatrix[origIndex][destIndex] = graph.getEdgeWeight(origin, destination);
					 nextNodeMatrix[origIndex][destIndex] = destIndex;
				 }
				 else
				 {
					 distanceMatrix[origIndex][destIndex] = Double.POSITIVE_INFINITY;
					 nextNodeMatrix[origIndex][destIndex] = null;
				 }
			 }
		 }
		 
		 return new Pair<>(distanceMatrix, nextNodeMatrix);
	 }
	 
	 private static <T> Pair<Double[][], Integer[][]> computeShortestPaths(Double[][] distanceMatrix, 
			 											Integer[][] nextNodeMatrix, 
			 											DirectedGraph<T> graph, 
			 											Map<Integer, T> indexToNode)
	 {
		 int numNodes = graph.getNodes().size();
		 for (int k = 0; k < numNodes; k++)
		 {
			 for (int i = 0; i < numNodes; i++)
			 {
				 for (int j = 0; j < numNodes; j++)
				 {
					 if (distanceMatrix[i][j] > distanceMatrix[i][k] + distanceMatrix[k][j])
					 {
						 distanceMatrix[i][j] = distanceMatrix[i][k] + distanceMatrix[k][j];
						 nextNodeMatrix[i][j] = nextNodeMatrix[i][k];
					 }
				 }
			 }
		 }
		 return new Pair<>(distanceMatrix, nextNodeMatrix);
	 }

}
