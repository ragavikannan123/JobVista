package org.recruitment.tests;

import java.util.Date;

import org.recruitment.users.Applicant;

public class Test {
	private String Title;
	private Date date;
	private int Duration;
	private String typeOfTest;
	
	
//	private Result result;
	public void recordTestResult(Applicant applicant, Result result) {
	    // Record the test result for an applicant
	}
	public String getTitle() {
		return Title;
	}
	public Date getDate() {
		return date;
	}
	public int getDuration() {
		return Duration;
	}
	public Test(String title, Date date, int duration, String typeOfTest) {
//		super();
		Title = title;
		this.date = date;
		Duration = duration;
		this.typeOfTest= typeOfTest;
	}
	public String getTypeOfTest() {
		return typeOfTest;
	}
	
	
	

	
}
