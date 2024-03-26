package org.recruitment.dao;

import java.util.List;
import org.recruitment.dto.ApplicantDTO;

public interface ApplicantDAO {
    boolean addApplicant(ApplicantDTO applicantDTO);
    boolean removeApplicant(String name, String email);
    boolean applyForJob(String name, String email, int openingId);
}
