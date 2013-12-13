package core.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Graph 
{
    Map<Integer, Vertex> vertices;
    
    private static int currId = 0;
    
    public Graph()
    {
        vertices = new HashMap<Integer, Vertex>();
    }
    
    public void addEdge(Vertex origin, Vertex destination, Double Weight)
    {
        origin.addOutgoing(destination, Weight);
        destination.addIncoming(origin, Weight);
    }
    
    public void addVertex(Vertex newVertex)
    {
        vertices.put(currId++, newVertex);
    }
    
    public Collection<Vertex> getVertices()
    {
        return vertices.values();
    }

}
