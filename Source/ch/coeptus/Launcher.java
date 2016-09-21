/*
 * file:	Launcher.java
 * author:	Silvan Wyss
 * month:	2016-09
 */

//package declaration
package ch.coeptus;

//own imports
import ch.nolix.common.container.SequencePattern;
import ch.nolix.common.dataProvider.NYSEProductSymbolManager;
import ch.nolix.common.dataProvider.VolumeCandleStick;
import ch.nolix.common.util.Time;

//launcher class
final class Launcher {

	//main method
	public static void main(String[] args) {
		
		SequencePattern<VolumeCandleStick> sequencePattern
		= new SequencePattern<VolumeCandleStick>()
		.addConditionForNext(cs -> cs.isBearish())
		.addConditionForNext(cs -> cs.isBearish())
		.addConditionForNext(cs -> cs.isBearish())
		.addConditionForNext(cs -> cs.isHammer())
		.addConditionForNext(cs -> cs.isBullish())
		.addBlankForNext();
		
		for (String ps: NYSEProductSymbolManager.getProductSymbols()) {		
			TestMethodManager.testCandleSticks(
				ps,
				new Time(2014, 1, 1),
				new Time(2016, 9, 16),
				sequencePattern
			);
		}
	}
}
