package test;

import java.util.ArrayList;

import bayes_network.kullback_leibler.KLDivergence;
import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;

public class KL_Test {
	private static final int CORRECTNESS = 0;
	private static final int STRESS = 1;
	private static int mode = STRESS;
	
	public static void main(String[] args) {
		ArffReader ar = new ArffReader();
		DataSet dataP = null;
		DataSet dataQ = null;
		
		switch(mode){
		case(CORRECTNESS):
			dataP = ar.readFile("data/kl_test1.arff");
			dataQ = ar.readFile("data/kl_test2.arff");
			break;
		case(STRESS):
			dataP = ar.readFile("data/lymph_train.arff");
			dataQ = ar.readFile("data/lymph_train.arff");
			break;
		}
		Attribute a = dataP.getAttributeById(0);
		Attribute b = dataP.getAttributeById(1);
		System.out.println(KLDivergence.divergence(dataP, dataQ, a, b));
	}

}
