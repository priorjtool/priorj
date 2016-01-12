package com.edu.ufcg.splab.priorj.technique;

import java.util.Comparator;

public class EchelonComparator implements Comparator<TestCaseProposal> {

	public static final int BY_SCORE = 1;  
    public static final int BY_WEIGHT = 2;  
    private int comparatorType;
    
	public EchelonComparator(final int comparatorType) {
		super();
		this.comparatorType = comparatorType;
	}

	@Override
	public int compare(TestCaseProposal obj1,
			TestCaseProposal obj2) {
        switch (this.comparatorType) {  
            case BY_SCORE:  
                Double score1 = new Double(obj1.getScore());
                Double score2 = new Double(obj2.getScore());
            	return score1.compareTo(score2);  
            case BY_WEIGHT:  
            	Double weight1 = new Double(obj1.getWeight());
                Double weight2 = new Double(obj2.getWeight());
            	return weight1.compareTo(weight2);
            default:  
                throw new RuntimeException("Invalid Option!");  
        }  
	}

}
