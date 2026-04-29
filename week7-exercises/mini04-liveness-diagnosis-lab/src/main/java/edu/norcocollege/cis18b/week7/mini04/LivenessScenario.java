package edu.norcocollege.cis18b.week7.mini04;

public class LivenessScenario {
	private final String name;
	private final LivenessIssue issue;
	private final String symptom;
	private final String mitigation;

	public LivenessScenario(String name, LivenessIssue issue, String symptom, String mitigation) {
		this.name = name;
		this.issue = issue;
		this.symptom = symptom;
		this.mitigation = mitigation;
	}

	public String name() {
		return name;
	}

	public LivenessIssue issue() {
		return issue;
	}

	public String symptom() {
		return symptom;
	}

	public String mitigation() {
		return mitigation;
	}
}