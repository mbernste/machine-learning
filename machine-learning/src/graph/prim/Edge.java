package graph.prim;

import java.util.Comparator;

import pair.Pair;

/**
 * Implements a directed edge in a weighted graph where edge weights are 
 * floating point values and nodes are represented by unique integers. 
 *
 */
public class Edge
{
    /**
     * The two vertices that constitute this edge.  We consider the edge
     * points:
     * <br>
     * <br>
     * first -> second
     */
    Pair<Integer, Integer> vertices;

    /**
     * The edge weight
     */
    Double weight;

    /**
     * Compares edges by weight
     */
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

    /**
     * Constructor
     * 
     * @param vertices the two vertices in the edge
     * @param weight the weight of the edge
     */
    public Edge(Pair<Integer, Integer> vertices, Double weight)
    {
        this.vertices = vertices;
        this.weight = weight;
    }
    
    public Edge(Integer origin, Integer destination, Double weight)
    {
        this.vertices = new Pair<Integer, Integer>(origin, destination);
    }

    /**
     * @return the edge weight
     */
    public Double getWeight()
    {
        return this.weight;
    }

    /**
     * @return the pair of vertices
     */
    public Pair<Integer, Integer> getVertices()
    {
        return this.vertices;
    }

    /**
     * @return the first vertex
     */
    public Integer getFirstVertex()
    {
        return this.vertices.getFirst();
    }

    /**
     * @return the second vertex
     */
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

