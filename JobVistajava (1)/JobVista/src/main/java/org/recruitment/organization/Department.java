package org.recruitment.organization;

import java.util.List;
import org.recruitment.users.Panelist;

public class Department {
	
	private String title;
	private String description;
	private List<Panelist> panelists;
	
	public Department(String title, Organisation organisation, String description) {
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Panelist> getPanelists(){
		return this.panelists;
	}
	
	public void addPanelist(Panelist panelist) {
		this.panelists.add(panelist);
	}
	
	public void removePanelist(Panelist panelist) {
		this.panelists.remove(panelist);
	}
}
