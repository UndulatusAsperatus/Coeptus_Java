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
import ch.nolix.common.util.Time;

//launcher class
final class Launcher {

	//main method
	public static void main(String[] args) {
		
		double alpha = 0.0099;
		int successorCount = 45;
				
		//Create sequence pattern.
		SequencePattern<VolumeCandleStick> sequencePattern
		= new SequencePattern<VolumeCandleStick>()
		.forCount(1).addBlankForNext()
		.forCount(2).addNextWithCondition(cs -> cs.isBearish())
		.addNextWithCondition(cs -> cs.isHammer(0.45) || cs.isInvertedHammer(0.5))
		.forCount(1 + successorCount).addBlankForNext()
		.addSequenceCondition(s -> s.getRefAt(4).bodyIsBelowBodyOf(s.getRefAt(5)));	
		
		//Create success norm for sequences that match the sequence pattern and that fulfil the sequence condition.
		IBooleanNorm<List<VolumeCandleStick>> successNorm
		= (s)
		-> {
			double buyPrice = s.getRefAt(5).getClosingPrice();
			double lowerLimit = (1.0 - 0.9 * alpha) * buyPrice;
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
		for (String ps: NYSEProductSymbolManager.getProductSymbols()) {
			s += TestMethodManager.testCandleSticksAndGetSuccessRatio(
				ps,
				new Time(2000, 1, 1),
				new Time(2016, 9, 21),
				sequencePattern,
				successNorm
			);
			n++;
		}
		
		System.out.println("n = " + n);
		System.out.println("average success ratio: " + s / n);
	}
}
