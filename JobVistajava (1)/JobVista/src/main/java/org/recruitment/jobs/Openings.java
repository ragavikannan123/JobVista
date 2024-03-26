package org.recruitment.jobs;

public class Openings {
	private Long openingId;
    private String description;
    private int experience;
    private String qualification;
    private String departments;
    private String employmentType;
    private String salaryRange;
    private Long panelistId;



    public Openings( String description, int experience, String qualification, String departments,
            String employmentType, String salaryRange, Long panelistId) {
        this.description = description;
        this.experience = experience;
        this.qualification = qualification;
        this.departments = departments;
        this.employmentType = employmentType;
        this.salaryRange = salaryRange;
        this.panelistId = panelistId;
    }
    

    public Openings( String description, String employmentType, Long panelistId) {
        this.description = description;
        this.employmentType = employmentType;
        this.panelistId = panelistId;
    }


    public Openings( String description, String departments, String employmentType, Long panelistId) {
        this.description = description;
        this.departments = departments;
        this.employmentType = employmentType;
        this.panelistId = panelistId;
    }


    public Long getOpeningId() {
        return openingId;
    }

    public void setOpeningId(Long openingId) {
        this.openingId = openingId;
    }

   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public Long getPanelistId() {
        return panelistId;
    }

    public void setPanelistId(Long panelistId) {
        this.panelistId = panelistId;
    }
}