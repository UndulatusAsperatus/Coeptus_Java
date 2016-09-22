/*
 * file:	Launcher.java
 * author:	Silvan Wyss
 * month:	2016-09
 */

//package declaration
package ch.coeptus;

//own imports
import ch.nolix.common.container.List;
import ch.nolix.common.container.SequencePattern;
import ch.nolix.common.dataProvider.NYSEProductSymbolManager;
import ch.nolix.common.dataProvider.VolumeCandleStick;
import ch.nolix.common.functional.IBooleanNorm;
import ch.nolix.common.functional.ISelector;
import ch.nolix.common.mathematics.Calculator;
import ch.nolix.common.sequencer.Sequencer;
import ch.nolix.common.util.Time;

//launcher class
final class Launcher {

	//main method
	public static void main(String[] args) {
		
		double alpha = 0.0098;
		int successorCount = 5;
		
		//Create sequence pattern.
		SequencePattern<VolumeCandleStick> sequencePattern
		= new SequencePattern<VolumeCandleStick>()
		.addBlankForNext()
		.addConditionForNext(cs -> cs.isBearish())
		.addConditionForNext(cs -> cs.isBearish())
		.addConditionForNext(cs -> cs.isHammer(0.454))
		.addBlankForNext();
		Sequencer.forCount(successorCount).run(()->sequencePattern.addBlankForNext());

		//Creates sequence condition.
		ISelector<List<VolumeCandleStick>> sequenceCondition
		= (s)
		-> {
			VolumeCandleStick hammer = s.getRefAt(4);
			VolumeCandleStick next = s.getRefAt(5);
	
			return (
				Calculator.getMin(next.getOpeningPrice(), next.getClosingPrice())
				> Calculator.getMax(hammer.getOpeningPrice(), hammer.getClosingPrice())
			);
		};
		
		//Create success norm for sequences that match the sequence pattern and that fulfil the sequence condition.
		IBooleanNorm<List<VolumeCandleStick>> successNorm
		= (s)
		-> {
			double buyPrice = s.getRefAt(5).getClosingPrice();
			double lowerLimit = (1.0 - alpha) * buyPrice;
			double upperLimit = (1.0 + alpha) * buyPrice;
			
			int index = 1;
			for (VolumeCandleStick vcs: s) {
				if (index > 5) {
					if (vcs.getClosingPrice() < lowerLimit) {
						return false;
					}
					if (vcs.getClosingPrice() >  upperLimit) {
						return true;
					}
				}
				index++;
			}
			
			return false;
		};
		
		int n = 0;
		double s = 0.0;
		for (String ps: NYSEProductSymbolManager.getProductSymbols().addAtEnd("NMR", "ABB", "V", "B", "C", "NOK", "NTDOY", "OAS", "PER", "PWR", "Q", "SBUX", "W", "TSLA", "UTX", "UA", "UNH", "VZ", "VRX", "WMT", "WFC", "WMB", "YUM", "YELP", "LAZ", "GS", "UBS", "CS", "MS", "DB")) {		
			s += TestMethodManager.testCandleSticksAndGetSuccessRatio(
				ps,
				new Time(2000, 1, 1),
				new Time(2016, 9, 21),
				sequencePattern,
				sequenceCondition,
				successNorm
			);
			n++;
		}
		
		System.out.println("n = " + n);
		System.out.println("average success ratio: " + s / n);
	}
}
