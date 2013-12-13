package core.graph;

import java.util.HashMap;
import java.util.Map;

public class Vertex 
{
    Map<Vertex, Double> outgoing;
    Map<Vertex, Double> incoming;
    
    private int id;
    
    public Vertex(int id)
    {
        this.id = id;
        outgoing = new HashMap<Vertex, Double>();
        incoming = new HashMap<Vertex, Double>();
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public void addOutgoing(Vertex out, Double weight)
    {
        outgoing.put(out, weight);
    }
    
    public void addIncoming(Vertex in, Double weight)
    {
        incoming.put(in, weight);
    }
    
}
