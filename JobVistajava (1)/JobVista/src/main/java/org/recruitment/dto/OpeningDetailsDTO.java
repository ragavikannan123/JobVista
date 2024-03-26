package org.recruitment.dto;

import java.util.Date;

public class OpeningDetailsDTO {
    private long openingId;
    private String description;
    private int experience;
    private String qualification;
    private String departments;
    private String employmentType;
    private String salaryRange;
    private Date startDate;
    private Date endDate;
    private String panelistName;
    private String panelistEmail;
    private String organizationName;
    private String departmentTitle;
    
    private OpeningsDTO opening;
    private PanelistDTO panelist;
    private OrganizationDTO organization;
    private DepartmentsDTO department;


    public OpeningDetailsDTO() {
    }

    public OpeningDetailsDTO(long openingId, String description, int experience, String qualification,
                             String departments, String employmentType, String salaryRange,
                             Date startDate, Date endDate, String panelistName, String panelistEmail,
                             String organizationName, String departmentTitle) {
        this.openingId = openingId;
        this.description = description;
        this.experience = experience;
        this.qualification = qualification;
        this.departments = departments;
        this.employmentType = employmentType;
        this.salaryRange = salaryRange;
        this.startDate = startDate;
        this.endDate = endDate;
        this.panelistName = panelistName;
        this.panelistEmail = panelistEmail;
        this.organizationName = organizationName;
        this.departmentTitle = departmentTitle;
    }
    
    
    public void setOpening(OpeningsDTO opening) {
        this.opening = opening;
    }

    public void setPanelist(PanelistDTO panelist) {
        this.panelist = panelist;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    public void setDepartment(DepartmentsDTO department) {
        this.department = department;
    }

	public long getOpeningId() {
		return openingId;
	}

	public String getDescription() {
		return description;
	}

	public int getExperience() {
		return experience;
	}

	public String getQualification() {
		return qualification;
	}

	public String getDepartments() {
		return departments;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public String getSalaryRange() {
		return salaryRange;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getPanelistName() {
		return panelistName;
	}

	public String getPanelistEmail() {
		return panelistEmail;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public String getDepartmentTitle() {
		return departmentTitle;
	}

	public void setOpeningId(long openingId) {
		this.openingId = openingId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public void setSalaryRange(String salaryRange) {
		this.salaryRange = salaryRange;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setPanelistName(String panelistName) {
		this.panelistName = panelistName;
	}

	public void setPanelistEmail(String panelistEmail) {
		this.panelistEmail = panelistEmail;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public void setDepartmentTitle(String departmentTitle) {
		this.departmentTitle = departmentTitle;
	}

    
}
