/**
 * 
 */
package io.github.alantcote.browsersfour;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 * Wrapper for {@link AnimationTimer} with a programmable period.
 */
public class Ticker extends AnimationTimer {
	protected long period = -1;
	protected long nextTick = Long.MAX_VALUE;
	protected Runnable target = null;

	/**
	 * Construct a new object.
	 * @param aRunnable object to be run in the JavaFX thread on ticks.
	 * @param aPeriod the number of milliseconds between ticks.
	 */
	public Ticker(Runnable aRunnable, long aPeriod) {
		super();
		
		target = aRunnable;
		period = aPeriod;
		nextTick = period + System.currentTimeMillis();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(long now) {
		long sysTime = System.currentTimeMillis();
		
//		System.out.println("Ticker.handle(): sysTime = " + sysTime + "; nextTick = " + nextTick);

		if (nextTick <= sysTime) {
			// invoke the target Runnable after delay of at least period millis
			if (target != null) {
				Platform.runLater(target);
			}

			nextTick = sysTime + period;
		}
	}
}
