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
		
		System.out.println("hammers of " + productSymbol + ": " + candleSticks.getCount(cs -> cs.isHammer(0.6)));
		
		SequencePattern<VolumeCandleStick> p
		= new SequencePattern<VolumeCandleStick>()
		.addNextWithCondition(vcs -> vcs.isHammer(0.6))
		.addBlankForNext()
		.addSequenceCondition(s -> s.getRefAt(1).bodyIsBelowBodyOf(s.getRefAt(2)));
		System.out.println("hammers with body below next of " + productSymbol + ": " + candleSticks.getSequences(p).getSize());
		
		List<List<VolumeCandleStick>> sequences
		= candleSticks.getSequences(sequencePattern);
		
		double successRatio = sequences.getRatio(s -> successNorm.getValue(s));
		//System.out.println(productSymbol + " " + sequences.getSize());
		System.out.println("Success ratio of " + productSymbol + ": " + successRatio);
		return successRatio;
	}
	
	//private constructor
	private TestMethodManager() {}
}
