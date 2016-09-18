/*
 * file:	Launcher.java
 * author:	Silvan Wyss
 * month:	2016-09
 */

//package declaration
package ch.coeptus.samples;

//own imports
import ch.nolix.common.container.List;
import ch.nolix.common.dataProvider.CandleStick;
import ch.nolix.common.dataProvider.FinanceDataProvider;
import ch.nolix.common.dataProvider.NYSEProductSymbolManager;
import ch.nolix.common.util.Time;

//launcher class
final class Launcher {

	//main method
	public static void main(String[] args) {
				
		List<CandleStick> candleSticks
		= FinanceDataProvider.getDailyCandleSticks(
			NYSEProductSymbolManager.BOEING,
			new Time(2016, 9, 12),
			new Time(2016, 9, 16)
		);
			
		candleSticks.forEach(
			cs -> System.out.println(cs.getRefTime().toString() + " " + cs.getOpeningPrice() + "$")
		);
	}
}
