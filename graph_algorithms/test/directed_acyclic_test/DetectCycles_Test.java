package directed_acyclic_test;

import java.util.ArrayList;

import directed_acyclic.DetectCycles;
import directed_acyclic.TopologicalSort;

public class DetectCycles_Test 
{
    public static void main(String[] args)
    {
        Double[][] nocycle = {
                            {null, 1.0, null, null, null, null, null},
                            {null, null, null, null, null, null, 1.0},
                            {null, null, null, 1.0, null, null, null},
                            {1.0, null, null, null, 1.0, null, null},
                            {null, null, null, null, null, null, 1.0},
                            {null, null, null, 1.0, null, null, null},
                            {null, null, null, null, null, null, null}
                           };
        
        Double[][] cycle = {
                            {null, 1.0, null, null, null, 1.0, null},
                            {null, null, null, null, null, null, 1.0},
                            {null, null, null, 1.0, null, null, null},
                            {1.0, null, null, null, 1.0, null, null},
                            {null, null, null, null, null, null, 1.0},
                            {null, null, null, 1.0, null, null, null},
                            {null, null, null, null, null, null, null}
                           };
        
        System.out.println(DetectCycles.run(nocycle));
        System.out.println(DetectCycles.run(cycle));
    }
    

}
