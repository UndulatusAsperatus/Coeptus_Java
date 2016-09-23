/*
 * file:	TestMethodManager.java
 * author:	Silvan Wyss
 * month:	2016-09
 */

//package declaration
package ch.coeptus;

//own imports
import ch.nolix.common.container.List;
import ch.nolix.common.container.SequencePattern;
import ch.nolix.common.dataProvider.FinanceDataProvider;
import ch.nolix.common.dataProvider.VolumeCandleStick;
import ch.nolix.common.functional.IBooleanNorm;
import ch.nolix.common.util.Time;

//class
public class TestMethodManager {

	//static method
	public static double testCandleSticksAndGetSuccessRatio(
		String productSymbol,
		Time startDate,
		Time endDate,
		SequencePattern<VolumeCandleStick> sequencePattern,
		IBooleanNorm<List<VolumeCandleStick>> successNorm
	) {
		final List<VolumeCandleStick> candleSticks
		= FinanceDataProvider.getDailyCandleSticks(
			productSymbol,
			startDate,
			endDate
		);
		
		System.out.println("hammers of " + productSymbol + ": " + candleSticks.getCount(cs -> cs.getLowerWick() / cs.getLength() >= 0.45));
		
		SequencePattern<VolumeCandleStick> hammerWithBodyBelowNextPattern
		= new SequencePattern<VolumeCandleStick>()
		.addNextWithCondition(vcs -> vcs.getLowerWick() / vcs.getLength() >= 0.45)
		.addBlankForNext()
		.addSequenceCondition(s -> s.getRefAt(1).bodyIsBelowBodyOf(s.getRefAt(2)));
		System.out.println("hammers with body below next of " + productSymbol + ": " + candleSticks.getSequences(hammerWithBodyBelowNextPattern).getSize());
		
		List<List<VolumeCandleStick>> sequences
		= candleSticks.getSequences(sequencePattern);
		
		System.out.println("potential sequences  of " + productSymbol + ": " + sequences.getSize()); 
		System.out.println("successful sequences  of " + productSymbol + ": " + sequences.getCount(cs -> successNorm.getValue(cs))); 
		
		double successRatio = sequences.getRatio(s -> successNorm.getValue(s));
		System.out.println("Success ratio of " + productSymbol + ": " + successRatio);
		return successRatio;
	}
	
	//private constructor
	private TestMethodManager() {}
}
