package edu.norcocollege.cis18b.week7.mini06;

import java.util.Map;

public class CountSummary {
	private final int processedTokens;
	private final Map<String, Integer> counts;

	public CountSummary(int processedTokens, Map<String, Integer> counts) {
		this.processedTokens = processedTokens;
		this.counts = counts;
	}

	public int processedTokens() {
		return processedTokens;
	}

	public Map<String, Integer> counts() {
		return counts;
	}
}