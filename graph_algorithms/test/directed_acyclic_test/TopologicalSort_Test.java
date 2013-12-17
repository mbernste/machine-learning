package directed_acyclic_test;

import java.util.ArrayList;

import directed_acyclic.TopologicalSort;

public class TopologicalSort_Test 
{
    public static void main(String[] args)
    {
        Double[][] dag = {
                            {null, 1.0, null, null, null, null, null},
                            {null, null, null, null, null, null, 1.0},
                            {null, null, null, 1.0, null, null, null},
                            {1.0, null, null, null, 1.0, null, null},
                            {null, null, null, null, null, null, 1.0},
                            {null, null, null, 1.0, null, null, null},
                            {null, null, null, null, null, null, null}
                         };
        
        ArrayList<Integer> sorted = TopologicalSort.run(dag);
        System.out.println(sorted);
    }
    

}
