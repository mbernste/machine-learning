package test;

import java.util.ArrayList;

import bayes_network.kullback_leibler.KLDivergence;
import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;

public class KL_Test {

	public static void main(String[] args) {
		ArffReader ar = new ArffReader();
		DataSet dataP = ar.readFile("data/kl_test1.arff");
		DataSet dataQ = ar.readFile("data/kl_test2.arff");
		ArrayList<Attribute> attrs = dataP.getAttributeList();
		System.out.println(KLDivergence.divergence(dataP, dataQ, attrs));
	}

}
