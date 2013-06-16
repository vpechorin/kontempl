package net.pechorina.kontempl.service;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import org.apache.log4j.Logger;

public class ProfilingService {
	private static final Logger logger = Logger.getLogger(ProfilingService.class);
	
	private Stopwatch stopwatch;

	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	public void setStopwatch(Stopwatch stopWatch) {
		this.stopwatch = stopWatch;
	}
	
	public void onRequestStart(String msg) {
		stopwatch = new Stopwatch().start();
		if (msg != null) {
			logger.debug("Start: " + msg);
		}
	}
	
	public long logElapsedTime(String msg) {
		long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		if (msg == null) {
			msg = "elapsed time: ";
		}
		logger.debug(msg + millis + " ms");
		return millis;
	}
}
