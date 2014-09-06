package bayes.structuresearch.score;

import data.DataSet;

import bayes.BayesianNetwork;

public interface ScoringFunction 
{
    public Double scoreNet(BayesianNetwork net, DataSet data);
}
