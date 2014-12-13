package examples;
import math.LogP;
import pair.Pair;

import hmm.HMM;
import hmm.State;
import hmm.StateSilent;
import hmm.Transition;
import hmm.algorithms.BackwardAlgorithm;
import hmm.algorithms.DpMatrix;
import hmm.algorithms.ForwardAlgorithm;


public class HMMExamples 
{
    /**
     * Example running the forward and backward algorithms on an HMM object.
     */
    public static void main(String[] args)
    {
      
        HMM toyHmm = buildToyHMM();  
        String[] sequence = {"x", "y", "x", "x"};
         
        /*
         * Forward algorithm
         */
        Pair<Double, DpMatrix> resultF = ForwardAlgorithm.run(toyHmm, sequence);
        
        /*
         * Backward algorithm
         */
        Pair<Double, DpMatrix> resultB = BackwardAlgorithm.run(toyHmm, sequence);

        System.out.println("Probability of sequence: " 
                                + LogP.exp(resultF.getFirst()));
    }
    
    /**
     * Example building an HMM object
     * 
     * @return an example HMM
     */
    public static HMM buildToyHMM()
    {
        HMM hmm = new HMM();
        
        /*
         * Create states
         */
        State A = new StateSilent("A");     
        State B = new StateSilent("B");
        State C = new StateSilent("C");     
        State D = new StateSilent("D");
        State E = new State("E");
        State G = new State("G");
        
        /*
         * Add transitions between states
         */
        A.addTransition(new Transition("A", "E", LogP.ln(1.0 / 3.0)));
        A.addTransition(new Transition("A", "X", LogP.ln(1.0 / 3.0)));
        A.addTransition(new Transition("A", "B", LogP.ln(1.0 / 3.0)));
        B.addTransition(new Transition("B", "C", LogP.ln(0.5)));
        B.addTransition(new Transition("B", "G", LogP.ln(0.5)));
        C.addTransition(new Transition("C", "D", LogP.ln(1.0)));
        D.addTransition(new Transition("D", "E", LogP.ln(1.0)));
        E.addTransition(new Transition("E", "E", LogP.ln(0.5)));
        E.addTransition(new Transition("E", "G", LogP.ln(0.5)));
        
        /*
         * Create emission distribution for state E
         */
        E.addEmission("x", LogP.ln(0.5));
        E.addEmission("y", LogP.ln(0.5));
      
        /*
         * Create emission distribution for state G
         */
        G.addEmission("x", LogP.ln(0.9));
        G.addEmission("y", LogP.ln(0.1));
        
        /*
         * Add states to model
         */
        hmm.addState(B);
        hmm.addState(D);
        hmm.addState(C);
        hmm.addState(E);
        hmm.addState(G);
        hmm.addState(A);

        /*
         * Set the begin state
         */
        hmm.setBeginStateId("A");
        hmm.setEndStateId("E");
        
        return hmm;
    }    
}
