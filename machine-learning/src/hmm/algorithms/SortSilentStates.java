package hmm.algorithms;

import graph.dag.TopologicalSort;
import hmm.HMM;
import hmm.State;
import hmm.Transition;

import java.util.ArrayList;
import java.util.Collection;

import bimap.BiMap;

public class SortSilentStates 
{
	public static ArrayList<State> run(HMM model)
	{
		Collection<State> silentStates = model.getSilentStates(); 
		
		ArrayList<State> sorted = new ArrayList<State>();
		
		BiMap<Integer, String> indices  = new BiMap<Integer, String>();
		
		int numNodes = silentStates.size();
		Double[][] graph = new Double[numNodes][numNodes];
        
        /*
         * Initialize every element in graph to null
         */
        for (int r = 0; r < numNodes; r++)
        {
            for (int c = 0; c < numNodes; c++)
            {
                graph[r][c] = null;
            }
        }
        
		int count = 0;
		for (State s : silentStates)
		{
			indices.put(count++, s.getId());
		}
		
		for (State s : silentStates)
		{	
			for (Transition t : s.getTransitions())
			{
				String dId = t.getDestinationId();
				String oId = s.getId();
				if (indices.containsValue(t.getDestinationId()))
				{
					graph[indices.getKey(oId)][indices.getKey(dId)] = 1.0;
				}
				
			}
		}
        
        ArrayList<Integer> sortedIndices = TopologicalSort.run(graph);
        
        for (Integer i : sortedIndices)
        {
        	sorted.add(model.getStateById(indices.getValue(i)));
        }
		
		return sorted;
	}
}
