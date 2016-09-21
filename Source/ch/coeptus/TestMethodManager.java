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
import ch.nolix.common.util.Time;

//class
public class TestMethodManager {

	//static method
	public static void testCandleSticks(
		String productSymbol,
		Time startDate,
		Time endDate,
		SequencePattern<VolumeCandleStick> sequencePattern
	) {
		final List<VolumeCandleStick> candleSticks
		= FinanceDataProvider.getDailyCandleSticks(
			productSymbol,
			startDate,
			endDate
		);
		
		List<List<VolumeCandleStick>> sequences = candleSticks.getSequences(sequencePattern);
		
		double successRatio = sequences.getRatio(s -> s.getRefLast().isBullish());
		System.out.println("Success ration of " + productSymbol + ": " + successRatio);
	}
	
	//private constructor
	private TestMethodManager() {}
}
