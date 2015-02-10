package graph;

import java.util.ArrayList;
import java.util.List;

public class Path<T>
{
    /**
     * The first node on the path
     */
    private T origin;
    
    /**
     * The final node on the path
     */
    private T destination;
    
    /**
     * The path's distance
     */
    private double length;
    
    /**
     * List of nodes representing the path
     */
    private List<T> path;
    
    public Path(T origin, T destinationNode)
    {
        this.origin = origin;
        this.destination = destinationNode;
        this.path = new ArrayList<>();  
        this.path.add(this.origin);
    }
    
    public void appendNodeToPath(T nextNode, Double edgeLength)
    {
        length += edgeLength;
        path.add(nextNode);
    }
    
    public List<T> getNodesOnPath()
    {
        return this.path;
    }
    
    public T getPathOrigin()
    {
        return this.origin;
    }
    
    public T getPathDestination()
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

