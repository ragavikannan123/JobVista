package org.recruitment.tests;

import org.recruitment.users.Applicant;

public class Result {
	private Applicant Applicant;
	private Test test;
	private int points;
	private boolean selectedOrNot;
	
	public Applicant getApplicant() {
		return Applicant;
	}
	public void setApplicant(Applicant Applicant) {
		this.Applicant = Applicant;
	}
	public Test getTestId() {
		return test;
	}
	public void setTestId(Test test) {
		this.test = test;
	}
	public int getpoints() {
		return points;
	}
	public void setpoints(int points) {
		this.points = points;
	}
	public boolean isSelectedOrNot() {
		return selectedOrNot;
	}
	public void setSelectedOrNot(boolean selectedOrNot) {
		this.selectedOrNot = selectedOrNot;
	}
	
	public Result(Applicant Applicant, Test test, int points, boolean selectedOrNot) {
		this.Applicant = Applicant;
		this.test = test;
		this.points = points;
		this.selectedOrNot = selectedOrNot;
	}
	
	
	public Result(String name, String email, int points) {
		this.Applicant = new Applicant(name, email);
		this.points = points;

	}
}
