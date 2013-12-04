package prim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import pair.Pair;

public class Prim 
{
	public final static Double NO_EDGE = -1.0;
	public final static int FIRST_VERTEX = 0;
	
	private Double[][] graph; 
	
	public static ArrayList<Edge> runPrims(Double[][] graph)
	{
		Set<Integer> verticesInTree = new HashSet<Integer>();
		verticesInTree.add(FIRST_VERTEX);
		
		PriorityQueue<Edge> existingEdges = 
				new PriorityQueue<Edge>(graph.length * graph.length,
									    Edge.EDGE_ORDER);
		
		// Add all edges of the first node to the priority queue
		addAllEdges(graph, FIRST_VERTEX, verticesInTree, existingEdges);
		
		// Create the set of Edges that constitute the spanning tree
		PriorityQueue<Edge> newEdges = new  PriorityQueue<Edge>(2 * graph.length,
		        											Edge.EDGE_ORDER);
		
		while (verticesInTree.size() < graph.length)
		{		
			Edge newEdge;
			Integer newVertex = -1;
			
			boolean isInTree = true;
			while (isInTree)
			{				
				newEdge = existingEdges.remove();
				
				if ( ! (verticesInTree.contains( newEdge.getFirstVertex() ) &&
					    verticesInTree.contains( newEdge.getSecondVertex() ))  )
				{					
					newEdges.add(newEdge);
					isInTree = false;
					
					if (!verticesInTree.contains(newEdge.getFirstVertex()))
					{
						newVertex = newEdge.getFirstVertex();
					}
					else
					{
						newVertex = newEdge.getSecondVertex();
					}
				}	
			}
			
			verticesInTree.add(newVertex);
			addAllEdges(graph, newVertex, verticesInTree, existingEdges);
		}
		
		return new ArrayList<Edge>(newEdges);
		
	}
	
	public static void addAllEdges(Double[][] graph, 
								   Integer newVertex,
								   Set<Integer> verticesInTree,
								   PriorityQueue<Edge> existingEdges)
	{
		for (int c = 0; c < graph.length; c++)
		{
			if (graph[newVertex][c] != NO_EDGE)
			{
				// Set the nodes that make up the edge (i.e the new vertex
				// and current column
				Pair<Integer, Integer> nodes = new Pair<Integer, Integer>();
				nodes.setFirst(newVertex);
				nodes.setSecond(c);
				
				// Set the weight of this edge as specified in the graph
				Double weight = graph[newVertex][c];
				
				// Create the Edge and add it to the priority queue
				Edge e = new Edge(nodes, weight);
				existingEdges.add(e);
			}
		}
	}
	
	public static class Edge
	{
		Pair<Integer, Integer> vertices;
		Double weight;
		
		public static final Comparator<Edge> EDGE_ORDER = 
	            new Comparator<Edge>() 
	            {
					public int compare(Edge e1, Edge e2) 
					{
						if (e1.weight == e2.weight)
						{
							return 0;
						}
						else if (e1.weight < e2.weight)
						{
							return 1;
						}
						else
						{
							return -1;
						}
					}
	            };
		
		public Edge(Pair<Integer, Integer> nodes, Double weight)
		{
			this.vertices = nodes;
			this.weight = weight;
		}
		
		public Double getWeight()
		{
			return this.weight;
		}
		
		public Pair<Integer, Integer> getVertices()
		{
			return this.vertices;
		}
		
		public Integer getFirstVertex()
		{
			return this.vertices.getFirst();
		}
		
		public Integer getSecondVertex()
		{
			return this.vertices.getSecond();
		}
		
		@Override
		public String toString()
		{
			String result = "<";
			result += this.vertices.getFirst();
			result += "--";
			result += this.weight;
			result += "--";
			result += this.vertices.getSecond();
			result += ">";
			
			return result;
		}
	}
	
}
