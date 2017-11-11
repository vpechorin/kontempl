package net.pechorina.kontempl.service;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProfilingService {
	static final Logger logger = LoggerFactory.getLogger(ProfilingService.class);
	
	private Stopwatch stopwatch;

	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	public void setStopwatch(Stopwatch stopWatch) {
		this.stopwatch = stopWatch;
	}
	
	public void onRequestStart(String msg) {
		stopwatch = Stopwatch.createStarted();
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
