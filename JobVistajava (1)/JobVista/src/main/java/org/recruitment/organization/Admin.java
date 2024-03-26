package org.recruitment.organization;

import java.util.List;

import org.recruitment.users.Panelist;

public class Admin {
	

	private Organisation organisation;
	public String name, email;
	
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public Admin(String name, String email, Organisation organisation) {
//		super(name, email);
		this.organisation = organisation;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	public List<Panelist> getListOfPanelists() {
	    // Return the list of panelists
	    return organisation.listOfPanelists;
	}
	
	public void addPanelist(Panelist panelist) {
		organisation.listOfPanelists.add(panelist);
    }

	
}
