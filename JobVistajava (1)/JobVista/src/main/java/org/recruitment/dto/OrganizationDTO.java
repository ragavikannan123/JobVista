package org.recruitment.dto;

public class OrganizationDTO {
    private int orgId;
    private String name;
    private String typeOfOrg;
    private String industry;
    private String contactEmail;
    private String contactNumber;

    // Default constructor
    public OrganizationDTO() {
    }

    public OrganizationDTO(int orgId, String name, String typeOfOrg, String industry, String contactEmail, String contactNumber) {
        this.orgId = orgId;
        this.name = name;
        this.typeOfOrg = typeOfOrg;
        this.industry = industry;
        this.contactEmail = contactEmail;
        this.contactNumber = contactNumber;
    }

    public OrganizationDTO(String name) {
        this.name = name;
    }

}
