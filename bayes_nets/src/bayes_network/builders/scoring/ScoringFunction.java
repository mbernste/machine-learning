package bayes_network.builders.scoring;

import bayes_network.BayesianNetwork;
import data.DataSet;

public interface ScoringFunction 
{
    public Double scoreNet(BayesianNetwork net, DataSet data);
}
