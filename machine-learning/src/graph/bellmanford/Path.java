package graph.bellmanford;

import java.util.ArrayList;
import java.util.List;

public class Path 
{
    /**
     * The first node on the path
     */
    private int origin;
    
    /**
     * The final node on the path
     */
    private int destination;
    
    /**
     * The path's distance
     */
    private double length;
    
    /**
     * List of nodes representing the path
     */
    private List<Integer> path;
    
    public Path(Integer origin, Integer destinationNode)
    {
        this.origin = origin;
        this.destination = destinationNode;
        this.path = new ArrayList<>();  
        this.path.add(this.origin);
    }
    
    public void appendNodeToPath(Integer nextNode, Double edgeLength)
    {
        length += edgeLength;
        path.add(nextNode);
    }
    
    public List<Integer> getNodesOnPath()
    {
        return this.path;
    }
    
    public int getPathOrigin()
    {
        return this.origin;
    }
    
    public int getPathDestination()
    {
        return this.destination;
    }
    
    public double getPathLength()
    {
        return this.length;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        for (int i = 0; i < path.size() - 1; i++)
        {
            str += path.get(i) + " --> ";
        }
        str += path.get(path.size() - 1);
        str += " : " + this.length;
        return str;
    }
}

